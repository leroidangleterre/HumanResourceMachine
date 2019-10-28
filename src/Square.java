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
class Square {

    private double x, y;
    private double size;

    private Color color;

    public Square(double x, double y, double size, Color c) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = c;
    }

    public Square() {
        this(0, 0, 0, Color.black);
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (zoom * this.x + x0 - zoom * this.size);
        int yDisplay = (int) (panelHeight - (zoom * this.y + y0 - zoom * this.size));
        int sizeApp = (int) (size * zoom);

        g.setColor(this.color);
        g.fillRect(xDisplay, yDisplay, sizeApp, sizeApp);
    }
}
