import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * This instruction is used to make a Worker move one step horizontally or
 * vertically in the Terrain.
 *
 * @author arthurmanoha
 */
public class PickupInstruction extends Instruction {

    private final Compass compass;

    private int xCompass, yCompass;
    private int compassSize;

    // Between 0.0 (the direction is a point) and 1.0 (the direction takes the whole height of the instruction)
    private double compassRelativeSize = 0.9;
    private double textRelativeX = 0.01;

    public PickupInstruction() {
        super();

        color = Color.gray;

        compass = new Compass();

        int directionSize = (int) (compassRelativeSize * this.height);
        compass.setSize(directionSize, directionSize);

        model = new PickupInstructionModel((CompassModel) (compass.getModel()));
    }

    public PickupInstruction(CardinalPoint newCardPoint) {
        this();
        ((PickupInstructionModel) model).setDirection(newCardPoint);
        this.setDirection(newCardPoint);
    }

    /**
     * Create a PickupInstruction with several cardinal points.
     *
     * @param parameters one or many CardinalPoints
     */
    public PickupInstruction(String parameters) {
        this();
        ((PickupInstructionModel) model).setDirection(parameters);
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
        int yDisplay = (int) (panelHeight - (y0 + this.y)) + margin;

        int xDir = (int) (xDisplay + (width - 0.5 * (height + height * compassRelativeSize)) * zoom);

        int yDir = (int) (yDisplay + 0.5 * (height - height * compassRelativeSize) * zoom);

        String text = "Pickup";
        g.setColor(Color.orange);
        setFont(g);
        int xText = (int) (xDisplay + textRelativeX * this.width * zoom);
        g.drawChars(text.toCharArray(), 0, text.length(),
                xText, yDisplay + g.getFont().getSize());

        compass.setPos(xDir, yDir);
        compass.setSize((int) (height * zoom * compassRelativeSize), (int) (height * zoom * compassRelativeSize));

        compass.paint(g, panelHeight, x0, y0, zoom);
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
            if (xClick >= xCompass) {
                compass.mousePressed(e);
            }
        }
    }

    public final void setDirection(CardinalPoint newDirection) {
        ((PickupInstructionModel) model).setDirection(newDirection);
    }

    /**
     * Receive a command from another component.
     *
     * @param s
     */
    @Override
    public void receiveCommand(String s) {
        if (s.equals("RECEIVE_RIGHT_CLICK")) {
        }
    }

    /**
     * Return the instruction as a String with all the compass directions
     *
     * @return
     */
    @Override
    public String toString() {
        String result = model.getName() + " ";
        // Append all the directions specified by the compass.
        result += compass.toString();
        return result;
    }
}
