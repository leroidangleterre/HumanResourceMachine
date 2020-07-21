package humanresourcemachine;

import java.util.ArrayList;
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
    private TerrainModel terrainModel; // The terrain is an observer of ScriptModel, it must also be an observer of each instructionModel.

    public ScriptModel() {
        instructions = new ArrayList<>();
        observersList = new ArrayList<>();
        workers = new ArrayList<>();
        date = 0;
        terrainModel = null;
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
        newIns.addObserver(terrainModel);
        if (newIns instanceof IfInstructionModel) {
            terrainModel.addObserver((IfInstructionModel) newIns);
        }
        this.instructions.add(rank, newIns);
    }

    /**
     * Place a new instruction at a given position, replacing whatever
     * instruction is already at that position.
     *
     * @param newIns
     * @param rank
     */
    public void setInstruction(InstructionModel newIns, int rank) {
        if (rank == -1) {
            rank = instructions.size();
        }
        newIns.addObserver(terrainModel);
        if (newIns instanceof IfInstructionModel) {
            terrainModel.addObserver((IfInstructionModel) newIns);
        }
        this.instructions.set(rank, newIns);
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

        System.out.println("script step");
        // Each worker must execute their current instruction.
        for (Worker w : workers) {
            System.out.println("    Worker " + w.getSerial());
            // Find the correct instruction
            int currentAddress = w.getCurrentAddress();
            if (currentAddress < this.length()) {
                InstructionModel inst = this.instructions.get(currentAddress);
                Notification notif = inst.createNotification();
                notif.setWorker(w);
                notifyObservers(notif);
            }
            // Else the worker has finished working.
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

        Notification n = new Notification("TerrainApplyInstructions", null);
        notifyObservers(n);
        n = new Notification("ScriptRepaint", null);
        notifyObservers(n);
    }

    // As an Observable, we notify Observers (the Terrain) when this Script changes.
    @Override
    public void addObserver(Observer obs) {
        if (!observersList.contains(obs)) {
            observersList.add(obs);

            if (obs instanceof TerrainModel) {
                terrainModel = (TerrainModel) obs;
            }
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
     * Remove all the workers.
     *
     */
    public void removeAllWorkers() {
        for (InstructionModel ins : instructions) {
            ins.removeAllWorkers();
        }
        workers.clear();
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
        int indexFirst = Math.min(index0, index1);
        int indexSecond = Math.max(index0, index1);

        InstructionModel second = instructions.remove(indexSecond);
        InstructionModel first = instructions.remove(indexFirst);

        // Adding formerly-second instruction at first index;
        instructions.add(indexFirst, second);
        // Adding formerly-first instruction at second index;
        instructions.add(indexSecond, first);

        this.updateInstructionTargets(index0, index1);
    }

    /**
     * When an instruction swap involves an IF or a JUMP target, the parent
     * instruction (the IF or the JUMP) must update their target address.
     *
     * @param index0
     * @param index1
     */
    private void updateInstructionTargets(int index0, int index1) {
        // Find any IF or JUMP that leads to any of the two indexes;
        // Tell those that the target has changed.
        for (InstructionModel inst : instructions) {
            if (inst instanceof JumpInstructionModel) {
                JumpInstructionModel jumpInst = (JumpInstructionModel) inst;
                int targetAddress = jumpInst.getTargetAddress();
                if (targetAddress == index0) {
                    jumpInst.setTargetAddress(index1);
                } else if (targetAddress == index1) {
                    jumpInst.setTargetAddress(index0);
                }
            } else if (inst instanceof IfInstructionModel) {
                IfInstructionModel ifInst = (IfInstructionModel) inst;

                // Change the ELSE address if necessary
                if (ifInst.getElseAddress() == index0) {
                    ifInst.setElseAddress(index1);
                } else if (ifInst.getElseAddress() == index1) {
                    ifInst.setElseAddress(index0);
                }

                // Change the END address if necessary
                if (ifInst.getEndAddress() == index0) {
                    ifInst.setEndAddress(index1);
                } else if (ifInst.getEndAddress() == index1) {
                    ifInst.setEndAddress(index0);
                }
            }
        }
    }

    public void clear() {
        instructions.clear();
    }
}
