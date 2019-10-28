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
class Displayable {

    // Flag that indicates that the simulation is running.
    private boolean isRunning;

    public Displayable() {

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
}
