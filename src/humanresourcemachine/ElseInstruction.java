package humanresourcemachine;

/**
 * The ELSE instruction is a Jump designed to be used in an IF construct.
 *
 * it points at the END instruction, but branches the worker to the line after the END.
 *
 * @author arthurmanoha
 */
public class ElseInstruction extends JumpInstruction {

    public ElseInstruction() {
        super();
        model = new ElseInstructionModel();
    }

    @Override
    public void setTargetInstruction(Instruction newTarget, int address) {
        super.setTargetInstruction(newTarget, address);
        setText("Else");
    }

    @Override
    public ElseInstructionModel getModel() {
        return (ElseInstructionModel) model;
    }
}
