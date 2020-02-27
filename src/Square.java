import java.awt.Color;
import java.awt.Graphics;

/**
 * A single square that the terrain is made of.
 *
 * @author arthurmanoha
 */
public class Square {

    protected double xCenter, yCenter;
    protected double size;

    protected Color color;

    private boolean isSelected;

    private Worker worker;
    private DataCube dataCube;

    /**
     * Create a square centered at the given (x,y)
     *
     * @param x x-coordinate of the center of the square
     * @param y y-coordinate of the center of the square
     * @param size size of the square
     * @param c color of the square
     */
    public Square(double x, double y, double size, Color c) {
        this.xCenter = x;
        this.yCenter = y;
        this.size = size;
        this.color = c;
        isSelected = false;
        worker = null;
        dataCube = null;
    }

    public Square() {
        this(0, 0, 0, Color.black);
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (zoom * (this.xCenter - this.size / 2) + x0);
        int yDisplay = (int) (panelHeight - (zoom * (this.yCenter + this.size / 2) + y0));
        int sizeApp = (int) (size * zoom);

        g.setColor(this.color);
        g.fillRect(xDisplay, yDisplay, sizeApp, sizeApp);
        g.setColor(Color.black);
        if (isSelected) {
            g.setColor(Color.yellow);
        }
        g.drawRect(xDisplay, yDisplay, sizeApp, sizeApp);

        if (dataCube != null) {
            dataCube.paint(g, panelHeight, x0, y0, zoom);
        }

        if (worker != null) {
            worker.paint(g, panelHeight, x0, y0, zoom);
        }
    }

    public double getX() {
        return this.xCenter;
    }

    public double getY() {
        return this.yCenter;
    }

    public double getXMin() {
        return this.xCenter - this.size / 2;
    }

    public double getXMax() {
        return this.xCenter + this.size / 2;
    }

    public double getYMin() {
        return this.yCenter - this.size / 2;
    }

    public double getYMax() {
        return this.yCenter + this.size / 2;
    }

    public void setSelected(boolean newIsSelected) {
        isSelected = newIsSelected;
    }

    public void receiveWorker(Worker newGuy) {
        if (worker == null) {
            worker = newGuy;
            worker.setPosition(xCenter, yCenter);
        }
    }

    /**
     * Remove and return the worker from the square, if one exists.
     *
     * @return null if no worker occupies the square, or the worker, which gets
     * removed.
     */
    public Worker removeWorker() {
        Worker result = this.worker;
        this.worker = null;
        return result;
    }

    /**
     * Check if the required worker is in the current Square.
     *
     * @param wantedGuy
     * @return true if wantedGuy is here, false otherwise.
     */
    public boolean containsWorker(Worker wantedGuy) {
        return (this.worker == wantedGuy);
    }

    public boolean containsWorker() {
        return (this.worker != null);
    }

    /**
     * Return the worker contained in this square if it has the right ID.
     *
     * @param id the requested worker ID.
     * @return the worker
     */
    public Worker getWorker(int id) {
        if (this.worker != null && this.worker.getSerial() == id) {
            return this.worker;
        } else {
            return null;
        }
    }

    public boolean containsDataCube() {
        return (this.dataCube != null);
    }

    public void createDataCube(int val) {
        this.dataCube = new DataCube(this.getX(), this.getY(), val);
    }

    /**
     * Add a new data cube, only if the square does not already hold a data
     * cube.
     *
     * @param newCube the newly added cube
     */
    public void addDataCube(DataCube newCube) {
        if (!this.containsDataCube()) {
            this.dataCube = newCube;
        }
    }

    /**
     * Remove and return the data cube from the square, if one exists.
     *
     * @return null if no data cube occupies the square, or the data cube, which
     * gets removed.
     */
    public DataCube removeDataCube() {
        DataCube result = this.dataCube;
        this.dataCube = null;
        return result;
    }

    /**
     * Return the dataCube without removeing it from the square.
     *
     * @return the data cube if it exists, or null.
     */
    public DataCube getDataCube() {
        return this.dataCube;
    }

    /**
     * Return the ID of the worker if there is one in this square, -1 if no
     * worker is there.
     *
     * @return
     */
    public int getWorkerId() {
        if (this.worker == null) {
            return -1;
        } else {
            return this.worker.getSerial();
        }
    }

    public int getNbCubes() {

        int count = 0;

        if (dataCube != null) {
            count++;
        }

        if (worker != null) {
            count += worker.getNbCubes();
        }
        return count;
    }
}
