package humanresourcemachine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class MoveInstructionModel extends InstructionModel {

    protected CardinalPoint currentDirection;

    public MoveInstructionModel() {
        super();
        currentDirection = CardinalPoint.NORTH;
    }

    public MoveInstructionModel(CardinalPoint newCardPoint) {
        this();
        currentDirection = newCardPoint;
    }

    public CardinalPoint getCardinalPoint() {
        return this.currentDirection;
    }

    public void toggleDirection() {

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
            break;
        }
    }

    public void setDirection(CardinalPoint newCardPoint) {
        this.currentDirection = newCardPoint;
    }

    /**
     * This method must be redefined by any subclass.
     *
     * @return the name of the instruction
     */
    public String getName() {
        return "WorkerMove";
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, this.getOptions());
        return n;
    }

    public String getOptions() {
        String option = "";
        switch (this.currentDirection) {
        case CENTER:
        case NORTH:
        case SOUTH:
        case EAST:
        case WEST:
            option = this.currentDirection.toString();
            break;
        default:
            option = "_";
            break;
        }
        return option;
    }

    @Override
    public String toString() {
        return getName() + " " + currentDirection;
    }
}
