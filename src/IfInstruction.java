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

    private Instruction elseInstruction;
    private Instruction endInstruction;

    private int indentationWidth;

    public IfInstruction() {
        super();

        model = new IfInstructionModel();
        elseInstruction = null;
        endInstruction = null;
        color = new Color(255, 140, 0);
        indentationWidth = 0;
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
        String text = "If " + ((IfInstructionModel) this.model).getElseAddress() + " " + ((IfInstructionModel) this.model).getEndAddress();
        g.drawChars(text.toCharArray(), 0, text.length(), xDisplay + width - g.getFontMetrics().stringWidth(text), yDisplay + g.getFont().getSize());
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

    public void setElseInstruction(Instruction newTarget, int address) {
        elseInstruction = newTarget;
        ((IfInstructionModel) model).setElseAddress(address);
        elseInstruction.color = this.color;
    }

    public Instruction getElseInstruction() {
        return elseInstruction;
    }

    public void setEndInstruction(Instruction newTarget, int address) {
        endInstruction = newTarget;
        ((IfInstructionModel) model).setEndAddress(address);
        endInstruction.color = this.color;
    }

    public Instruction getEndInstruction() {
        return endInstruction;
    }
}
