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
}
