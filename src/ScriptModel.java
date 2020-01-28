import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class ScriptModel extends MyDefaultModel implements Observable {

    protected Timer timer;
    protected boolean isRunning;
    protected int date;

    private ArrayList<InstructionModel> instructions;
    private ArrayList<Worker> workers;

    private ArrayList<Observer> observersList;

    public ScriptModel() {
        instructions = new ArrayList<>();
        observersList = new ArrayList<>();
        workers = new ArrayList<>();
        date = 0;
    }

    /**
     * Return the number of instructions
     *
     * @return the number of instructions.
     */
    public int length() {
        if (instructions == null) {
            return 0;
        }
        return instructions.size();
    }

    /**
     * Mark the UIs as selected or not.
     *
     */
    @Override
    public void selectContent() {

    }

    public void selectContent(int numInstr) {

    }

    /**
     * Add a new instruction at the specified position.
     *
     * @param newIns
     * @param rank
     */
    public void addInstruction(InstructionModel newIns, int rank) {
        if (rank == -1) {
            rank = instructions.size();
        }
        this.instructions.add(rank, newIns);
    }

    /**
     * Remove any selected instruction.
     *
     * @param rank The rank of the instruction that will be removed.
     */
    public void deleteInstruction(int rank) {
        instructions.remove(rank);
    }

    public int size() {
        if (instructions == null) {
            return -1;
        }
        return instructions.size();
    }

    public ArrayList<InstructionModel> getInstructions() {
        return instructions;
    }

    public int getNbInstructions() {
        return instructions.size();
    }

    public void play() {
        this.timer.start();
    }

    public void pause() {
        this.timer.stop();
    }

    /**
     * Execute one instruction for each Worker, then move each worker to the
     * next instruction. NB: Only execute the instructions for the worker that
     * have the same date.
     */
    public void step() {

        // Each worker must execute their current instruction.
        for (Worker w : workers) {
            // Find the correct instruction
            int currentAddress = w.getCurrentAddress();
            if (currentAddress >= this.length()) {
                System.out.println("Worker " + w + " has finished working.");
            } else {
                InstructionModel inst = this.instructions.get(currentAddress);
                inst.execute(date, w);
            }
        }

        // Replace the workers in the correct instructions for display.
        for (InstructionModel inst : instructions) {
            inst.removeAllWorkers();
        }
        for (Worker w : workers) {
            int index = w.getCurrentAddress();
            try {
                InstructionModel inst = instructions.get(index);
                if (inst != null) {
                    inst.addWorker(w);
                }
            } catch (IndexOutOfBoundsException e) {

            }
        }

        date++;

        Notification n = new Notification("ScriptRepaint", null);
        notifyObservers(n);
    }

    // As an Observable, we notify Observers (the Terrain) when this Script changes.
    @Override
    public void addObserver(Observer obs) {
        if (!observersList.contains(obs)) {
            observersList.add(obs);
        }
    }

    @Override
    public void removeObserver(Observer obs) {
        observersList.remove(obs);
    }

    @Override
    public void notifyObservers(Notification n) {
        for (Observer observer : observersList) {
            observer.update(n);
        }
    }

    /**
     * Add a new worker that will be obeying the instructions.
     *
     * @param w The newly added worker
     */
    protected void addWorker(Worker w) {
        w.setDate(this.date);
        w.setCurrentAddress(0);
        workers.add(w);
        instructions.get(0).addWorker(w);
    }

    /**
     * Extract the list of workers.
     *
     * @return the list of workers
     */
    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    /**
     * Swap two instructions. If at least one index is incorrect, do nothing.
     *
     * @param index0 the index of the first instruction
     * @param index1 the index of the second instruction
     */
    protected void swapInstructions(int index0, int index1) {
        if (index0 == index1) {
            return;
        }
        // Important: the second instruction is removed first, and reinserted first.
        InstructionModel second = instructions.remove(Math.max(index0, index1));
        InstructionModel first = instructions.remove(Math.min(index0, index1));

        // Adding formerly-second instruction at first index;
        instructions.add(index0, second);
        // Adding formerly-first instruction at second index;
        instructions.add(index1, first);
    }
}
