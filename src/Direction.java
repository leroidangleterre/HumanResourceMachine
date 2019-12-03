/**
 * This component is a button with an arrow oriented in one of the four possible
 * directions: N, S, E or W.
 *
 * @author arthurmanoha
 */
public class Direction extends MyDefaultComponent {

    private enum CardinalPoint {

        NORTH, SOUTH, EAST, WEST
    };
    private CardinalPoint currentDirection;

    public Direction() {
        currentDirection = CardinalPoint.NORTH;
    }

    @Override
    public void receiveCommand(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
