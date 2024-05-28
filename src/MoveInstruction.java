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

    int debug_count = 0;

    private Compass compass;

    private int xCompass, yCompass;
    private int compassSize;
    private double textRelativeX = 0.01;

    // Between 0.0 (the direction is a point) and 1.0 (the direction takes the whole height of the instruction)
    private double directionRelativeSize = 0.9;

    public MoveInstruction() {
        super();

        color = Color.BLUE.brighter();
        if (compass == null) {
            System.out.println("MoveInstr: compass was null");
            compass = new Compass();
        } else {
            System.out.println("MoveInstr: compass was NOT null");
        }

        model = new MoveInstructionModel((CompassModel) (compass.getModel()));
    }

    public MoveInstruction(CardinalPoint newCardPoint) {
        this();
        ((MoveInstructionModel) model).setDirection(newCardPoint);
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

        int xDisplay = (int) x0 + this.x;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        String text = "Move";
        g.setColor(Color.orange);
        setFont(g);
        int xText = (int) (xDisplay + textRelativeX * this.width * zoom);
        g.drawChars(text.toCharArray(), 0, text.length(),
                xText, yDisplay + g.getFont().getSize());

        // Paint the direction on the right-hand side of the instruction
        xCompass = (int) (xDisplay + (width - 0.5 * (height + height * directionRelativeSize)) * zoom);

        yCompass = (int) (yDisplay + 0.5 * (height - height * directionRelativeSize) * zoom);

        compass.setPos(xCompass, yCompass);
        compassSize = (int) (height * zoom * directionRelativeSize);
        compass.setSize(compassSize, compassSize);

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

        int xClick = e.getX();
        int yClick = e.getY();
        // Switch direction with right click
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (xClick >= xCompass) {
                compass.mousePressed(e);
            }
        }
    }

    public String toString() {
        String result = model.getName() + " ";
        // Append all the directions specified by the compass.
        result += compass.toString();
        return result;
    }
}
