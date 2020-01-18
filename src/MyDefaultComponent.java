import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

public abstract class MyDefaultComponent extends JPanel implements MouseListener,
        MouseMotionListener, MouseWheelListener, KeyListener {

    protected MyDefaultModel model;

    /* The origin of the represented environment will be visible
     at the x0-th pixel column and at the y0-th pixel line,
     starting from the lower-left corner.
     The zoom value is the amount of pixels between that origin
     and the point of coordinates (1, 0).  */
    protected double x0, y0, zoom;
    protected int panelHeight;

    protected boolean selectionIsMoving;
    protected boolean isSelecting;

    protected Window window;

    private static int NB_GP_CREATED = 0;
    protected int serialNumber;

    // Mouse information. In PIXELS, not converted !
    protected double xClick, yClick, xRelease, yRelease, xMouse, yMouse;
    protected double xMousePrevious, yMousePrevious;
    protected double xRightClick, yRightClick;
    protected boolean leftClickIsActive, wheelClickIsActive;

    private static int COMPONENT_WIDTH = 800;
    private static int COMPONENT_HEIGHT = 700;

    public MyDefaultComponent() {
        super();
        this.x0 = 67;
        this.y0 = 69;
        this.zoom = 110.8;

        setPreferredSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));
        setSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));

        this.serialNumber = MyDefaultComponent.NB_GP_CREATED;
        MyDefaultComponent.NB_GP_CREATED++;

        wheelClickIsActive = false;
        leftClickIsActive = false;
        selectionIsMoving = false;
        isSelecting = false;

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        xMouse = 0;
        yMouse = 0;
        xMousePrevious = 0;
        yMousePrevious = 0;

        repaint();
    }

    /**
     * Get the model associated with this component
     *
     * @return the model.
     */
    public MyDefaultModel getModel() {
        return model;
    }

    public void eraseAll(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0,
                (int) (this.getSize().getWidth()),
                (int) (this.getSize().getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {

        panelHeight = (int) (this.getSize().getHeight());

        this.drawAxis(g, panelHeight);
        if (this.leftClickIsActive && !selectionIsMoving) {
            paintSelectionRectangle(g, panelHeight, this.x0, this.y0, this.zoom);
        }
    }

    private void paintSelectionRectangle(Graphics g, int panelHeight, double x0, double y0, double zoom) {

        g.setColor(Color.blue);

        int xLeft = (int) Math.min(xClick, xMouse);
        int xRight = (int) Math.max(xClick, xMouse);

        int yMax = (int) Math.max(yClick, yMouse);
        int yMin = (int) Math.min(yClick, yMouse);

        g.drawRect(xLeft, yMin, xRight - xLeft, yMax - yMin);
    }

    public void drawAxis(Graphics g, double panelHeight) {
        g.setColor(Color.BLACK);
        g.drawLine((int) (this.x0), (int) (panelHeight - this.y0),
                (int) (this.x0 + 10 * this.zoom), (int) (panelHeight - this.y0));
        g.drawLine((int) (this.x0), (int) (panelHeight - this.y0),
                (int) (this.x0), (int) (panelHeight - (this.y0 + 10 * this.zoom)));
    }

    public double getX0() {
        return this.x0;
    }

    public void setX0(double newX0) {
        this.x0 = newX0;
        repaint();
    }

    public double getY0() {
        return this.x0;
    }

    public void setY0(double newY0) {
        this.y0 = newY0;
        repaint();
    }

    public void translate(double dx, double dy) {
        setX0(x0 + dx);
        setY0(y0 + dy);
        repaint();
    }

    public double getZoom() {
        return this.zoom;
    }

    public void setZoom(double newZoom) {
        this.zoom = newZoom;
        repaint();
    }

    public void multiplyZoom(double fact) {
        this.zoom *= fact;
        repaint();
    }

    public void resetView() {

        int panelWidth = this.getWidth();
        panelHeight = this.getHeight();

        // Displayable dimensions:
        double dispXMax = model.getXMax();
        double dispXMin = model.getXMin();
        double dispYMax = model.getYMax();
        double dispYMin = model.getYMin();

        zoom = panelWidth / (dispXMax - dispXMin);
        setX0(0);
        setY0(panelHeight / 2 - (dispYMin + dispYMax) * zoom / 2);

        repaint();
    }

    public void swipe(int dx, int dy) {
        setX0(x0 + dx);
        setY0(y0 + dy);
        repaint();
    }

    public void zoomIn() {
        setZoom(zoom * 1.1);
        repaint();
    }

    public void zoomOut() {
        setZoom(zoom / 1.1);
        repaint();
    }

    /**
     * Action performed when a left click is received.
     *
     * @param e The event received by the panel
     */
    @Override
    public void mousePressed(MouseEvent e) {
        xClick = e.getX();
        yClick = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = true;
        }
    }

    /**
     * Action performed when a left click release is received.
     *
     * @param e The event recceived by the panel
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        xRelease = e.getX();
        yRelease = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = false;
        }
        repaint();
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

    /**
     * Action performed when the mouse is moved to the pixel (x, y)
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        xMousePrevious = xMouse;
        yMousePrevious = yMouse;

        xMouse = e.getX();
        yMouse = e.getY();
    }

    /**
     * Action performed when the mouse is dragged to the pixel (x, y)
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isSelecting) {
        } else if (selectionIsMoving) {
        } else {
            setX0(x0 + e.getX() - xMouse);
            setY0(y0 - (e.getY() - yMouse)); // Y-axis is inverted
        }
        xMouse = e.getX();
        yMouse = e.getY();

        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Define the zoom factor
        double zoomFactor;
        if (e.getWheelRotation() > 0) {
            zoomFactor = 1 / 1.1;
        } else {
            zoomFactor = 1.1;
        }

        panelHeight = (int) this.getSize().getHeight();

        setX0(zoomFactor * (x0 - xMouse) + xMouse);
        setY0((panelHeight - yMouse) + zoomFactor * (y0 - (panelHeight - yMouse)));

        setZoom(zoom * zoomFactor);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '0':
                resetView();
                break;
            case '4':
                swipe(-1, 0);
                break;
            case '6':
                swipe(+1, 0);
                break;
            case '8':
                swipe(0, +1);
                break;
            case '2':
                swipe(0, -1);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Receive a command.
     *
     * @param text the command
     */
    public abstract void receiveCommand(String text);
}
