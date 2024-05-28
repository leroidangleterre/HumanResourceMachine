import java.util.ArrayList;
import java.util.Random;

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

    private CompassModel currentDirection;

    public MoveInstructionModel(CompassModel newCompassModel) {
        super();
        currentDirection = newCompassModel;
    }

    public CardinalPoint getCardinalPoint() {
        if (currentDirection.getCurrentDirections().size() > 0) {
            return currentDirection.getCurrentDirections().get(0);
        } else {
            return null;
        }
    }

//    public void toggleDirection() {
////        System.out.println("MoveInstructionModel.toggleDirection: TODO");
////        currentDirection.toggle(CardinalPoint.NORTH);
//    }
    public void setDirection(CardinalPoint newCardPoint) {
        currentDirection.setValue(newCardPoint);
        Notification n = createNotification();
        notifyObservers(n);
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
//        System.out.println("Movecreate notification");
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

        ArrayList<CardinalPoint> possibleDirections = currentDirection.getCurrentDirections();

        // Choose one direction among the available ones. If only one exists, choose that one.
        int index = new Random().nextInt(possibleDirections.size());
        directionString = possibleDirections.get(index).toString();

        Notification notif = new Notification("InstructionMove", w, directionString);
        notifyObservers(notif);

        super.execute(date, w);
    }

    public String getOptions() {
        String option = "";

        CardinalPoint chosenDirection = currentDirection.getValue();

        switch (chosenDirection) {
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
