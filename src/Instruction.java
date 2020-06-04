import java.awt.Color;
import java.awt.Graphics;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class Instruction extends MyDefaultComponent {

    protected InstructionModel model;

    private boolean isSelected;

    // Dimensions in pixels
    protected int width, height;

    // Position in pixels
    protected int x, y;

//    protected String text;
    private static int NB_CREATED = 0;
    protected Color color;

    /**
     * Create a new "instruction" component, which will be linked to its model.
     */
    public Instruction() {
        super();

        zoom = 1;
        width = 150;
        height = 30;
        serialNumber = NB_CREATED;
        NB_CREATED++;
        color = Color.gray;

        // Model creation
        this.model = new InstructionModel();
//        text = "Instruction";
    }

    public void setSelected(boolean newIsSelected) {
        isSelected = newIsSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Specify at what position the component will be drawn.
     *
     * @param newX
     * @param newY
     */
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /**
     * Get the model associated with this component
     *
     * @return the model
     */
    @Override
    public InstructionModel getModel() {
        return (InstructionModel) model;
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
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        g.setColor(color);

        int xDisplay = (int) x0 + this.x;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        g.fillRect(xDisplay, yDisplay, (int) (width * zoom), (int) (height * zoom));

        // If the instruction is selected, we draw a few black rectangles around it.
        // TODO: replace this with a large-stroke rectangle.
        if (isSelected) {
            g.setColor(Color.BLACK);
            for (int offset = 0; offset < 5; offset++) {
                g.drawRect(xDisplay + offset, yDisplay + offset,
                        (int) (width * zoom) - 2 * offset, (int) (height * zoom) - 2 * offset);
            }
        }
    }

    /**
     * Tell if the instruction spans over the given y-coordinate, regardless of
     * the x position and width.
     *
     * @param yTest
     * @return
     */
    public boolean containsPoint(double yTest) {
        return yTest < y && (y - height * zoom) < yTest;
    }

    /**
     * Tell if the entire instruction fits between the two given y-coordinates.
     *
     * @param yMin
     * @param yMax
     * @return
     */
    public boolean isContainedBetweenY(double yMin, double yMax) {
        if (y < yMax) {
            if (y - height * zoom > yMin) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Set the horizontal position of the component. Instructions that contain a
     * sub-component, such as MoveInstruction, must offset that inner component.
     *
     * @param newX
     */
    protected void setX(int newX) {
        this.x = newX;
    }

    protected void setY(int newY) {
        this.y = newY;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void receiveCommand(String s) {
    }

    /**
     * Method used to write the instruction in the save file.
     *
     * @return a string representing the instruction.
     */
    @Override
    public String toString() {
        return model.toString();
    }
}
