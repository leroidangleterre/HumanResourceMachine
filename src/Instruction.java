import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


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

    private boolean isSelected;

    // Dimensions in pixels
    private int width, height;

    // Position in pixels
    private int x, y;

    private static int NB_CREATED = 0;
    private int serial;
    private Color color;

    /**
     * Create a new "instruction" component, which will be linked to its model.
     */
    public Instruction() {
        width = (int) (100 * (1 + Math.random()));
        height = 20;
        x = 0;
        y = 0;
        serial = NB_CREATED;
        NB_CREATED++;
        switch (serial - 5 * (serial / 5)) {
            case 0:
                color = Color.red;
                break;
            case 1:
                color = Color.blue;
                break;
            case 2:
                color = Color.orange;
                break;
            case 3:
                color = Color.green;
                break;
            case 4:
                color = Color.magenta;
                break;
            default:
                color = Color.gray;
        }

        // Model creation
        this.model = new InstructionModel();
    }

    public void setSelected(boolean newIsSelected) {
        isSelected = newIsSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    //* Specify at what position the component will be drawn.
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

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
     * @param zoom zoom factor
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        g.setColor(color);

        int xDisplay = (int) x0;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        g.fillRect(xDisplay, yDisplay, (int) (width * zoom), (int) (height * zoom));
    }

    /**
     * Tell if a given point lies inside a selected component.
     *
     * @param x
     * @param y
     * @return true when the point located at (x, y) is inside a selected
     * instruction
     */
    @Override
    public boolean pointIsInSelection(double x, double y) {

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

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public void receiveCommand(String s) {
    }
}
