import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

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
public class Script extends MyDefaultComponent {

    protected boolean newInstructionBeingCreated;

    double initY0 = 888;
    double initX0 = 38;
    double initZoom = 2;

    public enum ScriptTool {

        MOVE, PICKUP, DROP, SELECTION
    }
    protected ScriptTool currentTool;

    private ArrayList<Instruction> instList;

    public Script() {
        super();
        model = new ScriptModel();
        currentTool = ScriptTool.SELECTION;
        instList = new ArrayList<>();

        x0 = initX0;
        y0 = initY0;
        zoom = initZoom;
        newInstructionBeingCreated = false;
    }

    /**
     * Add a new instruction at the specified position.
     *
     * @param newIns
     * @param rank if -1, add at the end.
     */
    public void addInstruction(Instruction newIns, int rank) {
        if (rank == -1) {
            rank = instList.size();
        }
        ((ScriptModel) model).addInstruction(newIns.getModel(), rank);
        this.instList.add(rank, newIns);

        computeSizesAndPositions();
    }

    /**
     * Add a new instruction at the end.
     *
     * @param newIns
     */
    public void addInstruction(Instruction newIns) {
        this.addInstruction(newIns, -1);
    }

    /**
     * Remove and return the instruction at the given rank.
     *
     * @param rank
     * @return
     */
    public Instruction removeInstruction(int rank) {
        return instList.remove(rank);
    }

    /**
     * Remove any selected instruction.
     *
     */
    public void deleteSelectedInstructions() {

        Iterator<Instruction> iter = instList.iterator();
        while (iter.hasNext()) {
            if (iter.next().isSelected()) {
                iter.remove();
            }
        }

        computeSizesAndPositions();
    }

    /**
     * Make each worker advance one step in their own script.
     *
     */
    public void step() {

    }

    /**
     * Return the number of instructions
     *
     * @return the number of instructions.
     */
    public int length() {
        return ((ScriptModel) model).size();
    }

    public void unselectEverything() {
        model.unselectEverything();
        for (Instruction inst : instList) {
            inst.setSelected(false);
        }
    }

    /**
     * Receive a command.
     *
     * @param text the command
     */
    @Override
    public void receiveCommand(String text) {
        Instruction newInst = null;
        switch (text) {
            // Valid commands: MOVE, PICKUP, DROP, SELECTION
            case "MOVE":
                newInst = new MoveInstruction();
                break;
            case "PICKUP":
                newInst = new Instruction();
                break;
            case "DROP":
                newInst = new Instruction();
                break;
            case "DELETEINSTR":
                deleteSelectedInstructions();
                break;
            default:
                break;
        }
        if (newInst != null) {

            // The new instruction is placed at the top of the panel.
            int apparentY = 0;
            int newY = (int) ((panelHeight - apparentY - this.y0));
            int xApparent = 0;

            newInst.setPosition(xApparent, newY);
            unselectEverything();
            newInst.setSelected(true);
            selectionIsMoving = true;
            newInstructionBeingCreated = true;

            // Insert the instruction at the appropriate position.
            int newIndex = 0;
            while (newIndex < instList.size() && instList.get(newIndex).getY() > newInst.getY()) {
                newIndex++;
            }

            instList.add(newIndex, newInst);
        }
        repaint();
    }

    /**
     * Set the new tool.
     *
     * @param newTool
     *
     */
    public void setTool(ScriptTool newTool) {
        this.currentTool = newTool;
    }

    /**
     * Set the new tool.
     *
     * @param s the String in capital letters (ex: "HOLE", or "SELECTION")
     */
    public void setTool(String s) {
        switch (s) {
            // Valid commands: MOVE, PICKUP, DROP, SELECTION
            case "SELECTION":
                this.setTool(ScriptTool.SELECTION);
                break;
            case "MOVE":
                this.setTool(ScriptTool.MOVE);
                break;
            case "PICKUP":
                this.setTool(ScriptTool.PICKUP);
                break;
            case "DROP":
                this.setTool(ScriptTool.DROP);
                break;
            default:
                // Keep the current tool unchanged.
                break;
        }
    }

