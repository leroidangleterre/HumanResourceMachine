import java.awt.Graphics;

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
public abstract class Displayable {

    // Flag that indicates that the simulation is running.
    private boolean isRunning;
    protected double xMouse, yMouse;
    protected double xMousePrev, yMousePrev;
    protected double xClick, yClick;
    protected double xRelease, yRelease;
    protected boolean lefClickIsActive;

    protected boolean isSelecting;
    protected double xSelection, ySelection;

    private static int NB_DISPL_CREATED = 0;
    private final int serial;

    public Displayable() {
        serial = NB_DISPL_CREATED;
        NB_DISPL_CREATED++;
        lefClickIsActive = false;
        isSelecting = false;
        xMousePrev = 0;
        yMousePrev = 0;
    }

    public String toString() {
        return "Displayable n°" + serial;
    }

    public void evolve() {
    }

    /**
     * Paint the displayable.
     *
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
    public abstract void receiveLeftClick(double x, double y);

    /**
     * Action done when the left button is released.
     */
    public abstract void receiveLeftRelease(double x, double yl);

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

}
