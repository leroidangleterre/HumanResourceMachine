/**
 *
 * @author arthurmanoha
 */
public class NoInstructionModel extends InstructionModel {
    
    public NoInstructionModel() {
        super();
    }

    /**
     * Set the worker's next address
     *
     */
    @Override
    public void execute(int date, Worker w) {
        super.execute(date, w);
        w.setCurrentAddress(w.getCurrentAddress() + 1);
    }
}
