import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Window extends JFrame implements KeyListener {

    private MyDefaultComponent panel;
    private int xMouse, yMouse;
    private boolean leftClickActive, centerClickActive, rightClickActive;

    int WINDOW_WIDTH = 1600;
    int WINDOW_HEIGHT = 1000;

    CustomSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;
    private boolean leftPanelActive;
    private boolean rightPanelActive;

    private JPanel buttonsPanel;

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

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 10));
        prepareButtonsPanel();

        this.add(buttonsPanel, BorderLayout.NORTH);

        this.pack();

        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    public Window(MyDefaultComponent pan) {
        this();
        this.setPanel(pan);
        split = new CustomSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.requestFocusInWindow();
        this.setVisible(true);
    }

    public Window(MyDefaultComponent left, MyDefaultComponent right) {
        this();
        split = new CustomSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setOneTouchExpandable(true);
        split.resetToPreferredSizes();
        split.add(left);
        split.add(right);
        split.addMouseMotionListener(new MyMouseMotionListener());
        split.addMouseWheelListener(new MyMouseWheelListener());
        split.addMouseListener(new MyMouseListener());

        this.add(split, BorderLayout.CENTER);
    }

    /**
     * Adds one panel to the window. If a panel is already here, create the
     * splitpane.
     *
     * @param newPanel
     */
    public void setPanel(MyDefaultComponent newPanel) {
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

    /**
     * Create the control buttons and place them on their panel.
     *
     */
    private void prepareButtonsPanel() {

        String buttonNames[] = {"Selection", "Hole", "Ground", "Input", "Worker", "Move", "Pickup", "Drop"};
        int rank = 0;
        for (String text : buttonNames) {
            JButton newButton = new JButton(text);
            newButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    split.receiveCommand(text.toUpperCase());
                }
            });
            buttonsPanel.add(newButton, rank);
            rank++;
        }
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
                    ((MyDefaultComponent) split.getLeftComponent()).mouseMoved(e.getX(), (e.getY()));
                } else {
                    ((MyDefaultComponent) split.getRightComponent()).mouseMoved(e.getX() - split.getDividerLocation(), (e.getY()));
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
                    ((MyDefaultComponent) split.getLeftComponent()).mouseDragged(e);
                } else if (rightPanelActive) {
                    MouseEvent eOffset = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() - split.getDividerLocation(), e.getY(), e.getClickCount(), false);
                    ((MyDefaultComponent) split.getRightComponent()).mouseDragged(eOffset);
                } else {
                    // No click is ongoing, the panel where the mouse is receives the event.
                    if (xMouse < split.getDividerLocation()) {
                        ((MyDefaultComponent) split.getLeftComponent()).mouseDragged(e);
                    } else {
                        MouseEvent eOffset = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() - split.getDividerLocation(), e.getY(), e.getClickCount(), false);
                        ((MyDefaultComponent) split.getRightComponent()).mouseDragged(eOffset);
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
                panel.mouseWheelMoved(zoomFactor, xMouse, yMouse);
            } else {
                // Transmit the zoom instruction to the appropriate panel
                if (leftPanelActive) {
                    ((MyDefaultComponent) split.getLeftComponent()).mouseWheelMoved(zoomFactor, xMouse, yMouse);
                } else if (rightPanelActive) {
                    ((MyDefaultComponent) split.getRightComponent()).mouseWheelMoved(zoomFactor, xMouse - split.getDividerLocation(), yMouse);
                } else if (xMouse < split.getDividerLocation()) {
                    ((MyDefaultComponent) split.getLeftComponent()).mouseWheelMoved(zoomFactor, xMouse, yMouse);
                } else {
                    ((MyDefaultComponent) split.getRightComponent()).mouseWheelMoved(zoomFactor, xMouse - split.getDividerLocation(), yMouse);
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
            MyDefaultComponent panel;
            // x-offset : zero for the left panel, non-zero for the right panel
            int xOffset = 0;
            if (leftPanelActive) {
                panel = (MyDefaultComponent) split.getLeftComponent();
            } else if (rightPanelActive) {
                panel = (MyDefaultComponent) split.getRightComponent();
            } else {
                if (xMouse < split.getDividerLocation()) {
                    panel = (MyDefaultComponent) split.getLeftComponent();
                    leftPanelActive = true;
                } else {
                    panel = (MyDefaultComponent) split.getRightComponent();
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
            MyDefaultComponent panel;
            if (leftPanelActive) {
                panel = (MyDefaultComponent) split.getLeftComponent();
            } else if (rightPanelActive) {
                panel = (MyDefaultComponent) split.getRightComponent();
            } else {
                if (xMouse < split.getDividerLocation()) {
                    panel = (MyDefaultComponent) split.getLeftComponent();
                } else {
                    panel = (MyDefaultComponent) split.getRightComponent();
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
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // One of the two panels will receive the event.
        MyDefaultComponent activePanel;
        if (leftPanelActive) {
            activePanel = (MyDefaultComponent) split.getLeftComponent();
        } else if (rightClickActive) {
            activePanel = (MyDefaultComponent) split.getRightComponent();
        } else {
            if (xMouse < split.getDividerLocation()) {
                activePanel = (MyDefaultComponent) split.getLeftComponent();
            } else {
                activePanel = (MyDefaultComponent) split.getRightComponent();
            }
        }

        activePanel.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
