import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class Window extends JFrame {

    private GraphicPanel panel;
    private int xMouse, yMouse;
    private boolean leftClickActive, centerClickActive, rightClickActive;

    private KeyboardListener keyboardListener;
    JSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(new Dimension(1200, 1020));
        split = null;

        this.addMouseListener(new MyMouseListener());
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.addMouseWheelListener(new MyMouseWheelListener());

        this.xMouse = 0;
        this.yMouse = 0;
        this.leftClickActive = false;
        this.centerClickActive = false;
        this.rightClickActive = false;

        this.setLayout(new BorderLayout());
    }

    public Window(GraphicPanel pan) {
        this();
        this.setPanel(pan);
        this.keyboardListener = new KeyboardListener(this.panel);
        this.addKeyListener(this.keyboardListener);
        this.panel.addKeyListener(this.keyboardListener);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    }

    public Window(GraphicPanel left, GraphicPanel right) {
        this();
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(left);
        split.add(right);
        split.addMouseMotionListener(new MyMouseMotionListener());
        split.addMouseWheelListener(new MyMouseWheelListener());
        this.add(split);
        split.setDividerLocation(SPLIT_PANE_HALF);
    }

    /**
     * Adds one panel to the window. If a panel is already here, create the
     * splitpane.
     *
     * @param newPanel
     */
    public void setPanel(GraphicPanel newPanel) {
        if (panel == null) {
            this.panel = newPanel;
            this.getContentPane().add(this.panel, BorderLayout.CENTER);

        } else {
            split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            split.add(this.panel);
            split.add(newPanel);
            this.add(split);
            split.setDividerLocation(SPLIT_PANE_HALF);
            this.panel = null;
        }
        newPanel.addMouseListener(new MyMouseListener());
        newPanel.addMouseMotionListener(new MyMouseMotionListener());
        newPanel.addMouseWheelListener(new MyMouseWheelListener());
    }

    public void resetSplitPosition() {
        split.setResizeWeight(SPLIT_PANE_HALF);
    }

    /* Change the zoom factor and keep unchanged only the point hovered by the mouse. */
    public void zoomOnMouse(double fact) {
        if (panel != null) {
            this.panel.zoomOnMouse(fact, xMouse, yMouse);
        } else {
            System.out.println(split.getDividerLocation() + ":");
        }
    }

    private class MyMouseMotionListener implements MouseMotionListener {

        public MyMouseMotionListener() {
            super();
        }

        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();
        }

        public void mouseDragged(MouseEvent e) {

            if (split == null) {
                // TODO:  the left and right clicks may have other properties.
                if (centerClickActive || rightClickActive) {
                    /* The Y coordinate axis is oriented downwards in the window,
                     upwards in the GraphicPanel. */
                    panel.translate(e.getX() - xMouse,
                            -(e.getY() - yMouse));
                }

            } else {
                // Transmit the zoom instruction to the appropriate panel
                if (xMouse < split.getDividerLocation()) {
                    ((GraphicPanel) split.getLeftComponent()).translate(e.getX() - xMouse,
                            -(e.getY() - yMouse));
                } else {
                    ((GraphicPanel) split.getRightComponent()).translate(e.getX() - xMouse,
                            -(e.getY() - yMouse));
                }
            }
            xMouse = e.getX();
            yMouse = e.getY();
        }
    }

    private class MyMouseWheelListener implements MouseWheelListener {

        public MyMouseWheelListener() {
            super();
        }

        public void mouseWheelMoved(MouseWheelEvent e) {

            // Define the zoom factor
            double zoomFactor;
            if (e.getWheelRotation() > 0) {
                zoomFactor = 1 / 1.1;
            } else {
                zoomFactor = 1.1;
            }

            if (split == null) {
                panel.zoomOnMouse(zoomFactor, xMouse, yMouse);
            } else {
                // Transmit the zoom instruction to the appropriate panel
                if (xMouse < split.getDividerLocation()) {
                    ((GraphicPanel) split.getLeftComponent()).zoomOnMouse(zoomFactor, xMouse, yMouse);
                } else {
                    ((GraphicPanel) split.getRightComponent()).zoomOnMouse(zoomFactor, xMouse - split.getDividerLocation(), yMouse);
                }
            }
        }
    }

    private class MyMouseListener implements MouseListener {

        public MyMouseListener() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            // System.out.println("Mouse entered");
        }

        public void mouseExited(MouseEvent e) {
            // System.out.println("Mouse exited");
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftClickActive = true;
            }
            if (e.getButton() == MouseEvent.BUTTON2) {
                centerClickActive = true;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightClickActive = true;
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftClickActive = false;
            }
            if (e.getButton() == MouseEvent.BUTTON2) {
                centerClickActive = false;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightClickActive = false;
            }
        }

    }

}
