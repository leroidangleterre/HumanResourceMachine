package humanresourcemachine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

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

    private static BufferedImage imageNorth;
    private static BufferedImage imageSouth;
    private static BufferedImage imageWest;
    private static BufferedImage imageEast;
    private BufferedImage currentImage;

    /**
     * The date of the last time this worker executed an instruction.
     *
     */
    private int date;

    /**
     * The maximum amount of consecutive datacubes that a worker is able to
     * push.
     *
     */
    private int pushAmount;

    public Worker() {
        x = 0;
        y = 0;
        serial = NB_WORKERS;
        NB_WORKERS++;
        observersList = new ArrayList<>();
        currentAddress = -1;

        if (imageNorth == null) {
            try {
                imageNorth = ImageIO.read(new File("C:\\Users\\arthurmanoha\\Documents\\Programmation\\Java\\HumanResourceMachine\\src\\img_worker_north.png"));
                imageSouth = ImageIO.read(new File("C:\\Users\\arthurmanoha\\Documents\\Programmation\\Java\\HumanResourceMachine\\src\\img_worker_south.png"));
                imageEast = ImageIO.read(new File("C:\\Users\\arthurmanoha\\Documents\\Programmation\\Java\\HumanResourceMachine\\src\\img_worker_east.png"));
                imageWest = ImageIO.read(new File("C:\\Users\\arthurmanoha\\Documents\\Programmation\\Java\\HumanResourceMachine\\src\\img_worker_west.png"));
            } catch (IOException e) {
                System.out.println("Worker images loading failed.");
            }
        }
        currentHeading = CardinalPoint.SOUTH;
        currentImage = imageSouth;

        pushAmount = 1;
    }

    public void setX(double newX) {
        this.x = newX;
        if (dataCube != null) {
            dataCube.setX(newX);
        }
    }

    public void setY(double newY) {
        this.y = newY;
        if (dataCube != null) {
            dataCube.setY(newY);
        }
    }

    public void setPosition(double xParam, double yParam) {
        setX(xParam);
        setY(yParam);
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

        if (currentImage == null) {
            g.setColor(Color.gray);
            g.fillOval(xDisplay, yDisplay, apparentDiameter, apparentDiameter);
        } else {
            g.drawImage(currentImage, xDisplay, yDisplay, (int) zoom, (int) zoom, null);
        }

        g.setColor(Color.black);
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

    public void setCurrentHeading(String newHeading) {
        switch (newHeading) {
        case "NORTH":
            currentHeading = CardinalPoint.NORTH;
            currentImage = imageNorth;
            break;
        case "SOUTH":
            currentHeading = CardinalPoint.SOUTH;
            currentImage = imageSouth;
            break;
        case "EAST":
            currentHeading = CardinalPoint.EAST;
            currentImage = imageEast;
            break;
        case "WEST":
            currentHeading = CardinalPoint.WEST;
            currentImage = imageWest;
            break;
        default:
            System.out.println("Worker.setCurrentHeading - invalid parameter: " + newHeading);
            currentHeading = CardinalPoint.SOUTH;
            break;
        }
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
            dataCube.setCarried(true);
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

    public int getCubeValue() {
        return dataCube.getValue();
    }

    public int getPushAmount() {
        return this.pushAmount;
    }
}
