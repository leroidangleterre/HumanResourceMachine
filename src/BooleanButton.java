import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthurmanoha
 */
class BooleanButton extends MyDefaultComponent {

    private Color color = Color.gray;

    // Position in pixels
    protected int x, y;
    // Dimensions in pixels
    protected int width, height;

    public BooleanButton() {

        model = new BooleanButtonModel();

        zoom = 1;
        width = 30;
        height = 30;
    }

    public BooleanButton(BooleanConstant currentBoolean) {
        this();
        ((BooleanButtonModel) model).setValue(currentBoolean);
    }

    public BooleanConstant getValue() {
        return ((BooleanButtonModel) model).getValue();
    }

    public void setValue(String newValue) {
        ((BooleanButtonModel) model).setValue(newValue);
    }

    @Override
    public void receiveCommand(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    @Override
    public void setSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
    }

    /**
     * Paint the button
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

        int xDisplay = this.x;
        int yDisplay = this.y;

        g.fillRect(xDisplay, yDisplay, (int) (width), (int) (height));

        g.setColor(Color.black);

        String text = ((BooleanButtonModel) model).getText();
        g.drawChars(text.toCharArray(), 0, text.length(),
                xDisplay + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                yDisplay + g.getFont().getSize());
    }

    public void toggle() {
        ((BooleanButtonModel) model).toggle();
        repaint();
    }

    @Override
    public String toString() {
        return ((BooleanButtonModel) model).getValue() + "";
    }
}
