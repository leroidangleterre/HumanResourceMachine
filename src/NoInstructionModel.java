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
        Notification n = new Notification(this.getName(), null, "");
        return n;
    }
}
