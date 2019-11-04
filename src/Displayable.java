import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class is any object displayed in a GraphicPanel. It may be a Terrain or
 * a Script.
 *
 * @author arthurmanoha
 */
public abstract class Displayable implements KeyListener {

    // Flag that indicates that the simulation is running.
    private boolean isRunning;
    protected double xMouse, yMouse;
    protected double xMousePrev, yMousePrev;
    protected double xClick, yClick;
    protected double xRelease, yRelease;
    protected boolean leftClickIsActive;
    protected boolean wheelClickIsActive;

    protected boolean isSelecting;
    protected double xSelection, ySelection;

    private static int NB_DISPL_CREATED = 0;
    private final int serial;

    // Dimensions of the Displayable, used to reset the view
    protected double xMin, xMax, yMin, yMax;

    public Displayable() {
        serial = NB_DISPL_CREATED;
        NB_DISPL_CREATED++;
        leftClickIsActive = false;
        isSelecting = false;
        xMousePrev = 0;
        yMousePrev = 0;

        // Default values, to be redefined in the subclasses.
        xMin = 0;
        xMax = 1;
        yMin = 0;
        yMax = 1;
    }

    @Override
    public String toString() {
        return "Displayable n°" + serial;
    }

    public void evolve() {
    }

    /**
     * Paint the displayable.
     *
     * @param g the Graphics where the Displayable will be painted
     * @param panelHeight the height of the panel
     * @param x0 x-offset for the display
     * @param y0 y-offset for the display
     * @param zoom zoom factor
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {

    }

    public void play() {
        if (!this.isRunning) {
            this.isRunning = true;
        }
    }

    public void pause() {
        if (this.isRunning) {
            this.isRunning = false;
        }
    }

    /**
     * Action performed when a left click occurs. Usually place or move stuff.
     */
    public void receiveLeftClick(double x, double y) {
        this.xClick = x;
        this.yClick = y;
        leftClickIsActive = true;
    }

    /**
     * Action performed when a mouse wheel click occurs. Usually start moving
     * the view.
     */
    public void receiveWheelClick(double x, double y) {
        this.xClick = x;
        this.yClick = y;
        wheelClickIsActive = true;
    }

    /**
     * Action done when the left button is released.
     */
    public void receiveLeftRelease(double x, double y) {
        this.xRelease = x;
        this.yRelease = y;
        leftClickIsActive = false;
    }

    /**
     * Action done when the wheel button is released.
     */
    public void receiveWheelRelease(double x, double y) {
        this.xRelease = x;
        this.yRelease = y;
        wheelClickIsActive = false;
    }

    /**
     * Action performed when the mouse is dragged to the point of
     * world-coordinates (x, y)
     *
     * @param x the x-position of the mouse after the drag, in meters
     * @param y the y-position of the mouse after the drag, in meters
     */
    public void receiveMouseDragged(double x, double y) {

        xMousePrev = xMouse;
        yMousePrev = yMouse;

        xMouse = x;
        yMouse = y;
    }

    /**
     * Action performed when the mouse is moved to the point of
     * world-coordinates (x, y)
     *
     * @param x the x-position of the mouse after the drag, in meters
     * @param y the y-position of the mouse after the drag, in meters
     */
    public void receiveMouseMoved(double x, double y) {

        xMousePrev = xMouse;
        yMousePrev = yMouse;

        xMouse = x;
        yMouse = y;
    }

    /**
     * Return the physical width of the Displayable (that will be used to
     * compute the apparent size when displayed)
     *
     * @return the width of the element
     */
    public abstract double getWidth();

    double getXMax() {
        return xMax;
    }

    double getXMin() {
        return xMin;
    }

    double getYMax() {
        return yMax;
    }

    double getYMin() {
        return yMin;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Displayable.keyTyped");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Displayable.keyPressed");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Displayable.keyReleased");
    }

    /**
     * Select any element that is inside the selection rectangle.
     *
     */
    public abstract void selectContent();
}
