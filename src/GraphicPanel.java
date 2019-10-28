import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicPanel extends JPanel {

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

    public GraphicPanel() {
        super();
        this.x0 = 237;
        this.y0 = 659;
        this.zoom = 9.64;
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
    }

    public GraphicPanel(Displayable d) {
        this();
        this.displayable = d;
    }

    public void eraseAll(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0,
                (int) (this.getSize().getWidth()),
                (int) (this.getSize().getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("GP.paintComponent()");

        this.eraseAll(g);

        int panelHeight = (int) (this.getSize().getHeight());

        this.displayable.paint(g, panelHeight, this.x0, this.y0, this.zoom);

        this.drawAxis(g, panelHeight);
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

    public void zoomOnMouse(double fact, int xMouse, int yMouse) {

//        System.out.print("GraphicPanel.zoomOnMouse(...) ; old zoom is" + zoom);
        double panelHeight = this.getSize().getHeight();

        x0 = fact * (x0 - xMouse) + xMouse;
        y0 = (panelHeight - yMouse) + fact * (y0 - (panelHeight - yMouse));

        this.zoom *= fact;
        repaint();
//        System.out.println("; fact is " + fact + "; new zoom is " + zoom);
    }

    public void resetView() {

        /* Maximal dimensions of the world (blocks + machines). */
        int width = this.getWidth();
        int height = this.getHeight();

        this.x0 = 0;
        this.y0 = 0;
        this.zoom = 1;
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

//    public void evolve() {
//        this.evolve();
//    }
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
}
