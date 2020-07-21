package humanresourcemachine;

/**
 * The ELSE instruction points at the END, but branches the worker to the
 * instruction following the END (hence 'targetAddress+1' in the notification).
 *
 * @author arthurmanoha
 */
public class ElseInstructionModel extends JumpInstructionModel {

    public ElseInstructionModel() {
        super();
    }

    @Override
    public String getName() {
        return "ElseInstruction";
    }

    @Override
    public String toString() {
        return getText() + " " + targetAddress;
    }

    @Override
    public void setText(String newText) {
        super.setText(newText);
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, (this.targetAddress + 1) + "");
        return n;
    }
}
