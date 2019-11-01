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

    int WINDOW_WIDTH = 1000;
    int WINDOW_HEIGHT = 800;

    private KeyboardListener keyboardListener;
    JSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.95;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.pack();
        this.setLocationRelativeTo(null);
        split = null;

        this.xMouse = 0;
        this.yMouse = 0;
        this.leftClickActive = false;
        this.centerClickActive = false;
        this.rightClickActive = false;

        this.setLayout(new BorderLayout());

        this.pack();
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
        split.setOneTouchExpandable(true);
        split.resetToPreferredSizes();
        split.add(left);
        split.add(right);
        split.addMouseMotionListener(new MyMouseMotionListener());
        split.addMouseWheelListener(new MyMouseWheelListener());
        split.addMouseListener(new MyMouseListener());

        this.add(split);
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
            this.panel = null;
        }
    }

    public void resetSplitPosition() {
        split.setResizeWeight(SPLIT_PANE_HALF);
        split.setDividerLocation(SPLIT_PANE_HALF);
    }

    private class MyMouseMotionListener implements MouseMotionListener {

        public MyMouseMotionListener() {
            super();
        }

        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();
            if (split != null) {
                // Transmit the zoom instruction to the appropriate panel,
                // based on the position of the mouse relative to the split.
                int xLimit = split.getDividerLocation();
                if (xMouse < xLimit) {
                    ((GraphicPanel) split.getLeftComponent()).receiveMouseMoved(e.getX(), (e.getY()));
                } else {
                    ((GraphicPanel) split.getRightComponent()).receiveMouseMoved(e.getX(), (e.getY()));
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();

            if (split == null) {
                if (centerClickActive || rightClickActive) {
                    /* The Y coordinate axis is oriented downwards in the window,
                     upwards in the GraphicPanel. */
                    panel.translate(e.getX() - xMouse,
                            -(e.getY() - yMouse));
                }

            } else {
                // Transmit the zoom instruction to the appropriate panel
                if (xMouse < split.getDividerLocation()) {
                    ((GraphicPanel) split.getLeftComponent()).receiveMouseDragged(e.getX(),
                            (e.getY()));
                } else {
                    ((GraphicPanel) split.getRightComponent()).receiveMouseDragged(e.getX(),
                            (e.getY()));
                }
            }
        }
    }

    private class MyMouseWheelListener implements MouseWheelListener {

        public MyMouseWheelListener() {
            super();
        }

        public void mouseWheelMoved(MouseWheelEvent e) {

            xMouse = e.getX();
            yMouse = e.getY();

            // Define the zoom factor
            double zoomFactor;
            if (e.getWheelRotation() > 0) {
                zoomFactor = 1 / 1.1;
            } else {
                zoomFactor = 1.1;
            }

            if (split == null) {
                panel.receiveMouseWheelMovement(zoomFactor, xMouse, yMouse);
            } else {
                // Transmit the zoom instruction to the appropriate panel
                if (xMouse < split.getDividerLocation()) {
                    ((GraphicPanel) split.getLeftComponent()).receiveMouseWheelMovement(zoomFactor, xMouse, yMouse);
                } else {
                    ((GraphicPanel) split.getRightComponent()).receiveMouseWheelMovement(zoomFactor, xMouse - split.getDividerLocation(), yMouse);
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
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {

            // One of the two panels will receive the event.
            GraphicPanel panel;
            if (xMouse < split.getDividerLocation()) {
                panel = (GraphicPanel) split.getLeftComponent();
            } else {
                panel = (GraphicPanel) split.getRightComponent();
            }

            panel.mousePressed(e);

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

            // One of the two panels will receive the event.
            GraphicPanel panel;
            if (xMouse < split.getDividerLocation()) {
                panel = (GraphicPanel) split.getLeftComponent();
            } else {
                panel = (GraphicPanel) split.getRightComponent();
            }

            panel.mouseReleased(e);

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
