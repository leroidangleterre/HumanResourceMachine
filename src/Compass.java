import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * This component is a button with an arrow oriented in one of the eight
 * possible directions: N, S, E, W, NE, SE, SW, NW.
 *
 * @author arthurmanoha
 */
public class Compass extends MyDefaultComponent implements Observer {

    private static int NB_DIR_CREATED = 0;
    private int serial;

    private Color color;

    // Dimensions in pixels
    private int width, height;

    // Position in pixels
    private int x, y;

    public Compass() {
        serial = NB_DIR_CREATED;
        NB_DIR_CREATED++;

        model = new CompassModel();
        ((CompassModel) model).addObserver(this);

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

        g.fillRect(x, y, (int) (width), (int) (height));

        g.setColor(Color.black);
        g.drawLine(x + width / 3, y, x + width / 3, y + height);
        g.drawLine(x + 2 * width / 3, y, x + 2 * width / 3, y + height);
        g.drawLine(x, y + height / 3, x + width, y + height / 3);
        g.drawLine(x, y + 2 * height / 3, x + width, y + 2 * height / 3);

        int arrowSize = width / 2;
        for (CardinalPoint currentDirection : getCurrentDirections()) {
            if (currentDirection != null) {
                paintArrow(g, x + width / 2, y + width / 2, arrowSize, currentDirection);
            }
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
        double xArrow = 0;
        double yArrow = 0;
        switch (cardinal) {
        case EAST:
            angle = 0 * Math.PI / 4;
            xArrow = xCenter + 2 * size / 3;
            yArrow = yCenter;
            break;
        case SOUTH_EAST:
            angle = 1 * Math.PI / 4;
            xArrow = xCenter + 2 * size / 3;
            yArrow = yCenter + 2 * size / 3;
            break;
        case SOUTH:
            angle = 2 * Math.PI / 4;
            xArrow = xCenter;
            yArrow = yCenter + 2 * size / 3;
            break;
        case SOUTH_WEST:
            angle = 3 * Math.PI / 4;
            xArrow = xCenter - 2 * size / 3;
            yArrow = yCenter + 2 * size / 3;
            break;
        case WEST:
            angle = 4 * Math.PI / 4;
            xArrow = xCenter - 2 * size / 3;
            yArrow = yCenter;
            break;
        case NORTH_WEST:
            angle = 5 * Math.PI / 4;
            xArrow = xCenter - 2 * size / 3;
            yArrow = yCenter - 2 * size / 3;
            break;
        case NORTH:
            angle = 6 * Math.PI / 4;
            xArrow = xCenter;
            yArrow = yCenter - 2 * size / 3;
            break;
        case NORTH_EAST:
            angle = 7 * Math.PI / 4;
            xArrow = xCenter + 2 * size / 3;
            yArrow = yCenter - 2 * size / 3;
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
            double arrowLength = size / 2;
            double arrowWidth = 0.3 * size;
            xTop = (int) (xArrow + 2 * arrowLength / 3 * cosA);
            yTop = (int) (yArrow + 2 * arrowLength / 3 * sinA);
            xLeft = (int) (xArrow - arrowLength / 3 * cosA - arrowWidth * sinA);
            yLeft = (int) (yArrow + arrowWidth * cosA + (-arrowLength / 3) * sinA);
            xRight = (int) (xArrow - arrowLength / 3 * cosA + arrowWidth * sinA);
            yRight = (int) (yArrow - arrowWidth * cosA + (-arrowLength / 3) * sinA);

            int xPoints[] = {xTop, xLeft, xRight};
            int yPoints[] = {yTop, yLeft, yRight};

            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    /**
     * Toggle a single cardinal point for this compass.
     *
     * @param newDirection if that direction was active, it will be turned off;
     * otherwise it will be activated.
     */
    private void toggleDirection(CardinalPoint newDirection) {
        ((CompassModel) model).toggle(newDirection);
        repaint();
    }

    public void set(CardinalPoint newDirection) {
        ((CompassModel) model).setValue(newDirection);
        repaint();
    }

    /**
     * Get the single direction of this compass.
     *
     * @return a single CardinalPoint if the compass has only one direction
     * activated, null otherwise.
     */
    public CardinalPoint getCurrentDirection() {
        return ((CompassModel) model).getValue();
    }

    /**
     * Get all the active directions.
     *
     * @return the list of active directions
     */
    public ArrayList<CardinalPoint> getCurrentDirections() {
        return ((CompassModel) model).getCurrentDirections();
    }

    @Override
    public String toString() {

        String result = "";
        for (CardinalPoint s : ((CompassModel) model).getCurrentDirections()) {
            result += s + " ";
        }
        return result;
    }

    public int getSerial() {
        return serial;
    }

    public int getWidth(double zoom) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveCommand(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // line=0:north, line=1:center, line=2:south
        // col=0:west, col=1:center, col=2:east
        int line, col;
        line = 3 * (e.getY() - this.y) / height;
        col = 3 * (e.getX() - this.x) / width;

        switch (line) {
        case 0:
            switch (col) {
            case 0:
                toggleDirection(CardinalPoint.NORTH_WEST);
                break;
            case 1:
                toggleDirection(CardinalPoint.NORTH);
                break;
            case 2:
                toggleDirection(CardinalPoint.NORTH_EAST);
                break;
            default:
                break;
            }
            break;
        case 1:

            switch (col) {
            case 0:
                toggleDirection(CardinalPoint.WEST);
                break;
            case 1:
                toggleDirection(CardinalPoint.CENTER);
                break;
            case 2:
                toggleDirection(CardinalPoint.EAST);
                break;
            default:
                break;
            }
            break;

        case 2:
            switch (col) {
            case 0:
                toggleDirection(CardinalPoint.SOUTH_WEST);
                break;
            case 1:
                toggleDirection(CardinalPoint.SOUTH);
                break;
            case 2:
                toggleDirection(CardinalPoint.SOUTH_EAST);
                break;
            default:
                break;
            }
            break;
        }

        repaint();
    }

    // As an Observer, we receive info from Observable objects;
    @Override
    public void update(Notification n) {
        switch (n.getName()) {
        case "CompassChanged":
            repaint();
            break;
        default:
            break;
        }
    }
}
