import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthurmanoha
 */
public class DataCube implements Comparable {

    /**
     * The worker's current position (its center).
     */
    private double x, y;
    private double size = 0.5;

    private int value;

    private static int NB_CUBES = 0;
    private int serial;

    private int arcWidth = 10;
    private int arcHeight = 10;

    public DataCube(double xParam, double yParam, int newValue) {
        x = xParam;
        y = yParam;
        serial = NB_CUBES;
        NB_CUBES++;
        value = newValue;
    }

    public DataCube(double xParam, double yParam) {
        this(xParam, yParam, 0);
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        int xDisplay = (int) (zoom * (this.x - this.size / 2) + x0);
        int yDisplay = (int) (panelHeight - (zoom * (this.y + this.size / 2) + y0));
        int apparentDiameter = (int) (size * zoom);

        g.setColor(new Color(204, 153, 0));
        g.fillRoundRect(xDisplay, yDisplay, apparentDiameter, apparentDiameter, arcWidth, arcHeight);

        String text = "" + value;
        g.setColor(Color.black);
        g.drawString(text, xDisplay + apparentDiameter / 2, yDisplay + apparentDiameter / 2);
    }

    public int getValue() {
        return value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double newX, double newY) {
        x = newX;
        y = newY;
    }

    @Override
    public boolean isLessThan(Comparable other) {
        if (other instanceof DataCube) {
            return this.value <= ((DataCube) other).value;
        } else if (other instanceof IntegerValue) {
            return this.value <= ((IntegerValue) other).getValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean isEqualTo(Comparable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMoreThan(Comparable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDifferentThan(Comparable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
