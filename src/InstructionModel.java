import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class InstructionModel extends MyDefaultModel {

    // An IF loop may have several inner instructions;
    // A GOTO instruction has a target instruction
    ArrayList<InstructionModel> subInstructions;

    public InstructionModel() {
        subInstructions = new ArrayList<>();
    }

    @Override
    public void selectContent() {
    }

    /**
     * Tell if a given point lies inside a selected component.
     *
     * @param x
     * @param y
     * @return true when the point located at (x, y) is inside a selected
     * instruction
     */
    public boolean pointIsInSelection(double x, double y) {
        return false;
    }

}
