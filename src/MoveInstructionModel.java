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

    private CardinalPoint currentDirection;

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
        System.out.println("Movecreate notification");
        Notification n = new Notification(this.getName(), null, this.getOptions());
        return n;
    }

    /**
     * Apply a specific treatment to the worker. That includes updating the
     * worker's current address (i.e. next instruction)
     *
     */
    @Override
    public void execute(int date, Worker w) {

        // Mark this worker as trying to move in that direction.
        //
        // This shall be done by the Terrain.
        //        w.moveInDirection(this.getCardinalPoint());
//        w.setCurrentAddress(w.getCurrentAddress() + 1); // go to the next instruction
        String directionString;
        switch (this.currentDirection) {
        case CENTER:
            directionString = "C";
            break;
        case NORTH:
            directionString = "N";
            break;
        case EAST:
            directionString = "E";
            break;
        case SOUTH:
            directionString = "S";
            break;
        case WEST:
            directionString = "W";
            break;
        default:
            directionString = "-";
            break;
        }
        Notification notif = new Notification("InstructionMove", w, directionString);
        notifyObservers(notif);

        super.execute(date, w);
    }

    public String getOptions() {
        String option = "";
        switch (this.currentDirection) {
        case CENTER:
            option = "C";
            break;
        case NORTH:
            option = "N";
            break;
        case SOUTH:
            option = "S";
            break;
        case EAST:
            option = "E";
            break;
        case WEST:
            option = "W";
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
