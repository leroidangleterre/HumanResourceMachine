import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * This instruction is a conditional branch. It may or may not change a Worker's
 * next set of instruction.
 *
 * @author arthurmanoha
 */
public class IfInstruction extends Instruction {

    // The element we are testing: current block, or neighbor square/worker/block...
    private Compass compass;

    private double textRelativeX = 0.01;
    // Apparent relative size of the compass
    private double compassRelativeSize = 0.9;
    private double compassRelativeX = 0.15;
    private double boolRelativeSize = 0.9;
    private double boolRelativeX = 0.37;
    private double choiceBoxRelativeX = 0.6;
    private double choiceBoxRelativeWidth = 0.35;
    private double choiceBoxRelativeHeight = 0.9;

    // Apparent coordinates of the inner elements (computed at each repaint).
    private int xCompass;
    private int yCompass;
    private int compassApparentSize;
    private int xBool;
    private int yBool;
    private int boolApparentWidth;
    private int xChoiceBox;
    private int yChoiceBox;
    private int choiceBoxApparentWidth;
    private int choiceBoxApparentHeight;

    // The boolean operation: equals, greater than, ...
    private BooleanButton boolButton;
    // The expected value: a number, or a type such as Worker, Empty, Wall...
    // TODO: replace that type with some sort of a Comparable interface.
    private MyChoiceBox choiceBox;

    private Instruction elseInstruction;
    private Instruction endInstruction;

    private int indentationWidth;

    public IfInstruction() {
        super();

        model = new IfInstructionModel();
        model.setText("If");
        elseInstruction = null;
        endInstruction = null;
        color = new Color(255, 140, 0);
        indentationWidth = 0;

        ((IfInstructionModel) model).setDirection(CardinalPoint.WEST);
        compass = new Compass();
        compass.setDirection(((IfInstructionModel) model).getCardinalPoint());

        boolButton = new BooleanButton(((IfInstructionModel) model).getCurrentBoolean());
        choiceBox = new MyChoiceBox(
                ((IfInstructionModel) model).getChoiceValue(),
                model);
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

    public void setIndentationWidth(int newWidth) {
        indentationWidth = newWidth;
        repaint();
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

        int xDisplay = (int) x0 + this.x;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        // Draw a vertical line between this IfInstruction and the two NoInstructions (ELSE and END).
        if (endInstruction != null) {
            g.setColor(this.color);
            int yEnd = this.endInstruction.getY();
            int yEndDisplay = (int) (panelHeight - (y0 + yEnd));
            g.fillRect(xDisplay, yDisplay, (int) (indentationWidth * zoom), (yEndDisplay - yDisplay));
            g.setColor(Color.blue);
            g.drawRect(xDisplay, yDisplay, (int) (indentationWidth * zoom), (yEndDisplay - yDisplay));
        }

        super.paint(g, panelHeight, x0, y0, zoom);

        g.setColor(Color.BLACK);
        String text = "If ";

        int xText = (int) (xDisplay + textRelativeX * this.width * zoom);
        setFont(g);

        g.drawChars(text.toCharArray(), 0, text.length(), xText, yDisplay + g.getFont().getSize());

        // Paint the compass
        compassApparentSize = (int) (compassRelativeSize * this.height * zoom);
        xCompass = (int) (xDisplay + compassRelativeX * this.width * zoom);
        yCompass = (int) (yDisplay + 0.5 * height * (1 - compassRelativeSize) * zoom);
        compass.setPos(xCompass, yCompass);
        compass.setSize(compassApparentSize, compassApparentSize);
        compass.paint(g, panelHeight, x0, y0, zoom);

        // Paint the boolean button
        boolApparentWidth = (int) (boolRelativeSize * this.height * zoom);
        xBool = (int) (xDisplay + boolRelativeX * this.width * zoom);
        yBool = (int) (yDisplay + 0.5 * height * (1 - boolRelativeSize) * zoom);
        boolButton.setPos(xBool, yBool);
        boolButton.setSize(boolApparentWidth, boolApparentWidth);
        boolButton.paint(g, panelHeight, x0, y0, zoom);

        // Paint the choiceBox
        choiceBoxApparentWidth = (int) (choiceBoxRelativeWidth * this.width * zoom);
        choiceBoxApparentHeight = (int) (choiceBoxRelativeHeight * this.height * zoom);
        xChoiceBox = (int) (xDisplay + choiceBoxRelativeX * this.width * zoom);
        yChoiceBox = (int) (yDisplay + 0.5 * height * (1 - choiceBoxRelativeHeight) * zoom);
        choiceBox.setPos(xChoiceBox, yChoiceBox);
        choiceBox.setSize(choiceBoxApparentWidth, choiceBoxApparentHeight);
        choiceBox.paint(g, panelHeight, x0, y0, zoom);

    }

    /**
     * Receive a mouse button pressed. Use the right click to change the
     * direction, the boolean used, or the expected value.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        // The instruction is positioned at (x, y) and is dimensioned (width, height).
        xClick = e.getX();
        yClick = e.getY();

        System.out.println("bool: " + xBool + ", " + (xBool + boolApparentWidth));

        // Test for a click on the compass
        if (xClick >= xCompass && xClick <= xCompass + compassApparentSize) {
            compass.toggle();
            ((IfInstructionModel) model).setDirection(compass.getCurrentDirection());
            repaint();
        } else if (xClick >= xBool && xClick <= xBool + boolApparentWidth) {
            // Act on the boolean
            System.out.println("click BOOLEAN");
            boolButton.toggle();
            ((IfInstructionModel) model).setCurrentBoolean(boolButton.getValue());
            repaint();
        } else if (xClick >= xChoiceBox && xClick <= xChoiceBox + choiceBoxApparentWidth) {
            // Act on the choice box.
            System.out.println("click choice box");
            choiceBox.toggle();
            // TODO set the choice value of the model.
            ((IfInstructionModel) model).setChoiceValue(choiceBox.getValue());
            repaint();
        }
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
     * Set an ELSE instruction, located at a given address.
     *
     * @param newTarget
     * @param address
     */
    public void setElseInstruction(Instruction newTarget, int address) {
        if (newTarget != null) {
            elseInstruction = newTarget;
            ((IfInstructionModel) model).setElseAddress(address + 1);
            elseInstruction.color = this.color;
        }
    }

    public Instruction getElseInstruction() {
        return elseInstruction;
    }

    public void setEndInstruction(Instruction newTarget, int address) {
        if (newTarget != null) {
            endInstruction = newTarget;
            ((IfInstructionModel) model).setEndAddress(address + 1);
            endInstruction.color = this.color;
        }
    }

    public Instruction getEndInstruction() {
        return endInstruction;
    }
}
