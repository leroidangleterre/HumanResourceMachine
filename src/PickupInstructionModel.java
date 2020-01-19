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

    /**
     * Apply a specific treatment to the worker. That includes updating the
     * worker's current address (i.e. next instruction)
     *
     */
    @Override
    public void execute(int date, Worker w) {

        w.pickup(this.getCardinalPoint());

        w.setCurrentAddress(w.getCurrentAddress() + 1); // go to the next instruction
        super.execute(date, w);
    }
}
