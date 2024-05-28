import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author arthurmanoha
 */
public class MyChoiceBox extends MyDefaultComponent {

    // Background color of the box
    private Color color = Color.green;
    private Color textColor = Color.gray;

    // Plus/Minus buttons when the box represents a number
    private Color buttonsColor = Color.gray;
    private Color buttonsBorderColor = Color.yellow;
    private Color buttonsTextColor = Color.black;

    // Position in pixels
    protected int x, y;
    // Dimensions in pixels
    protected int width, height;

    // Position of the buttons when they exist
    private int xDisplayButtons;

    // Compass (when it is visible)
    private Compass compass;

    public MyChoiceBox(int val) {
        super();

        model = new MyChoiceBoxModel(0);

        zoom = 1;
        width = 30;
        height = 30;
        compass = new Compass();
        ((MyChoiceBoxModel) model).setCompass(compass);
    }

    public MyChoiceBox(int val, InstructionModel inst) {
        this(0);
    }

    public MyChoiceBox(String text, InstructionModel inst) {
        this(0, inst);
        ((MyChoiceBoxModel) model).setValue(text);
    }

    @Override
    public void receiveCommand(String text) {
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /**
     * Set the size of the choicebox.
     * If a parameter is negative, the corresponding attribute is not modified.
     *
     * @param newWidth the new width. If strictly less than 0, component width
     * is not modified.
     * @param newHeight the new height. If strictly less than 0, component
     * height is not modified.
     */
    @Override
    public void setSize(int newWidth, int newHeight) {
        if (newWidth > 0) {
            width = newWidth;
        }
        if (newHeight > 0) {
            height = newHeight;
        }
    }

    /**
     * Paint the choice box
     *
     * @param g the Graphics we paint on
     * @param panelHeight the height of the drawing area
     * @param x0 x-offset for the origin of the drawing
     * @param y0 y-offset for the origin of the drawing
     * @param zoom zoom factor used to scale things INSIDE the Instruction
     * component.
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {

        int xDisplay = this.x;
        int yDisplay = this.y;

        // Main part of the box
        g.setColor(color);
        g.fillRect(xDisplay, yDisplay, (int) (width), (int) (height));

        if (isCompass()) {
            compass.setSize(height, height);
            int xCompass = xDisplay;
            compass.setPos(xCompass, yDisplay);
            compass.paint(g, panelHeight, x0, y0, zoom);
        } else {

            g.fillRect(xDisplay, yDisplay, (int) (width), (int) (height));

            // Text (which may be the text representation of a number)
            g.setColor(textColor);

            String text;
            int leftShift; // Value used to center the texts.
            // Plus/Minus buttons if the value is a number
            if (isNumber()) {

                // Display buttons
                xDisplayButtons = xDisplay + width - height;
                g.setColor(buttonsColor);
                g.fillRect(xDisplayButtons, yDisplay, height, height);

                // Buttons borders
                g.setColor(buttonsBorderColor);
                g.drawRect(xDisplayButtons, yDisplay,
                        height, height / 2);
                g.drawRect(xDisplayButtons, yDisplay + height / 2,
                        height, height / 2);

                // Buttons text
                g.setColor(buttonsTextColor);
                text = "+";
                leftShift = g.getFontMetrics().stringWidth(text) / 2;
                g.drawChars(text.toCharArray(), 0, text.length(), xDisplayButtons + height / 2 - leftShift, yDisplay + height / 2);
                text = "-";
                leftShift = g.getFontMetrics().stringWidth(text) / 2;
                g.drawChars(text.toCharArray(), 0, text.length(), xDisplayButtons + height / 2 - leftShift, yDisplay + height);
            }
            text = ((MyChoiceBoxModel) model).getValue();
            leftShift = g.getFontMetrics().stringWidth(text) / 2;
            // For numbers (with some space used for the buttons), the text can only be displayed in the first part of the box.
            if (isNumber()) {
                leftShift = 3 * leftShift;
            }

            g.setColor(textColor);
            g.drawChars(text.toCharArray(), 0, text.length(),
                    xDisplay + width / 2 - leftShift,
                    yDisplay + g.getFont().getSize());
        }
    }

    public void toggle() {
        ((MyChoiceBoxModel) model).toggle();
        repaint();
    }

    public void setValue(String newVal) {
        ((MyChoiceBoxModel) model).setValue(newVal);
    }

    public String getValue() {
        return ((MyChoiceBoxModel) model).getValue();
    }

    public boolean isNumber() {
        return ((MyChoiceBoxModel) model).isNumber();
    }

    public boolean isCompass() {
        return ((MyChoiceBoxModel) model).isCompass();
    }

    /**
     * Action performed when a left click is received.
     *
     * @param e The event received by the panel
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Get the values for xClick, yClick, leftClickIsActive, wheelClickIsActive
        super.mousePressed(e);

        xClick = e.getX();
        yClick = e.getY();

        if (isNumber()) {
            // Special check for the buttons
            if (xClick > xDisplayButtons) {
                if (yClick > this.y + height / 2) {
                    // Lower button
                    ((MyChoiceBoxModel) model).decreaseValue();
                } else {
                    // Top button
                    ((MyChoiceBoxModel) model).increaseValue();
                }
            } else {
                toggle();
            }
        } else if (isCompass()) {
            if (xClick > this.x + this.height) {
                // clicked outside the compass, to its right; go to the next value
                toggle();
            } else {
                // clicked inside the compass: change the compass orientation
                compass.mousePressed(e);
            }
        } else {
            // Switch to the next possible value.
            toggle();
        }
    }

}
