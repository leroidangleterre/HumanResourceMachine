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
public class InstructionModel extends MyDefaultModel implements Observable {

    // An IF loop may have several inner instructions;
    // A GOTO instruction has a target instruction
    ArrayList<InstructionModel> subInstructions;

    private ArrayList<Worker> workersList;
    private static int NB_INSTRUCTION_MODELS_CREATED = 0;
    private int serial;

    private String text;

    // This list will contain only the TerrainModel.
    private ArrayList<Observer> observersList;

    public InstructionModel() {
        subInstructions = new ArrayList<>();

        workersList = new ArrayList<>();
        serial = NB_INSTRUCTION_MODELS_CREATED;
        NB_INSTRUCTION_MODELS_CREATED++;
        observersList = new ArrayList<>();
        text = "";
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
     * the proper information to send to the TerrainModel.
     */
    public void execute(int date, Worker w) {
        if (w.getDate() == date) {
            w.setDate(w.getDate() + 1);
        }
    }

    /**
     * This method must be redefined by any subclass.
     */
    public String getName() {
        return "Instruction";
    }

    /**
     * This method must be redefined by any subclass.
     */
    public String getOptions() {
        return "no_options";
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        text = newText;
    }

    @Override
    public void addObserver(Observer obs) {
    }

    @Override
    public void removeObserver(Observer obs) {
    }

    @Override
    public void notifyObservers(Notification n) {
    }
}
