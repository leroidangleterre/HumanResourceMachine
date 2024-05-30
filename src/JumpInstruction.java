import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * This instruction is an unconditional branch. It changes a Worker's next
 * instruction.
 *
 * @author arthurmanoha
 */
public class JumpInstruction extends Instruction {

    private Instruction target;

    public JumpInstruction() {
        super();

        model = new JumpInstructionModel();
        model.setText("Jump");
        target = null;
        color = new Color(102, 0, 153);
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

        int xDisplay = (int) x0;
        int yDisplay = (int) (panelHeight - (y0 + this.y));

        g.setColor(Color.orange);
        setFont(g);
        String text = model.getText();
        g.drawChars(text.toCharArray(), 0, text.length(),
                (int) (xDisplay + this.width * zoom - g.getFontMetrics().stringWidth(text)),
                yDisplay + g.getFont().getSize());

        paintLinkToTarget(g, panelHeight, x0, y0, zoom);
    }

    /**
     * Paint the curved arrow that leads to the target instruction
     *
     * @param g the Graphics we paint on
     * @param panelHeight the height of the drawing area
     * @param x0 x-offset for the origin of the drawing
     * @param y0 y-offset for the origin of the drawing
     * @param zoom zoom factor used to scale things INSIDE the Instruction
     */
    private void paintLinkToTarget(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        if (target == null) {

            int xDisplay = (int) (x0 + this.width * zoom);
            int yDisplay = (int) (panelHeight - (y0 + this.y - (this.height) * zoom / 2));
            int length = (int) (1.3 * this.width * zoom);
            g.setColor(Color.red);
            int linkWidth = 10;
            g.fillRect(xDisplay, yDisplay - linkWidth / 2, length, linkWidth);
        } else {
            int xDisplay = (int) (x0 + this.width * zoom);
            int yDisplayStart = (int) (panelHeight - (y0 + this.y - (this.height) * zoom / 2));

            int yDisplayEnd = (int) (panelHeight - (y0 + target.y - (target.height) * zoom / 2));

            double radius = Math.abs(yDisplayStart - yDisplayEnd) / 2;
            int yCenter = (yDisplayStart + yDisplayEnd) / 2;

            // Trunk of the arrow, without the tip.
            int nbSegments = 13;
            int nbPoints = 2 * nbSegments + 2;
            double angle = Math.PI / (2 * (nbSegments - 1));
            int tipWidth = (int) (height * zoom / 4);
            int[] xTab = new int[nbPoints];
            int[] yTab = new int[nbPoints];

            double signum = Math.signum(yDisplayStart - yDisplayEnd);

            // Values for the body of the arrow
            for (int i = 0; i < nbSegments + 1; i++) {
                xTab[i] = (int) (xDisplay + tipWidth + (radius + tipWidth / 2) * Math.sin((2 * i - 1) * angle));
                xTab[2 * nbSegments + 2 - i - 1] = (int) (xDisplay + tipWidth + (radius - tipWidth / 2) * Math.sin((2 * i - 1) * angle));

                yTab[i] = (int) (yCenter + signum * (radius + tipWidth / 2) * Math.cos((2 * i - 1) * angle));
                yTab[2 * nbSegments + 2 - i - 1] = (int) (yCenter + signum * (radius - tipWidth / 2) * Math.cos((2 * i - 1) * angle));
            }
            // Special values for the base of the arrow:
            xTab[0] = xDisplay;
            xTab[nbPoints - 1] = xDisplay;
            // Part of the arrow that is linked to the tip
            xTab[nbPoints / 2] = xDisplay + tipWidth;
            xTab[nbPoints / 2 - 1] = xDisplay + tipWidth;

            g.setColor(Color.red);
            g.fillPolygon(xTab, yTab, nbPoints);

            // Tip of the arrow: a triangle
            xTab = new int[3];
            yTab = new int[3];

            xTab[0] = xDisplay;
            yTab[0] = (int) (yCenter - signum * radius);
            xTab[1] = xDisplay + tipWidth;
            yTab[1] = (int) (yCenter - signum * radius + tipWidth);
            xTab[2] = xDisplay + tipWidth;
            yTab[2] = (int) (yCenter - signum * radius - tipWidth);

            g.fillPolygon(xTab, yTab, 3);
        }
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

    public void setTargetInstruction(Instruction newTarget, int address) {
        target = newTarget;
        ((JumpInstructionModel) model).setTargetAddress(address);
    }

    public Instruction getTargetInstruction() {
        return target;
    }

    public int getTargetAddress() {
        return ((JumpInstructionModel) model).getTargetAddress();
    }

    public void setText(String newText) {
        ((JumpInstructionModel) model).setText(newText);
    }

    public String getText() {
        return ((JumpInstructionModel) model).getText();
    }

    @Override
    public String toString() {
        String text = getText() + " " + ((JumpInstructionModel) model).targetAddress;
        return text;
    }
}
