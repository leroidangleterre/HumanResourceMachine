import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class Instruction {

    private int rank;
    private double apparentSize;
    protected Color color;

    private double width, height;

    /**
     * Create a new instruction at a specific position in a script.
     *
     * @param r the rank of the new instruction
     */
    public Instruction(int r) {
        this.rank = r;
        this.color = Color.red;
        this.apparentSize = 1.0;
        this.width = 5;
        this.height = 1;
    }

    /**
     * Create a new instruction at the end of a script.
     */
    public Instruction() {
        this(-1);
    }

    /**
     * Set the rank of the instruction in the list. This method does not check
     * the rank of any other instruction.
     *
     * @param newRank the new ran of the instruction
     */
    public void setRank(int newRank) {
        this.rank = newRank;
        this.color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
    }

    /**
     * Paint the representation of the instruction on the given panel.
     *
     * @param g the panel
     * @param panelHeight the panel's height (in pixels)
     * @param x0 the x-component of the scrolling
     * @param y0 the y-component of the scrolling
     * @param zoom the zoom factor
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (x0);
        int yDisplay = (int) (panelHeight - (-zoom * this.rank * apparentSize + y0 - zoom * this.apparentSize));
        int sizeApp = (int) (apparentSize * zoom);

        g.setColor(this.color);
        g.fillRect(xDisplay, yDisplay, sizeApp, sizeApp);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
