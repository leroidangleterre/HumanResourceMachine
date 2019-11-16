import java.awt.Color;
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class represents a character that obeys a Script and that may move in
 * and act upon its environment.
 *
 * @author arthurmanoha
 */
public class Worker {

    /**
     * The worker's current position (its center).
     */
    private double x, y;
    private double size = 1;

    public Worker() {
        x = 0;
        y = 0;
    }

    public void setPosition(double xParam, double yParam) {
        x = xParam;
        y = yParam;
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (zoom * (this.x - this.size / 2) + x0);
        int yDisplay = (int) (panelHeight - (zoom * (this.y + this.size / 2) + y0));
        int apparentDiameter = (int) (size * zoom);

        g.setColor(Color.gray);
        g.fillOval(xDisplay, yDisplay, apparentDiameter, apparentDiameter);
    }
}
