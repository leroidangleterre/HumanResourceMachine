package humanresourcemachine;

/**
 *
 * @author arthurmanoha
 */
public class DropInstructionModel extends InstructionModel {

    public DropInstructionModel() {
        super();
    }

    /**
     * This method must be redefined by any subclass.
     *
     * @return the name of the instruction
     */
    @Override
    public String getName() {
        return "WorkerDrop";
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, "");
        return n;
    }
}
