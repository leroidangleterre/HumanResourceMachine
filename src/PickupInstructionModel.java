/**
 *
 * @author arthurmanoha
 */
public class PickupInstructionModel extends DirectionalInstructionModel {

    public PickupInstructionModel(CompassModel newCompassModel) {
        super(newCompassModel);
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

    @Override
    public String getName() {
        return "WorkerPickup";
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, this.getOptions());
        return n;
    }

    @Override
    public String toString() {
        return getName() + " " + currentDirection;
    }
}
