import java.awt.Color;
import java.awt.Graphics;

/**
 * This instruction is used to make a Worker move one step horizontally or
 * vertically in the Terrain and push any block in its way.
 * A worker is capable to push only a certain amount of consecutive blocks at
 * once.
 *
 * @author arthurmanoha
 */
public class PushInstruction extends MoveInstruction {

    private Compass compass;
    // Between 0.0 (the direction is a point) and 1.0 (the direction takes the whole height of the instruction)
    private double directionRelativeSize = 0.9;

    public PushInstruction() {
        super();

        model = new PushInstructionModel();

        color = Color.BLUE.brighter();
        updateCompass();
    }

    public PushInstruction(CardinalPoint newCardPoint) {
        this();
        ((MoveInstructionModel) model).setDirection(newCardPoint);
        updateCompass();
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        super.paint(g, panelHeight, x0, y0, zoom);
        super.paintName(g, panelHeight, x0, y0, zoom);
    }
}
