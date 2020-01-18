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

    private ArrayList<Worker> workersList;
    private static int NB_INSTRUCTION_MODELS_CREATED = 0;
    private int serial;

    public InstructionModel() {
        subInstructions = new ArrayList<>();

        workersList = new ArrayList<>();
        serial = NB_INSTRUCTION_MODELS_CREATED;
        NB_INSTRUCTION_MODELS_CREATED++;
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

    /**
     * Add a new worker who will execute this instruction when next step occurs.
     *
     * @param newWorker
     */
    public void addWorker(Worker newWorker) {
        workersList.add(newWorker);
    }

    /**
     * Get all the workers currently about to execute this instruction.
     *
     * @return the list of workers
     */
    public ArrayList<Worker> getWorkers() {
        return workersList;
    }

    public void removeAllWorkers() {
        workersList.clear();
    }

    /**
     * Get how many workers are currently about to execute this instruction.
     */
    public int getNbWorkers() {
        return workersList.size();
    }

    public int getSerial() {
        return serial;
    }

    /**
     * Execute the instruction. This method must be overriden by the subclasses
     * AND called by the overriding methods. NB: each instruction model must set
     * the new address for the worker.
     */
    public void execute(int date, Worker w) {
        if (w.getDate() == date) {
            w.setDate(w.getDate() + 1);
        }
    }
}
