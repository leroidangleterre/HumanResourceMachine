/**
 *
 * @author arthurmanoha
 */
public class NoInstructionModel extends InstructionModel {

    private String text;

    public NoInstructionModel() {
        super();
        text = "NoOp";
    }

    /**
     * Set the worker's next address to the next address.
     *
     */
    @Override
    public void execute(int date, Worker w) {
        super.execute(date, w);
        w.setCurrentAddress(w.getCurrentAddress() + 1);
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getText() {
        return text;
    }

    /**
     * This method must be redefined by any subclass.
     *
     * @return the name of the instruction
     */
    public String getName() {
        return "NoOperation";
    }

    @Override
    public Notification createNotification() {
        System.out.println("NOOP create notification");
        Notification n = new Notification(this.getName(), null, "");
        return n;
    }
}
