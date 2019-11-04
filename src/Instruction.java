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

    protected Color color;

    private final double width, height;

    private boolean isSelected;

    /**
     * Create a new instruction
     */
    public Instruction() {
        this.color = Color.red;
        this.width = 3 + 2 * Math.random();
        this.height = 1;
    }

    /**
     * Paint the representation of the instruction on the given panel.
     *
     * @param g the panel
     * @param panelHeight the panel's height (in pixels)
     * @param xDisplay the x apparent position on the graphics
     * @param yDisplay the y apparent position on the graphics
     * @param zoom the zoom factor
     */
    public void paint(Graphics g, int panelHeight, int xDisplay, int yDisplay, double zoom) {
        int appWidth = (int) (this.width * zoom);
        int appHeight = (int) (this.height * zoom);

        g.setColor(this.color);
        g.fillRect(xDisplay, yDisplay, appWidth, appHeight);
        if (isSelected) {
            g.setColor(Color.green);
        }
        g.drawRect(xDisplay, yDisplay, appWidth, appHeight - 1);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setSelected(boolean newIsSelected) {
        isSelected = newIsSelected;
    }

    /**
     * The height of each instruction.
     *
     * @return the height (in meters, not pixels) of any given instruction.
     */
    public double getSize() {
        return height;
    }
}