    /**
     * Tell if a given point is inside a selected instruction.
     *
     * @param yTest the y-coordinate of the tested point
     * @return true when the point is inside the instruction, regardless of its
     * x-coordinate
     */
    public boolean pointIsInSelection(double yTest) {
        for (Instruction inst : instList) {
            if (inst.isSelected() && inst.containsPoint(yTest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move the selection. Movement started at (yClick) and is now at (yMouse)
     *
     * @param x
     * @param y
     */
    public void moveSelection(double x, double y) {
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
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {

        // First, display all non-selected instructions.
        for (Instruction inst : instList) {
            if (!inst.isSelected()) {
                inst.paint(g, panelHeight, x0, y0, zoom);
            }
        }
        // Then, display all selected instructions on top of the rest.
        for (Instruction inst : instList) {
            if (inst.isSelected()) {
                inst.paint(g, panelHeight, x0, y0, zoom);
            }
        }
    }

    // Extract an instruction, and re-insert it at a random position.
    public void changeOrderOfInstructions() {
        int rank0 = (int) (Math.random() * this.instList.size());
        int rank1 = (int) (Math.random() * this.instList.size());

        if (rank0 != rank1) {
            // Remove instruction at rank0, then insert it at rank1
            Instruction inst = instList.remove(rank0);
            instList.add(rank1, inst);
        }
        computeSizesAndPositions();
    }

    @Override
    public void paintComponent(Graphics g) {
        eraseAll(g);
        this.paint(g, panelHeight, x0, y0, zoom);
        super.paintComponent(g);
    }

    /**
     * Action performed when a left click is received.
     *
     * @param e The event received by the panel
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        xClick = e.getX();
        yClick = e.getY();
        double yClickInScript = (panelHeight - yClick - y0);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = true;
            // Click on the selection to start moving that selection;
            // Click outside the selection to start creating a new selection
            if (pointIsInSelection(yClickInScript)) {
                selectionIsMoving = true;
            } else {
                isSelecting = true;
            }
            newInstructionBeingCreated = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = true;
            isSelecting = false;
            selectionIsMoving = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        super.mouseReleased(e);

        ScriptModel sModel = ((ScriptModel) model);

        double yClickInScript = (panelHeight - yClick - y0);
        double yReleaseInScript = (panelHeight - yRelease - y0);

        double yMin = Math.min(yClickInScript, yReleaseInScript);
        double yMax = Math.max(yClickInScript, yReleaseInScript);

        if (currentTool == ScriptTool.SELECTION && isSelecting) {
            sModel.unselectEverything();
        }
        // Click in an instruction and release in another one: regular selection by rectangle.
        switch (currentTool) {
            case SELECTION:
                // Do the selection action only if the left button was used.
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (selectionIsMoving) {
                        // Stop moving the instructions, snap the coordinates of the components
                        selectionIsMoving = false;

                    } else {
                        isSelecting = false;
                        for (Instruction inst : instList) {

                            // This instruction must be selected if either of these three conditions is met:
                            // 1) It contains the y-coordinate of the click, or
                            // 2) It contains the y-coordinate of the release, or
                            // 3) It is contained between the y-coordinate of the click and that of the release.
                            if (inst.containsPoint(yMax) || inst.containsPoint(yMin)
                                    || inst.isContainedBetweenY(yMin, yMax)) {
                                inst.setSelected(true);
                            } else {
                                inst.setSelected(false);
                            }
                        }
                    }
                }
                break;

            default:
                break;
        }
        computeSizesAndPositions();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved((MouseWheelEvent) e);
        computeSizesAndPositions();
    }

    private void moveSelectedInstructions(int dy) {
        // Move each selected instruction up or down.
        for (Instruction inst : instList) {
            if (inst.isSelected()) {
                inst.y += dy;
            }
        }
        detectInstructionOverlap();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectionIsMoving) {

            moveSelectedInstructions(-(int) (e.getY() - yMouse));

            detectInstructionOverlap();

        } else if (!isSelecting) {

            this.x0 += e.getX() - xMouse;
            this.y0 -= e.getY() - yMouse;

        }

        xMouse = e.getX();
        yMouse = e.getY();

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        if (selectionIsMoving || newInstructionBeingCreated) {
            moveSelectedInstructions(-(int) (e.getY() - yMousePrevious));
            detectInstructionOverlap();
        }
        repaint();
    }

    /**
     * Compute the sizes and positions of all instructions. Maybe we should name
     * this function "layout"...
     */
    private void computeSizesAndPositions() {

        // The instructions will be located in the (x positive, y negative) region,
        // with the first instruction starting at (0,0).
        // Special case for instructions that are being moved: no update of the y-coordinate.
        int x, y;

        x = 0;
        y = 0;
        for (Instruction inst : instList) {
            if (!(selectionIsMoving && inst.isSelected())) {
                if (y != inst.getY()) {
                    // A non-selected instruction is being translated.
                    inst.setPosition(x, y);
                }
            }
            y -= inst.getHeight() * zoom;
            inst.setZoom(zoom);
        }

    }

    /**
     * Detect when one non-selected instruction has to move to the other side of
     * the moving block; Tell the model to update accordingly. NB: the
     * Instruction component for the selected instructions must not update its
     * y-coordinate from the model but only from the mouse movements.
     */
    private void detectInstructionOverlap() {

        // Swap any two instructions that are in one order in the list,
        // but in the other order with respect to their y-coordinates.
        boolean mustLoop;

        do {
            mustLoop = false;
            for (int i = 0; i < instList.size(); i++) {
                Instruction instI = instList.get(i);
                for (int j = i + 1; j < instList.size(); j++) {
                    // Test instructions i and j (with j>i)
                    Instruction instJ = instList.get(j);
                    if (instJ.getY() > instI.getY()) {
                        swapInstructions(i, j);
                        mustLoop = true;
                    }
                }
            }
        } while (mustLoop);

        repaint();
    }

    /**
     * Swap two instructions. If at least one index is incorrect, do nothing.
     *
     * @param index0 the index of the first instruction
     * @param index1 the index of the second instruction
     */
    private void swapInstructions(int index0, int index1) {
        if (index0 == index1) {
            return;
        }

        // Important: the second instruction is removed first, and reinserted first.
        Instruction second = instList.remove(Math.max(index0, index1));
        Instruction first = instList.remove(Math.min(index0, index1));

        // Adding formerly-second instruction at first index;
        instList.add(index0, second);
        // Adding formerly-first instruction at second index;
        instList.add(index1, first);
        computeSizesAndPositions();
        repaint();
    }

    /**
     * Find at which rank the given instruction is located within the script.
     *
     * @param inst the instruction we search for
     * @return -1 if the instruction does not exist in the script, the correct
     * index otherwise.
     */
    private int findIndexOf(Instruction instParam) {
        int index = 0;
        for (Instruction inst : instList) {
            if (instParam.equals(inst)) {
                // We found the instruction.
                return index;
            }
            index++;
        }
        // At this step, no instruction was found.
        return -1;
    }

    private void printSelectedInstructions() {
        for (Instruction ins : instList) {
            System.out.println("Instruction " + ins.serialNumber + ", "
                    + (ins.isSelected() ? "is selected" : "is not selected"));
        }
    }
}
