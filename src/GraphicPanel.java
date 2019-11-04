import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicPanel extends JPanel implements KeyListener {

    /* The origin of the represented environment will be visible
     at the x0-th pixel column and at the y0-th pixel line,
     starting from the lower-left corner.
     The zoom value is the amount of pixels between that origin
     and the point of coordinates (1, 0).  */
    private double x0, y0, zoom;

    private Displayable displayable;
    private Timer timer;
    private boolean isRunning;

    private int date;

    private Window window;

    private static int NB_GP_CREATED = 0;
    private int serialNumber;

    // Mouse information
    protected double xClick, yClick, xRelease, yRelease, xMouse, yMouse;
    protected double xRightClick, yRightClick;
    private boolean wheelClickIsActive;

    public GraphicPanel() {
        super();
        this.x0 = 67;
        this.y0 = 69;
        this.zoom = 110.8;
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                date++;
                displayable.evolve();
                repaint();
            }
        };
        int period = 10;
        this.timer = new Timer(period, listener);
        this.isRunning = false;
        this.date = 0;

        this.serialNumber = GraphicPanel.NB_GP_CREATED;
        GraphicPanel.NB_GP_CREATED++;

        wheelClickIsActive = false;

        this.addKeyListener(new KeyboardListener(this));
    }

    public GraphicPanel(Displayable d) {
        this();
        this.displayable = d;
    }

    @Override
    public String toString() {
        return "GraphicPanel n° " + this.serialNumber + displayable;
    }

    public void eraseAll(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0,
                (int) (this.getSize().getWidth()),
                (int) (this.getSize().getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {

        this.eraseAll(g);

        int panelHeight = (int) (this.getSize().getHeight());

        this.displayable.paint(g, panelHeight, this.x0, this.y0, this.zoom);

//        this.drawAxis(g, panelHeight);
        if (this.displayable.leftClickIsActive) {
            paintSelectionRectangle(g, panelHeight, this.x0, this.y0, this.zoom);
        }
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
        this.x0 += dx;
        this.y0 += dy;
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

    public void receiveMouseWheelMovement(double fact, int xMouse, int yMouse) {

        double panelHeight = this.getSize().getHeight();

        x0 = fact * (x0 - xMouse) + xMouse;
        y0 = (panelHeight - yMouse) + fact * (y0 - (panelHeight - yMouse));

        this.zoom *= fact;
        repaint();
    }

    public void resetView() {

        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();

        // Displayable dimensions:
        double dispXMax = displayable.getXMax();
        double dispXMin = displayable.getXMin();
        double dispYMax = displayable.getYMax();
        double dispYMin = displayable.getYMin();

        // TODO set the scroll and zoom to show all the terrain at once.
//        this.x0 = 0;
//        this.y0 = 0;
//        this.zoom = 1;
        zoom = panelWidth / (dispXMax - dispXMin);
        x0 = 0;
        y0 = panelHeight / 2 - (dispYMin + dispYMax) * zoom / 2; // Half the panel height plus half the terrain apparent height.

        repaint();
    }

    public void swipe(int dx, int dy) {
        this.x0 += dx;
        this.y0 += dy;
        repaint();
    }

    public void zoomIn() {
        this.zoom *= 1.1;
        repaint();
    }

    public void zoomOut() {
        this.zoom /= 1.1;
        repaint();
    }

    public void play() {
        this.timer.start();
        this.displayable.play();
    }

    public void pause() {
        this.timer.stop();
        this.displayable.pause();
    }

    public void togglePlayPause() {
        if (this.isRunning) {
            this.isRunning = false;
            this.pause();
        } else {
            this.isRunning = true;
            this.play();
        }
    }

    /**
     * Action performed when a left click is received.
     *
     * @param e The event received by the panel
     */
    public void mousePressed(MouseEvent e) {
        xClick = e.getX();
        yClick = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            displayable.receiveLeftClick((xClick - this.x0) / this.zoom, (this.getHeight() - yClick - this.y0) / this.zoom);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = true;
        }
    }

    /**
     * Action performed when a left click release is received.
     *
     * @param e The event recceived by the panel
     */
    public void mouseReleased(MouseEvent e) {
        xRelease = e.getX();
        yRelease = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            displayable.receiveLeftRelease((xRelease - this.x0) / this.zoom, (this.getHeight() - yRelease - this.y0) / this.zoom);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = false;
        }
        repaint();
    }

    /**
     * Action performed when the mouse is moved to the pixel (x, y)
     *
     * @param x the x-position of the mouse after the move, in pixel
     * @param y the y-position of the mouse after the move, in pixel
     */
    public void receiveMouseMoved(int x, int y) {
        displayable.receiveMouseMoved((x - this.x0) / this.zoom, (this.getHeight() - y - this.y0) / this.zoom);

        xMouse = x;
        yMouse = y;

        repaint();
    }

    /**
     * Receive a MouseDragged event, and interpret it as if it happened at the
     * given x-coordinate
     *
     * @param x The x-coordinate of the mouse after the drag event, in pixel
     * @param y The y-coordinate of the mouse after the drag event, in pixel
     */
    public void receiveMouseDragged(int x, int y) {

        if (wheelClickIsActive) {
            this.translate(x - xMouse, yMouse - y);
        } else {
            displayable.receiveMouseDragged((x - this.x0) / this.zoom, (this.getHeight() - y - this.y0) / this.zoom);
        }

        xMouse = x;
        yMouse = y;

        repaint();
    }

    /**
     * Mouse drag: interact with the content of the Displayable
     *
     * @param e the mouse event
     */
    public void receiveMouseDragged(MouseEvent e) {
        receiveMouseDragged(e.getX(), e.getY());
    }

    private void paintSelectionRectangle(Graphics g, int panelHeight, double x0, double y0, double zoom) {

        g.setColor(Color.red);

        int xLeft = (int) Math.min(xClick, xMouse);
        int xRight = (int) Math.max(xClick, xMouse);

        int yMax = (int) Math.max(yClick, yMouse);
        int yMin = (int) Math.min(yClick, yMouse);

        g.drawRect(xLeft, yMin, xRight - xLeft, yMax - yMin);
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
            case 'p':
                togglePlayPause();
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
}
