import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class Window extends JFrame implements KeyListener {

    private GraphicPanel panel;
    private int xMouse, yMouse;
    private boolean leftClickActive, centerClickActive, rightClickActive;

    int WINDOW_WIDTH = 1600;
    int WINDOW_HEIGHT = 1000;

    CustomSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;
    private boolean leftPanelActive;
    private boolean rightPanelActive;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setLocationRelativeTo(null);
        split = null;

        this.xMouse = 0;
        this.yMouse = 0;
        this.leftClickActive = false;
        this.centerClickActive = false;
        this.rightClickActive = false;
        this.leftPanelActive = false;
        this.rightPanelActive = false;

        this.setLayout(new BorderLayout());

        this.pack();

        this.setLocationRelativeTo(null);

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
//                System.out.println("Window gained focus.");
                if (split != null) {
//                    System.out.println("Split has focus ?  " + split.hasFocus());
                }
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
//                System.out.println("Window lost focus.");
            }
        });

        this.setVisible(true);
    }

    public Window(GraphicPanel pan) {
        this();
        this.setPanel(pan);
        split = new CustomSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.requestFocusInWindow();
        this.setVisible(true);
    }

    public Window(GraphicPanel left, GraphicPanel right) {
        this();
        split = new CustomSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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
            split = new CustomSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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

        @Override
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
                    ((GraphicPanel) split.getRightComponent()).receiveMouseMoved(e.getX() - split.getDividerLocation(), (e.getY()));
                }
                split.setXMouse(xMouse);
            }
        }

        @Override
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
                // A click is ongoing, the panel where the click happened receives the mouseMoved event. 
                if (leftPanelActive) {
                    ((GraphicPanel) split.getLeftComponent()).receiveMouseDragged(e);
                } else if (rightPanelActive) {
                    ((GraphicPanel) split.getRightComponent()).receiveMouseDragged(e.getX() - split.getDividerLocation(), e.getY());
                } else {
                    // No click is ongoing, the panel where the mouse is receives the event.
                    if (xMouse < split.getDividerLocation()) {
                        ((GraphicPanel) split.getLeftComponent()).receiveMouseDragged(e);
                    } else {
                        ((GraphicPanel) split.getRightComponent()).receiveMouseDragged(e.getX() - split.getDividerLocation(), e.getY());
                    }
                }
            }
            split.setXMouse(xMouse);
        }
    }

    private class MyMouseWheelListener implements MouseWheelListener {

        public MyMouseWheelListener() {
            super();
        }

        @Override
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
                if (leftPanelActive) {
                    ((GraphicPanel) split.getLeftComponent()).receiveMouseWheelMovement(zoomFactor, xMouse, yMouse);
                } else if (rightPanelActive) {
                    ((GraphicPanel) split.getRightComponent()).receiveMouseWheelMovement(zoomFactor, xMouse - split.getDividerLocation(), yMouse);
                } else if (xMouse < split.getDividerLocation()) {
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

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

            // One of the two panels will receive the event.
            GraphicPanel panel;
            // x-offset : zero for the left panel, non-zero for the right panel
            int xOffset = 0;
            if (leftPanelActive) {
                panel = (GraphicPanel) split.getLeftComponent();
            } else if (rightPanelActive) {
                panel = (GraphicPanel) split.getRightComponent();
            } else {
                if (xMouse < split.getDividerLocation()) {
                    panel = (GraphicPanel) split.getLeftComponent();
                    leftPanelActive = true;
                } else {
                    panel = (GraphicPanel) split.getRightComponent();
                    xOffset = -split.getDividerLocation();
                    rightPanelActive = true;
                }
            }

            // If the click takes place on the right-hand side of the split, the event's coordinates
            // are expressed in the referential of the appropriate panel (left or right).
            MouseEvent newEvent = new MouseEvent((Component) e.getSource(), e.getID(),
                    e.getWhen(), e.getModifiers(), e.getX() + xOffset, e.getY(), e.getClickCount(), false);

            panel.mousePressed(newEvent);

            if (e.getButton() == MouseEvent.BUTTON1) {
                leftClickActive = true;
            }
            if (e.getButton() == MouseEvent.BUTTON2) {
                centerClickActive = true;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightClickActive = true;
                split.setRightClickActive(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            // One of the two panels will receive the event.
            GraphicPanel panel;
            if (leftPanelActive) {
                panel = (GraphicPanel) split.getLeftComponent();
            } else if (rightPanelActive) {
                panel = (GraphicPanel) split.getRightComponent();
            } else {
                if (xMouse < split.getDividerLocation()) {
                    panel = (GraphicPanel) split.getLeftComponent();
                } else {
                    panel = (GraphicPanel) split.getRightComponent();
                }
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
                split.setRightClickActive(false);
            }

            // The currently active panel becomes inactive if no mouse button remains pressed.
            if (!leftClickActive && !centerClickActive & !rightClickActive) {
                leftPanelActive = false;
                rightPanelActive = false;
                split.setRightPanelActive(false);
                split.setLeftPanelActive(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Window: keyTyped");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Window: keyPressed");

        // One of the two panels will receive the event.
        GraphicPanel activePanel;
        if (leftPanelActive) {
            activePanel = (GraphicPanel) split.getLeftComponent();
        } else if (rightClickActive) {
            activePanel = (GraphicPanel) split.getRightComponent();
        } else {
            if (xMouse < split.getDividerLocation()) {
                activePanel = (GraphicPanel) split.getLeftComponent();
            } else {
                activePanel = (GraphicPanel) split.getRightComponent();
            }
        }

        activePanel.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Window: keyReleased");
    }

}
