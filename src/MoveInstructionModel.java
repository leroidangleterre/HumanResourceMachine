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
public class MoveInstructionModel extends DirectionalInstructionModel {

    public MoveInstructionModel(CompassModel newCompassModel) {
        super(newCompassModel);
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
}
