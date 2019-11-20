import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
public class Script extends MyDefaultComponent {

    double initY0 = 800;

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

        y0 = initY0;

        zoom = 1.0;
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
    }

    /**
     * Receive a command.
     *
     * @param text the command
     */
    @Override
    public void receiveCommand(String text) {
        setTool(text);
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
     * Tell if a given point lies inside a selected component.
     *
     * @param x
     * @param y
     * @return true when the point located at (x, y) is inside a selected
     * instruction
     */
    @Override
    public boolean containsPoint(double x, double y) {

        return false;
    }

    /**
     * Tell if a given point is inside a selected instruction.
     *
     */
    public boolean pointIsInSelection(double yTest) {
        System.out.println("Script: pointIsInSelection");
        for (Instruction inst : instList) {
            if (inst.isSelected() && inst.containsPoint(yTest)) {
                System.out.println("Point is in selection (instruction " + inst.serialNumber + ");");
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

        for (Instruction inst : instList) {
            inst.paint(g, panelHeight, x0, y0, zoom);
        }
    }

    // Extract an instruction, and re-insert it at a random position.
    public void changeOrderOfInstructions() {
        int rank0 = (int) (Math.random() * this.instList.size());
        int rank1 = (int) (Math.random() * this.instList.size());

        // Remove instruction at rank0, then insert it at rank1
        Instruction inst = instList.remove(rank0);
        instList.add(rank1, inst);

        if (rank0 != rank1) {
            System.out.println("changed " + rank0 + " to " + rank1);
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
            System.out.println("Script: test for click in selection");
            if (pointIsInSelection(yClickInScript)) {
                selectionIsMoving = true;
                System.out.println("Script: selection is moving");
            } else {
                isSelecting = true;
            }
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
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectionIsMoving) {
            // Move each selected instruction up or down.
            for (Instruction inst : instList) {
                if (inst.isSelected()) {
                    inst.y -= (e.getY() - yMouse); // Y-axis is inverted
                }
            }
            // Detect when one non-selected instruction has to move to the other side of the moving block;
            // Tell the model to update accordingly.
            // NB: the Instruction component for the selected instructions must not update its y-coordinate
            // from the model but only from the mouse movements.
        } else if (!isSelecting) {

            this.x0 += e.getX() - xMouse;
            this.y0 -= e.getY() - yMouse; // Y-axis is inverted

        }

        xMouse = e.getX();
        yMouse = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e
    ) {
        super.mouseMoved(e);
        repaint();
    }

    @Override
    public void mouseWheelMoved(double fact, int xMouse, int yMouse
    ) {
        super.mouseWheelMoved(fact, xMouse, yMouse);
        computeSizesAndPositions();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e
    ) {
        super.mouseWheelMoved(e);
        computeSizesAndPositions();
    }

    /**
     * Compute the sizes and positions of all instructions. Maybe we should name
     * this function "layout"...
     */
    private void computeSizesAndPositions() {

        // The instructions will be located in the (x positive, y negative) region,
        // with the first instruction starting at (0,0).
        int x = 0, y = 0;
        for (Instruction inst : instList) {
            inst.setPosition(x, y);
            y -= inst.getHeight() * zoom;
            inst.setZoom(zoom);
        }
    }
}
