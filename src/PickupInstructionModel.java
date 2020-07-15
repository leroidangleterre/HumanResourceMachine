/**
 *
 * @author arthurmanoha
 */
public class PickupInstructionModel extends InstructionModel {

    private CardinalPoint currentDirection;

    public PickupInstructionModel() {
        super();
        currentDirection = CardinalPoint.NORTH;
    }

    public CardinalPoint getCardinalPoint() {
        return this.currentDirection;
    }

    public void setDirection(CardinalPoint newDirection) {
        currentDirection = newDirection;
    }

    public void toggleDirection() {

        switch (currentDirection) {
        case NORTH:
            setDirection(CardinalPoint.EAST);
            break;
        case EAST:
            setDirection(CardinalPoint.SOUTH);
            break;
        case SOUTH:
            setDirection(CardinalPoint.WEST);
            break;
        case WEST:
            setDirection(CardinalPoint.CENTER);
            break;
        case CENTER:
            setDirection(CardinalPoint.NORTH);
            break;
        default:
            break;
        }
    }

    @Override
    public String getName() {
        return "WorkerPickup";
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, this.currentDirection.toString());
        return n;
    }

    @Override
    public String toString() {
        return getName() + " " + currentDirection;
    }
}
