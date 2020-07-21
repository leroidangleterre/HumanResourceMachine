package humanresourcemachine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author arthurmanoha
 */
public class DataCube extends MyDefaultComponent {

    /**
     * The worker's current position (its center).
     */
    private double x, y;
    private double size = 0.5;

    private double defaultZoomFactor = 0.015;

    private int value;

    private static int NB_CUBES = 0;
    private int serial;

    private int arcWidth = 10;
    private int arcHeight = 10;

    private boolean isCarried; // True when carried by a worker
    private double offsetWhenCarried = 0.4;
    private double offsetWhenNotCarried = -0.4;

    public DataCube(double xParam, double yParam, int newValue) {
        x = xParam;
        y = yParam;
        serial = NB_CUBES;
        NB_CUBES++;
        value = newValue;
        isCarried = false;

        textRelativeSize = 0.5;
        zoom = 1;
    }

    public DataCube(double xParam, double yParam) {
        this(xParam, yParam, NB_CUBES);
    }

    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        double offset = isCarried ? offsetWhenCarried : offsetWhenNotCarried;
        int xDisplay = (int) (zoom * (this.x - this.size / 2) + x0);
        int yDisplay = (int) (panelHeight - (zoom * (this.y + this.size * (0.5 + offset)) + y0));
        int apparentDiameter = (int) (size * zoom);

        g.setColor(new Color(204, 153, 0));
        g.fillRoundRect(xDisplay, yDisplay, apparentDiameter, apparentDiameter, arcWidth, arcHeight);

        g.setColor(Color.black);
        g.drawRoundRect(xDisplay, yDisplay, apparentDiameter, apparentDiameter, arcWidth, arcHeight);

        this.zoom = zoom * defaultZoomFactor;
        setFont(g);
        String text = "" + value;
        g.setColor(Color.blue);
        g.drawChars(text.toCharArray(), 0, text.length(),
                (int) (xDisplay + this.size * zoom - g.getFontMetrics().stringWidth(text)),
                yDisplay + g.getFont().getSize());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        this.value = newValue;
    }

    @Override
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public void setPosition(double newX, double newY) {
        x = newX;
        y = newY;
    }

    public void setCarried(boolean newIsCarried) {
        isCarried = newIsCarried;
    }

    @Override
    public void receiveCommand(String text) {
        System.out.println("Cube should not receive commands.");
    }

}
