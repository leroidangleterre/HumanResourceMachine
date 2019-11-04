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

    public enum ScriptTool {

        MOVE, PICKUP, DROP, SELECTION
        //TODO Add all the instructions
    }
    private ScriptTool currentTool;

    public Script() {
        super();
        pause();
        instructions = new ArrayList<>();
        currentTool = ScriptTool.SELECTION;
    }

    /**
     * Add a new instruction at the specified position.
     *
     * @param newIns
     * @param rank
     */
    public void addInstruction(Instruction newIns, int rank) {
        this.instructions.add(rank, newIns);
        computeDimensions();
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
        if (instructions != null && instructions.size() != 0) {
            int xDisplay = (int) x0;
            int instrSize = (int) (zoom * instructions.get(0).getHeight());
            int yDisplay = (int) (panelHeight - (y0 + instrSize * length()));
            for (Instruction instruction : instructions) {
                instruction.paint(g, panelHeight, xDisplay, yDisplay, zoom);
                yDisplay += instrSize;
            }
        }
    }

    @Override
    public void receiveLeftClick(double x, double y) {
        super.receiveLeftClick(x, y);
        if (currentTool == ScriptTool.SELECTION) {
            isSelecting = true;
        }
    }

    @Override
    public void receiveLeftRelease(double x, double y) {
        super.receiveLeftRelease(x, y);
        switch (currentTool) {
            case SELECTION:
                isSelecting = false;
                // TODO Select everything that is inside the selection rectangle.
                selectContent();
                break;
            default:
                break;
        }
    }

    /**
     * Analyse the list of instructions to know the width and height of the
     * script.
     *
     */
    private void computeDimensions() {
        xMin = 0;
        yMin = 0;
        xMax = 0;
        yMax = 0;
        for (Instruction i : instructions) {
            yMax += i.getHeight();
            xMax = Math.max(xMax, i.getWidth());
        }
    }

    @Override
    public double getWidth() {
        computeDimensions();
        return xMax - xMin;
    }

    /**
     * Return the number of instructions
     *
     * @return the number of instructions.
     */
    public int length() {
        return instructions.size();
    }

    /**
     * Select the appropriate instructions.
     */
    @Override
    public void selectContent() {
        if (instructions != null && !instructions.isEmpty()) {

            double size = instructions.get(0).getSize();
            if (size < 0) {
                size = 1;
            }
            int numLineClick = (int) ((yMax - yClick) / size);
            int numLineRelease = (int) ((yMax - yRelease) / size);

            int firstLine = Math.min(numLineClick, numLineRelease);
            firstLine = Math.max(0, Math.min(this.length() - 1, firstLine));

            int lastLine = Math.max(numLineClick, numLineRelease);
            lastLine = Math.max(0, Math.min(this.length() - 1, lastLine));

            for (int line = 0; line < length(); line++) {
                if (line >= firstLine && line <= lastLine) {
                    instructions.get(line).setSelected(true);
                } else {
                    instructions.get(line).setSelected(false);
                }
            }
        }
    }
}
