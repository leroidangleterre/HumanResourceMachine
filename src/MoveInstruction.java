import java.awt.Color;
import java.awt.Graphics;

/**
 * This instruction is used to make a Worker move one step horizontally or
 * vertically in the Terrain.
 *
 * @author arthurmanoha
 */
public class MoveInstruction extends Instruction {

    private Direction direction;

    public MoveInstruction() {
        super();
        color = Color.BLUE.brighter();
        direction = new Direction();
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

        int margin = 3;
        int xDisplay = (int) x0 + margin;
        int yDisplay = (int) (panelHeight - (y0 + this.y)) + margin;

        // Paint the direction on the right-hand side of the instruction
    }

}
