package humanresourcemachine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * This instruction is used to make a Worker drop their data block on the Square
 * they occupy.
 *
 * @author arthurmanoha
 */
public class DropInstruction extends Instruction {

    private double textRelativeX = 0.01;

    public DropInstruction() {
        super();

        model = new DropInstructionModel();
        model.setText("Drop");

        color = Color.gray;
    }

    /**
     * Specify at what position the component will be drawn.
     *
     * @param newX
     * @param newY
     */
    @Override
    public void setPosition(int newX, int newY) {
        super.setPosition(newX, newY);
    }

    /**
     * Paint the instruction
     *
     * @param g the Graphics we paint on
     * @param panelHeight the height of the drawing area
     * @param x0 x-offset for the origin of the drawing
     * @param y0 y-offset for the origin of the drawing
     * @param zoom zoom factor used to scale things INSIDE the Instruction
     * component.
     */
    @Override
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        super.paint(g, panelHeight, x0, y0, zoom);

        int margin = 0;
        int xDisplay = (int) x0 + this.x + margin;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        String text = "Drop";
        g.setColor(Color.orange);
        setFont(g);
        int xText = (int) (xDisplay + textRelativeX * this.width * zoom);
        g.drawChars(text.toCharArray(), 0, text.length(),
                xText, yDisplay + g.getFont().getSize());
    }

    /**
     * Receive a mouse button pressed. Use the right click to change the
     * direction.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public String toString() {
        return model.toString();
    }
}
