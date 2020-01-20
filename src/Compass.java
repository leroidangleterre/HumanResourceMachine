import java.awt.Color;
import java.awt.Graphics;

/**
 * This component is a button with an arrow oriented in one of the four possible
 * directions: N, S, E or W.
 *
 * @author arthurmanoha
 */
public class Compass {

    private static int NB_DIR_CREATED = 0;
    private int serial;

    private Color color;

    // Dimensions in pixels
    private int width, height;

    // Position in pixels
    private int x, y;

    private CardinalPoint currentDirection;

    public Compass() {
        serial = NB_DIR_CREATED;
        NB_DIR_CREATED++;
        currentDirection = CardinalPoint.NORTH;
        setRandomDirection();

        color = Color.orange;
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void setSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
    }

    // The dimensions must have already been set.
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        g.setColor(this.color);

        int xDisplay = this.x;
        int yDisplay = this.y;

        g.fillRect(xDisplay, yDisplay, (int) (width), (int) (height));
        if (currentDirection != null) {
            int arrowSize = width / 2;
            paintArrow(g, xDisplay + width / 2, yDisplay + width / 2, arrowSize, currentDirection);
        }
    }

    /**
     * Paint an arrow in a specific cardinal point. the arrow is defined as a
     * triangle.
     *
     * @param g
     * @param xCenter
     * @param yCenter
     * @param size
     * @param cardinal
     */
    private void paintArrow(Graphics g, int xCenter, int yCenter, int size, CardinalPoint cardinal) {
        int xTop, xLeft, xRight;
        int yTop, yLeft, yRight;
        double angle;
        switch (cardinal) {
            case EAST:
                angle = 0;
                break;
            case NORTH:
                angle = -Math.PI / 2;
                break;
            case WEST:
                angle = Math.PI;
                break;
            case SOUTH:
                angle = Math.PI / 2;
                break;
            default:
                angle = 0;
        }
        g.setColor(Color.red);
        if (cardinal == CardinalPoint.CENTER) {
            g.fillOval(xCenter - size / 4, yCenter - size / 4, size / 2, size / 2);
        } else {
            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);
            xTop = (int) (size * cosA);
            yTop = (int) (size * sinA);
            xLeft = (int) (size * cosA / 2 - size * sinA / 4);
            yLeft = (int) (size * sinA / 2 - size * cosA / 4);
            xRight = (int) (size * cosA / 2 + size * sinA / 4);
            yRight = (int) (size * sinA / 2 + size * cosA / 4);

            int xPoints[] = {xTop + xCenter, xLeft + xCenter, xRight + xCenter};
            int yPoints[] = {yTop + yCenter, yLeft + yCenter, yRight + yCenter};

            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    public void setDirection(CardinalPoint newDirection) {
        this.currentDirection = newDirection;
    }

    public CardinalPoint getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Take the next possible value, in the order N -> E -> S -> W -> C
     *
     */
    public void toggle() {
        switch (currentDirection) {
            case NORTH:
                currentDirection = CardinalPoint.EAST;
                break;
            case EAST:
                currentDirection = CardinalPoint.SOUTH;
                break;
            case SOUTH:
                currentDirection = CardinalPoint.WEST;
                break;
            case WEST:
                currentDirection = CardinalPoint.CENTER;
                break;
            case CENTER:
                currentDirection = CardinalPoint.NORTH;
                break;
            default:
            // No change
        }
    }

    /**
     * Choose a direction at random.
     *
     */
    private void setRandomDirection() {
        int rand = (int) (Math.random() * 4);
        for (int i = 0; i < rand; i++) {
            toggle();
        }
    }

    public int getSerial() {
        return serial;
    }
}
