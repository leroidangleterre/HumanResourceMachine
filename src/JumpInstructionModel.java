/**
 *
 * @author arthurmanoha
 */
public class JumpInstructionModel extends InstructionModel {

    private int targetAddress;
    private InstructionModel target;

    public JumpInstructionModel() {
        super();
        targetAddress = 0;
        target = null;
    }

    @Override
    public String getName() {
        return "JumpInstruction";
    }

    public void setTargetInstruction(InstructionModel newTarget) {
        target = newTarget;
    }

    public void setTargetAddress(int newAddress) {
        targetAddress = newAddress;
    }

    public int getTargetAddress() {
        return targetAddress;
    }

    @Override
    public Notification createNotification() {
        Notification n = new Notification(this.getName(), null, this.targetAddress + "");
        return n;
    }

    @Override
    public String toString() {
        String text = getText() + " " + targetAddress;
        return text;
    }
}
