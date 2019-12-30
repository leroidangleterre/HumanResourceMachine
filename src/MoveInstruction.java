import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * This instruction is used to make a Worker move one step horizontally or
 * vertically in the Terrain.
 *
 * @author arthurmanoha
 */
public class MoveInstruction extends Instruction {

    private final Direction direction;
    // Between 0.0 (the direction is a point) and 1.0 (the direction takes the whole height of the instruction)
    private double directionRelativeSize = 0.9;

    public MoveInstruction() {
        super();
        color = Color.BLUE.brighter();

        direction = new Direction();

        int directionSize = (int) (directionRelativeSize * this.height);
        direction.setSize(directionSize, directionSize);
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
        int xDisplay = (int) x0 + margin;
        int yDisplay = (int) (panelHeight - (y0 + this.y)) + margin;

        // Paint the direction on the right-hand side of the instruction
        int xDir = (int) (xDisplay + (width - 0.5 * (height + height * directionRelativeSize)) * zoom);

        int yDir = (int) (yDisplay + 0.5 * (height - height * directionRelativeSize) * zoom);

        direction.setPos(xDir, yDir);
        direction.setSize((int) (height * zoom * directionRelativeSize), (int) (height * zoom * directionRelativeSize));

        direction.paint(g, panelHeight, x0, y0, zoom);
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

        // Switch direction with right click
        if (e.getButton() == MouseEvent.BUTTON3) {
            toggleDirection();
        }
    }

    private void toggleDirection() {
        direction.toggle();
        repaint();
    }

    /**
     * Receive a command from another component.
     *
     * @param s
     */
    @Override
    public void receiveCommand(String s) {
        if (s.equals("RECEIVE_RIGHT_CLICK")) {
            direction.toggle();
        }
    }
}
