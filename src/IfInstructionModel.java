/**
 *
 * @author arthurmanoha
 */
public class IfInstructionModel extends InstructionModel {

    // The element we are testing: current block, or neighbor square/worker/block...
    private Compass leftOperand;
    // The boolean operation: equals, greater than, ...
    private BooleanTest test;
    // The expected value: a number, or a type such as Worker, Empty, Wall...
    // TODO: replace that type with some sort of a Comparable interface.
    private Object rightOperand;

    private int elseAddress;
    private int endIfAddress;
    private InstructionModel elseTarget;
    private InstructionModel endIfTarget;

    public IfInstructionModel() {
        super();
        elseAddress = 0;
        elseTarget = null;
        endIfAddress = 0;
        endIfTarget = null;
    }

    /**
     * Execute the IF: evaluate the condition, then set the worker's next
     * address.
     *
     * @param date
     * @param w
     */
    @Override
    public void execute(int date, Worker w) {
        super.execute(date, w);
    }

    public void setElseInstruction(InstructionModel newTarget) {
        elseTarget = newTarget;
    }

    public void setElseAddress(int newAddress) {
        elseAddress = newAddress;
    }

    public int getElseAddress() {
        return elseAddress;
    }

    public void setEndInstruction(InstructionModel endInstruction) {
        endIfTarget = endInstruction;
    }

    public void setEndAddress(int newAddress) {
        endIfAddress = newAddress;
    }

    public int getEndAddress() {
        return endIfAddress;
    }

}
