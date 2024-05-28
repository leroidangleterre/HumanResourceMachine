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

    double marginPercentage = 0.05; // As a percentage of the apparent height: 0.05 means 5%

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

    // The boolean operation: equals, greater than, ...
    private BooleanButton boolButton;
    // The expected value: a number, or a type such as Worker, Empty, Wall...
    // TODO: replace that type with some sort of a Comparable interface.
    private MyChoiceBox choiceBox;

    private ElseInstruction elseInstruction;
    private Instruction endInstruction;

    private int indentationWidth;

    public IfInstruction() {
        super();

        model = new IfInstructionModel();
        elseInstruction = null;
        endInstruction = null;
        color = new Color(255, 140, 0);
        indentationWidth = 0;

        ((IfInstructionModel) model).setDirection(CardinalPoint.WEST);
        compass = new Compass();
        compass.set(((IfInstructionModel) model).getCardinalPoint());

        boolButton = new BooleanButton(((IfInstructionModel) model).getCurrentBoolean());

        choiceBox = new MyChoiceBox(0, model);

        ((IfInstructionModel) model).setChoiceBoxModel((MyChoiceBoxModel) choiceBox.getModel());
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

        setFont(g);

        int xDisplay = (int) x0 + this.x;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        // Draw a vertical line between this IfInstruction and the two NoInstructions (ELSE and END).
        if (endInstruction != null) {
            g.setColor(this.color);
        } else {
            System.out.println("Error If: the 'endInstruction' is null");
        }
        int yEnd = this.endInstruction.getY();
        int yEndDisplay = (int) (panelHeight - (y0 + yEnd));
        g.fillRect(xDisplay, yDisplay, (int) (indentationWidth * zoom), (yEndDisplay - yDisplay));
        g.setColor(Color.green);
        g.drawRect(xDisplay, yDisplay, (int) (indentationWidth * zoom), (yEndDisplay - yDisplay));

        // Compute all the coordinates of the sub-components
        // Text sub-component
        g.setColor(Color.BLACK);
        String text = ((IfInstructionModel) model).getText();
        int xText = (int) (xDisplay + textRelativeX * this.width * zoom);
        int textWidth = g.getFontMetrics().stringWidth(text);

        int margin = (int) (marginPercentage * height * zoom);

        // Apparent height of the Instruction
        int appHeight = (int) (height * zoom);
        int subComponentHeight = appHeight - 2 * margin;

        // Compass sub-component
        xCompass = (int) (xDisplay + margin + textWidth);
        yCompass = (int) (yDisplay + margin);
        compass.setPos(xCompass, yCompass);
        compassApparentSize = subComponentHeight; // Compass is displayed as a square.
        compass.setSize(compassApparentSize, subComponentHeight);

        // Boolean sub-component
        xBool = xDisplay + textWidth + 2 * margin + compassApparentSize;
        yBool = yCompass;
        boolApparentWidth = compassApparentSize;
        boolButton.setPos(xBool, yBool);
        boolButton.setSize(boolApparentWidth, subComponentHeight);

        // ChoiceBox sub-component
        xChoiceBox = xDisplay + textWidth + 3 * margin + compassApparentSize + boolApparentWidth;
        yChoiceBox = yCompass;
        int choiceBoxTextWidth = g.getFontMetrics().stringWidth(choiceBox.getValue());
        // Text must be written in a rectangle at least as wide as it is high; the buttons are painted after that.
        choiceBoxApparentWidth = (int) Math.max(subComponentHeight, choiceBoxTextWidth);
        if (choiceBox.isNumber()) {
            // Additional width for the buttons
            choiceBoxApparentWidth += subComponentHeight;
        }
        if (choiceBox.isCompass()) {
            // Specific width for when the choice box is a compass
            choiceBoxApparentWidth = (int) (subComponentHeight * 1.5);
        }
        choiceBox.setSize(choiceBoxApparentWidth, subComponentHeight);
        choiceBox.setPos(xChoiceBox, yChoiceBox);

        // The width of the current IfInstruction depends on the width of its subcomponents. The height is not changed here.
        this.width = (int) (textWidth / zoom + height + height + choiceBoxApparentWidth / zoom);
        super.paint(g, panelHeight, x0, y0, zoom);

        g.setColor(Color.black);
        g.drawChars(text.toCharArray(), 0, text.length(), xText, yDisplay + g.getFont().getSize());
        compass.paint(g, panelHeight, x0, y0, zoom);
        boolButton.paint(g, panelHeight, x0, y0, zoom);
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

        // Test for a click on the compass
        if (xClick >= xCompass && xClick <= xCompass + compassApparentSize) {
            compass.mousePressed(e);
            ((IfInstructionModel) model).setDirection(compass.getCurrentDirection());
            repaint();
        } else if (xClick >= xBool && xClick <= xBool + boolApparentWidth) {
            // Act on the boolean
            boolButton.toggle();
            ((IfInstructionModel) model).setCurrentBoolean(boolButton.getValue());
            repaint();
        } else if (xClick >= xChoiceBox && xClick <= xChoiceBox + choiceBoxApparentWidth) {
            // Act on the choice box.
            choiceBox.mousePressed(e);
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
    public void setElseInstruction(ElseInstruction newTarget, int address) {
        if (newTarget != null) {
            elseInstruction = newTarget;
            elseInstruction.color = this.color;
        }
        ((IfInstructionModel) model).setElseAddress(address);
        if (newTarget == null) {
        } else {
            if (newTarget.getModel() != null) {
                ((IfInstructionModel) model).setElseInstruction(newTarget.getModel());
            }
        }
        this.computeText();
    }

    public ElseInstruction getElseInstruction() {
        return elseInstruction;
    }

    public int getElseAddress() {
        return ((IfInstructionModel) model).getElseAddress();
    }

    public void setEndInstruction(Instruction newTarget, int address) {
        if (newTarget != null) {
            endInstruction = newTarget;
            endInstruction.color = this.color;
        }
        ((IfInstructionModel) model).setEndAddress(address);
        this.computeText();
    }

    public int getEndAddress() {
        return ((IfInstructionModel) model).getEndAddress();
    }

    public Instruction getEndInstruction() {
        return endInstruction;
    }

    private void computeText() {

        String newText = "IF goto next, else goto " + getElseAddress() + ", end goto " + getEndAddress();

        model.setText(newText);
    }

    void setChoiceBoxValue(String choiceBoxValue) {
        choiceBox.setValue(choiceBoxValue);
        ((IfInstructionModel) model).setChoiceValue(choiceBoxValue);
    }

    void setComparator(String newComparator) {
        boolButton.setValue(newComparator);
        ((IfInstructionModel) model).setCurrentBoolean(BooleanConstant.valueOf(newComparator));
    }

    void setCompass(String newCompassDirection) {
        compass.set(CardinalPoint.valueOf(newCompassDirection));
        ((IfInstructionModel) model).setDirection(CardinalPoint.valueOf(newCompassDirection));
    }
}
