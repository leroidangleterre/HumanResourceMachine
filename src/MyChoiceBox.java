import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthurmanoha
 */
public class MyChoiceBox extends MyDefaultComponent {

    private MyChoiceBoxModel model;

    private Color color = Color.green;

    // Position in pixels
    protected int x, y;
    // Dimensions in pixels
    protected int width, height;

    public MyChoiceBox(int val) {
        super();

        model = new MyChoiceBoxModel(0);

        zoom = 1;
        width = 30;
        height = 30;
    }

    public MyChoiceBox(int val, InstructionModel inst) {
        this(0);
        setInstructionModel(inst);
    }

    public MyChoiceBox(String text, InstructionModel inst) {
        this(0, inst);
        model.setValue(text);
    }

    private void setInstructionModel(InstructionModel newModel) {
        this.model.setInstructionModel(newModel);
    }

    @Override
    public void receiveCommand(String text) {
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void setSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
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
        g.setColor(color);

        int xDisplay = this.x;
        int yDisplay = this.y;

        g.fillRect(xDisplay, yDisplay, (int) (width), (int) (height));

        g.setColor(Color.orange);

        String text = (model).getValue();
        g.drawChars(text.toCharArray(), 0, text.length(),
                xDisplay + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                yDisplay + g.getFont().getSize());
    }

    public void toggle() {
        model.toggle();
        repaint();
    }

    public void setValue(String newVal) {
        this.model.setValue(newVal);
    }

    public String getValue() {
        return model.getValue();
    }
}
