import java.awt.Graphics;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This is the set of instructions that each worker will execute.
 *
 * @author arthurmanoha
 */
public class Script extends Displayable {

    private ArrayList<Instruction> instructions;

    public Script() {
        pause();
        instructions = new ArrayList<>();
    }

    /**
     * Add a new instruction at the specified position.
     *
     * @param newIns
     * @param rank
     */
    public void addInstruction(Instruction newIns, int rank) {
        newIns.setRank(rank);
        this.instructions.add(rank, newIns);
    }

    /**
     * Add a new instruction at the end.
     *
     * @param newIns
     */
    public void addInstruction(Instruction newIns) {
        addInstruction(newIns, instructions.size());
    }

    /**
     * Make each worker advance one step in their own script.
     *
     */
    public void step() {

    }

    /**
     * Paint the script.
     *
     * @param g the Graphics on which the script is painted
     * @param panelHeight the height of the panel (in pixels)
     * @param x0 the x-component of the scroll
     * @param y0 the y-component of the scroll
     * @param zoom the zoom factor
     */
    @Override
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        super.paint(g, panelHeight, x0, y0, zoom);
        for (Instruction instruction : instructions) {
            instruction.paint(g, panelHeight, x0, y0, zoom);
        }
    }

    @Override
    public void receiveLeftClick(double x, double y) {
        this.xClick = x;
        this.yClick = y;
    }

    @Override
    public void receiveLeftRelease(double x, double y) {

        this.xRelease = x;
        this.yRelease = y;
    }

}
