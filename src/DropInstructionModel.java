/**
 *
 * @author arthurmanoha
 */
public class DropInstructionModel extends InstructionModel {

    public DropInstructionModel() {
        super();
    }

    /**
     * Make the worker drip their data block.
     *
     */
    @Override
    public void execute(int date, Worker w) {

        w.drop();

        w.setCurrentAddress(w.getCurrentAddress() + 1); // go to the next instruction
        super.execute(date, w);
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
