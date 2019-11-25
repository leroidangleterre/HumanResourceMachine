import java.awt.Graphics;
import java.awt.event.MouseEvent;
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
public class ScriptModel extends MyDefaultModel {

    protected Timer timer;
    protected boolean isRunning;
    protected int date;

    private ArrayList<InstructionModel> instructions;

    public ScriptModel() {
        instructions = new ArrayList<>();
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
}
