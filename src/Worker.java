import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This class represents a character that obeys a Script and that may move in
 * and act upon its environment.
 *
 * @author arthurmanoha
 */
public class Worker implements Observable {

    /**
     * The worker's current position (its center).
     */
    private double x, y;
    private double size = 1;

    private static int NB_WORKERS = 0;
    private int serial;

    private ArrayList<Observer> observersList;

    // If the worker is moving, we need to know in which direction.
    private CardinalPoint currentHeading;

    // The index of the instruction about to be executed by the Worker
    private int currentAddress;

    // A worker may carry a data cube.
    private DataCube dataCube;

    /**
     * The date of the last time this worker executed an instruction.
     *
     */
    private int date;

    public Worker() {
        x = 0;
        y = 0;
        serial = NB_WORKERS;
        NB_WORKERS++;
        observersList = new ArrayList<>();
        currentAddress = -1;
    }

    public void setPosition(double xParam, double yParam) {
        x = xParam;
        y = yParam;
        if (dataCube != null) {
            dataCube.setPosition(this.x, this.y);
        }
    }

    public void setDate(int newDate) {
        date = newDate;
    }

    public int getDate() {
        return date;
    }

    public void setCurrentAddress(int newAdress) {
        currentAddress = newAdress;
    }

    public int getCurrentAddress() {
        return this.currentAddress;
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (zoom * (this.x - this.size / 2) + x0);
        int yDisplay = (int) (panelHeight - (zoom * (this.y + this.size / 2) + y0));
        int apparentDiameter = (int) (size * zoom);

        g.setColor(Color.gray);
        g.fillOval(xDisplay, yDisplay, apparentDiameter, apparentDiameter);

        String text;
        if (dataCube == null) {
            text = "<no cube>";
        } else {
            text = "<cube " + dataCube.getValue() + ">";
        }
        g.setColor(Color.black);
        g.drawString(text, xDisplay + apparentDiameter / 2, yDisplay + apparentDiameter / 2);
        if (this.hasDataCube()) {
            dataCube.paint(g, panelHeight, x0, y0, zoom);
        }
    }

    public int getSerial() {
        return serial;
    }

    /**
     * Apply an instruction sent by the script.
     *
     * @param ins the instruction to execute.
     */
    public void executeInstruction(InstructionModel ins) {

        Notification notif = new Notification("WorkerMove", this);
        notifyObservers(notif);
    }

    @Override
    public void addObserver(Observer obs) {
        observersList.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
    }

    @Override
    public void notifyObservers(Notification n) {
        for (Observer obs : observersList) {
            obs.update(n);
        }
    }

    CardinalPoint getDirection() {
        return this.currentHeading;
    }

    /**
     * Request from the Terrain to move in the given direction.
     *
     * @param cardinalPoint
     */
    public void moveInDirection(CardinalPoint cardinalPoint) {
        currentHeading = cardinalPoint;
        Notification notif = new Notification("WorkerMove", this);
        notifyObservers(notif);
    }

    public void pickup(CardinalPoint direction) {
        currentHeading = direction;
        Notification notif = new Notification("WorkerPickup", this);
        notifyObservers(notif);
    }

    public void drop() {
        Notification notif = new Notification("WorkerDrop", this);
        notifyObservers(notif);
    }

    /**
     * Test if the worker is carrying a cube.
     *
     * @return true when the worker has a cube, false otherwise.
     */
    public boolean hasDataCube() {
        return (this.dataCube != null);
    }

    /**
     * Receive a data cube and keep it until further notice.
     *
     * @param newCube the new cube
     */
    public void setDataCube(DataCube newCube) {
        if (dataCube == null) {
            dataCube = newCube;
            dataCube.setPosition(this.x, this.y);
        }
    }

    public DataCube removeDataCube() {
        if (dataCube == null) {
            return null;
        } else {
            DataCube cube = this.dataCube;
            this.dataCube = null;
            return cube;
        }
    }

    public int getNbCubes() {
        if (dataCube == null) {
            return 0;
        } else {
            return 1;
        }
    }
}
