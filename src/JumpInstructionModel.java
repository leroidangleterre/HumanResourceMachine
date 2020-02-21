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

    /**
     * Set the worker's next address
     *
     */
    @Override
    public void execute(int date, Worker w) {

        w.setCurrentAddress(this.targetAddress); // go to the instruction designated by this Jump.
        super.execute(date, w);
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

}
