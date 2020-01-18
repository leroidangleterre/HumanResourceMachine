import java.awt.Color;
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * A single square that the terrain is made of.
 *
 * @author arthurmanoha
 */
public class Square {

    private double xCenter, yCenter;
    private double size;

    private Color color;

    private boolean isSelected;

    private Worker worker;

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
}
