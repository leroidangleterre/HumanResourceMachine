/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class PushInstructionModel extends MoveInstructionModel {

    public PushInstructionModel() {
        super();
    }

    public PushInstructionModel(CardinalPoint newCardPoint) {
        super(newCardPoint);
    }

    /**
     * This method must be redefined by any subclass.
     *
     * @return the name of the instruction
     */
    public String getName() {
        return "WorkerPush";
    }

}
