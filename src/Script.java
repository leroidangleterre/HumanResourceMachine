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
    public boolean pointIsInSelection(double x, double y) {

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
        xClick = e.getX();
        yClick = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = true;
            isSelecting = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        super.mouseReleased(e);

        ScriptModel sModel = ((ScriptModel) model);

        xRelease = e.getX(); // after refacto; may need to be removed
        yRelease = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = false;
        }

        double yClickInTerrain = (panelHeight - yClick - y0) / zoom;
        double yReleaseInTerrain = (panelHeight - yRelease - y0) / zoom;

        double xClickInTerrain = (xClick - x0) / zoom;
        double xReleaseInTerrain = (xRelease - x0) / zoom;

        int numLineClick = 0;
        int numColumnClick = 0;
        int numLineRelease = 0;
        int numColumnRelease = 0;

        int bottomLine = Math.max(numLineClick, numLineRelease);
        int topLine = Math.min(numLineClick, numLineRelease);
        int leftCol = Math.min(numColumnClick, numColumnRelease);
        int rightCol = Math.max(numColumnClick, numColumnRelease);

        if (currentTool == ScriptTool.SELECTION && isSelecting) {
            sModel.unselectEverything();
        }
        // Click in a square and release in another one: regular selection by rectangle.
        for (int line = topLine; line <= bottomLine; line++) {
            for (int col = leftCol; col <= rightCol; col++) {
                switch (currentTool) {
                    case SELECTION:
                        if (isSelecting) {
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        isSelecting = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(double fact, int xMouse, int yMouse) {
        super.mouseWheelMoved(fact, xMouse, yMouse);
        computeSizesAndPositions();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
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
        }
    }
}
