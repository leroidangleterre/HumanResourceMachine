import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the set of instructions that each worker will execute.
 *
 * @author arthurmanoha
 */
public class Script extends MyDefaultComponent implements Observer {

    protected boolean newInstructionBeingCreated;

    double initX0 = 90;
    double initY0 = 614;
    double initZoom = 2;

    private final int indentationWidth = 25;

    public enum ScriptTool {

        MOVE, PICKUP, DROP, SELECTION
    }
    protected ScriptTool currentTool;

    private ArrayList<Instruction> instList;

    private boolean isPlaying;
    private int delay;
    private int period; // milliseconds
    private Timer timer;

    public Script() {
        super();
        model = new ScriptModel();
        currentTool = ScriptTool.SELECTION;
        instList = new ArrayList<>();

        x0 = initX0;
        y0 = initY0;
        zoom = initZoom;
        newInstructionBeingCreated = false;

        isPlaying = false;
        delay = 0;
        period = 1000;
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

        if (newIns instanceof JumpInstruction) {
            // Create the target instruction, aligned on the right
            NoInstruction jumpTarget = new NoInstruction();
            jumpTarget.setText("");
            int targetAddress = rank + 1;
            this.addInstruction(jumpTarget, targetAddress);
            ((JumpInstruction) newIns).setTargetInstruction(jumpTarget, targetAddress);
            jumpTarget.setSelected(true);
        }

        if (newIns instanceof IfInstruction) {
            // Create the Else and the Endif instructions.
            ElseInstruction elseTarget = new ElseInstruction();
            int elseAddress = rank + 1;
            this.addInstruction(elseTarget, elseAddress);
            ((IfInstruction) newIns).setElseInstruction(elseTarget, elseAddress);
            NoInstruction endTarget = (NoInstruction) elseTarget.getTargetInstruction();
            endTarget.setText("END");
            int endAddress = rank + 2;
            ((IfInstruction) newIns).setEndInstruction(endTarget, endAddress);
            elseTarget.setSelected(true);
            endTarget.setSelected(true);
            elseTarget.setTargetInstruction(endTarget, endAddress);

            ((IfInstruction) newIns).setIndentationWidth(indentationWidth);
        }

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
     * Remove and return the instruction at the given rank. Must also remove the
     * corresponding entry in the model.
     *
     * @param rank
     * @return
     */
    public Instruction deleteInstruction(int rank) {
        Instruction removedInst = instList.remove(rank);
        ((ScriptModel) model).deleteInstruction(rank);
        return removedInst;
    }

    /**
     * Remove any selected instruction.
     *
     */
    public void deleteSelectedInstructions() {

        // TODO Delete the targets of Jump and If instructions
        for (int rank = instList.size() - 1; rank >= 0; rank--) {
            Instruction inst = instList.get(rank);
            if (inst.isSelected()) {
                instList.remove(rank);
                ((ScriptModel) model).deleteInstruction(rank);
            }
        }

        computeSizesAndPositions();
    }

    /**
     * Make each worker advance one step in their own script.
     *
     */
    public void step() {
        ((ScriptModel) model).step();
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
        case "MOVE":
            newInst = new MoveInstruction();
            break;
        case "PICKUP":
            newInst = new PickupInstruction();
            break;
        case "DROP":
            newInst = new DropInstruction();
            break;
        case "JUMP":
            newInst = new JumpInstruction();
            break;
        case "IF":
            newInst = new IfInstruction();
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
            addInstruction(newInst, newIndex);
        }
        repaint();
    }

    // As an Observer, we receive info from Observable objects (the Terrain);
    @Override
    public void update(Notification n) {
        switch (n.getName()) {
        case "newWorker":
            ((ScriptModel) model).addWorker((Worker) n.getObject());
            repaint();
            break;
        case "ScriptRepaint":
            repaint();
            break;
        default:
            break;
        }
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

    private Instruction getInstruction(double y) {
        for (Instruction inst : instList) {
            if (inst.containsPoint(y)) {
                return inst;
            }
        }
        return null;
    }

    /**
     * Get the instruction located within the script at the given rank.
     *
     * @param rank
     * @return null if the rank does not refer to an instruction, or the
     * corresponding instruction.
     */
    public Instruction getInstruction(int rank) {
        Instruction inst;
        try {
            inst = this.instList.get(rank);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return inst;
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

        // Display the instruction from first to last.
        for (Instruction inst : instList) {
            if (inst != null) {
                inst.paint(g, panelHeight, x0, y0, zoom);
            }
        }
        paintWorkers(g, panelHeight, x0, y0, zoom);
    }

    /**
     * Display a blip for each worker about to execute each instruction.
     *
     * @param g
     * @param panelHeight
     * @param x0
     * @param y0
     * @param zoom
     */
    private void paintWorkers(Graphics g, int panelHeight, double x0, double y0, double zoom) {

        // Each blip is as tall as an instruction.
        int height;
        if (instList.isEmpty()) {
            height = 0;
        } else {
            height = instList.get(0).getHeight();
        }
        int blipSize = (int) (height * zoom * 0.9);

        int xDisplay;
        int yDisplay;

        int instrIndex = 0;

        for (Instruction inst : instList) {
            xDisplay = (int) (x0 - blipSize);
            if (inst != null) {
                yDisplay = (int) (panelHeight - (y0 + inst.getY()));
            } else {
                yDisplay = (int) (panelHeight - y0);
            }
            for (Worker w : ((ScriptModel) model).getWorkers()) {
                if (w.getCurrentAddress() == instrIndex) {
                    g.setColor(Color.red);
                    g.fillOval(xDisplay, yDisplay, blipSize, blipSize);
                    g.setColor(Color.black);
                    String text = w.getSerial() + "";
                    g.drawChars(text.toCharArray(), 0, text.length(), xDisplay + blipSize / 2, yDisplay + g.getFont().getSize());
                    xDisplay -= blipSize + 1;// Next blip is displayed to the left of the one we just drew.
                }
            }
            instrIndex++;
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
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            leftClickIsActive = true;
            // Click on the selection to start moving that selection;
            // Click outside the selection to start creating a new selection
            if (pointIsInSelection(yClickInScript)) {
                selectionIsMoving = true;
            } else {
                isSelecting = true;
            }
            newInstructionBeingCreated = false;
            break;
        case MouseEvent.BUTTON2:
            wheelClickIsActive = true;
            isSelecting = false;
            selectionIsMoving = false;
            break;
        case MouseEvent.BUTTON3:
            // The right click event is passed to the appropriate instruction.
            Instruction inst = this.getInstruction(yClickInScript);
            if (inst != null) {
                inst.mousePressed(e);
            }
            break;
        default:
            break;
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
                inst.setY(inst.getY() + dy);
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
            this.setX0(x0 + (e.getX() - xMouse));
            this.setY0(y0 - (e.getY() - yMouse));
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
        int y = 0;
        int x = 0;
        int dx = (int) (indentationWidth * zoom);

        for (Instruction inst : instList) {
            if (inst != null) {
                inst.setZoom(zoom);
                if (!(selectionIsMoving && inst.isSelected())) {
                    if (y != inst.getY()) {
                        // A non-selected instruction is being translated.
                        inst.setY(y);
                    }
                }

                // Update the target rank of the Jump instructions in case their target has moved.
                if (inst instanceof JumpInstruction) {
                    Instruction target = ((JumpInstruction) inst).getTargetInstruction();
                    if (target != null) {
                        int targetRank = this.findIndexOf(target);
                        ((JumpInstruction) inst).setTargetInstruction(target, targetRank);
                    }
                }

                // Update the target ranks of the IF instructions in case these targets have moved.
                if (inst instanceof IfInstruction) {
                    ElseInstruction elseTarget = ((IfInstruction) inst).getElseInstruction();
                    Instruction endTarget = ((IfInstruction) inst).getEndInstruction();

                    int newElseRank = this.findIndexOf(elseTarget);
                    int newEndRank = this.findIndexOf(endTarget);

                    ((IfInstruction) inst).setElseInstruction(elseTarget, newElseRank);
                    ((IfInstruction) inst).setEndInstruction(endTarget, newEndRank);
                    if (elseTarget != null) {
                        ((JumpInstruction) elseTarget).setText("ELSE " + newEndRank);
                    }
                }

                // Everything inside an if construct is shifted to the right.
                inst.setX(x);

                if (inst instanceof IfInstruction) {
                    x += dx;
                }
                if (inst instanceof JumpInstruction && ((JumpInstruction) inst).getText().equals("ELSE")) {
                    // The ELSE must be one level to the left.
                    inst.setX(inst.getX() - dx);
                }
                if (inst instanceof NoInstruction && ((NoInstruction) inst).getText().equals("END")) {
                    // The END must be one level to the left.
                    x -= dx;
                    inst.setX(x);
                    // Next instruction at same indent.
                }

                y -= inst.getHeight() * zoom;
            }
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
    public void swapInstructions(int index0, int index1) {

        if (index0 == index1) {
            return;
        }
        ((ScriptModel) model).swapInstructions(index0, index1);

        // Important: the second instruction is removed first, and reinserted first.
        int indexFirst = Math.min(index0, index1);
        int indexSecond = Math.max(index0, index1);
        Instruction second = instList.remove(indexSecond);
        Instruction first = instList.remove(indexFirst);

        // Adding formerly-second instruction at first index;
        instList.add(indexFirst, second);
        // Adding formerly-first instruction at second index;
        instList.add(indexSecond, first);

        // Jump -> NoOp link: everytime one of the swapped instructions (or both) is a NoInstr,
        // we must update the address of each JumpInstruction.
        computeSizesAndPositions();
        repaint();
    }

    /**
     * Move an instruction from an initial rank to a new rank.
     *
     * @param indexStart
     * @param indexEnd
     */
    public void moveInstruction(int indexStart, int indexEnd) {
        int dx;
        if (indexStart < indexEnd) {
            dx = 1;
        } else {
            dx = -1;
        }

        while (indexStart != indexEnd) {
            swapInstructions(indexStart, indexStart + dx);
            indexStart += dx;
        }
    }

    /**
     * Find at which rank the given instruction is located within the script.
     *
     * @param instParam the instruction we search for
     * @return -1 if the instruction does not exist in the script, the correct
     * index otherwise.
     */
    public int findIndexOf(Instruction instParam) {
        int index = 0;
        if (instParam == null) {
            return -1;
        }
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

    public void printInstructionsAsText() {
        int index = 0;
        for (Instruction inst : instList) {
            if (inst == null) {
                System.out.println("Script: error in printInstructionsAsText, instruction is NULL");
            } else {
                System.out.print(index + " <" + inst.getClass());
                if (inst instanceof IfInstruction) {
                    System.out.print(" else at " + ((IfInstructionModel) ((IfInstruction) inst).getModel()).getElseAddress());
                    System.out.print(" end at " + ((IfInstructionModel) ((IfInstruction) inst).getModel()).getEndAddress());
                    index++;
                } else if (inst instanceof JumpInstruction) {
                    System.out.print(" jump to " + ((JumpInstructionModel) ((JumpInstruction) inst).getModel()).getTargetAddress());
                }
                System.out.println("");
            }
        }
    }

    void save() {
        try {
//            System.out.println("Script is saving.");
            String path = HumanResourceMachine.path;
            String filename = "\\script.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + filename));

            writer.write(((ScriptModel) model).size() + "\n");

            for (Instruction inst : instList) {
                writer.write(inst + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot save, exception");
        }
    }

    /**
     * Remove all instructions. Must clear the model.
     */
    public void clear() {
//        System.out.println("Clearing script");
        instList.clear();
        ((ScriptModel) model).clear();
        repaint();
    }

    public void load() {
        String path;
        String filename = "no filename set";

        try {
            clear();
            path = HumanResourceMachine.path;
            filename = path + "\\script.txt";
            System.out.println("Loading script file: " + filename);

            FileReader fileReader = new FileReader(filename);

            BufferedReader reader = new BufferedReader(fileReader);
            String text;

            int index = 0;
            int nbInstructions = -1;
            while ((text = reader.readLine()) != null) {
                if (nbInstructions == -1) {
                    // Read the number of instructions on the first line
                    nbInstructions = Integer.valueOf(text);
                    // Create NoOps that will be replaced. We need this to be
                    // able to place instructions after a jump target for example
                    for (int i = 0; i < nbInstructions; i++) {
                        addInstruction(new NoInstruction());
                    }
                } else {
                    // Create the instruction corresponding to the given text.
                    decodeInstruction(text, index);
                    index++;
                }
            }

            // Make the links between IfInstructions and their targets.
            // They already know the addresses, not the instructions yet.
            computeSizesAndPositions();
            System.out.println("Script file loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("Cannot load: file <" + filename + "> not found.");
        } catch (IOException ex) {
            System.out.println("Cannot load: IO exception");
        }
        repaint();
    }

    /**
     * Convert a text read from a save file into an actual instruction.
     *
     * Must update both the Script and the ScriptModel.
     *
     * @param text the textual representation of the instruction
     * @param index the rank at which the instruction shall be placed within the
     * script
     */
    private void decodeInstruction(String text, int index) {
        Instruction inst = null;

        if (text.equals("NoOperation")) {
            // Check if this NoInstruction is already the target of an IF or a JUMP
            if (getInstruction(index) instanceof NoInstruction) {
                String currentText = ((NoInstruction) getInstruction(index)).getText();
                if (currentText.contains("ELSE") || currentText.contains("END")) {
                }
            }

        } else if (text.contains("If")) {
            inst = new IfInstruction();
            String ifOptions[] = text.split(" ");

            CardinalPoint direction = CardinalPoint.valueOf(ifOptions[1]);
            String comparator = ifOptions[2];
            String choiceBoxValue = ifOptions[3];
            String elseAddressString = ifOptions[4];
            String endAddressString = ifOptions[5];

            ((IfInstruction) inst).setCompass(direction);
            ((IfInstruction) inst).setComparator(comparator);
            ((IfInstruction) inst).setChoiceBoxValue(choiceBoxValue);
            int elseAddress = Integer.valueOf(elseAddressString);
            int endAddress = Integer.valueOf(endAddressString);

            NoInstruction endInst = new NoInstruction();
            instList.set(endAddress, endInst);
            endInst.setText("END");

            ElseInstruction elseInst = new ElseInstruction();
            instList.set(elseAddress, elseInst);
            elseInst.setTargetInstruction(endInst, endAddress);

            ((IfInstruction) inst).setElseInstruction(elseInst, elseAddress);
            ((IfInstruction) inst).setEndInstruction(endInst, endAddress);

            instList.set(elseAddress, elseInst);
            ((ScriptModel) model).setInstruction(elseInst.getModel(), elseAddress);

            // Set the indentation one level deeper betweeen the IF and the END
            ((IfInstruction) inst).setIndentationWidth(indentationWidth);

        } else if (text.contains("Move")) {
            String parameters = text.substring(text.indexOf(" ") + 1);
            inst = new MoveInstruction(parameters);

        } else if (text.contains("WorkerPickup")) {
            String parameters = text.substring(text.indexOf(" ") + 1);
            inst = new PickupInstruction(parameters);

        } else if (text.contains("Drop")) {
            inst = new DropInstruction();

        } else if (text.contains("Jump")) {
            inst = new JumpInstruction();
            // JUMP target must be set properly
            int targetAddress = Integer.valueOf(text.split(" ")[1]);
            Instruction target = getInstruction(targetAddress);
            ((JumpInstruction) inst).setTargetInstruction(target, targetAddress);

        } else if (text.contains("ELSE")) {
            inst = null;
        }

        // Update the script model now that the Script component is up-to-date
        if (inst != null) {
            instList.set(index, inst);
            ((ScriptModel) model).setInstruction(inst.getModel(), index);
        }
    }

    private void waitForKeypressed() {

        try {
            System.out.println("press key");
            System.in.read();
        } catch (IOException e) {
            System.out.println("wait error...");
        }
    }

    public void testPrintNbInst() {
        System.out.println("In component: " + this.instList.size()
                + ", in model: " + ((ScriptModel) model).getNbInstructions());
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Start playing (or keep playing) with the most recently set period.
     */
    public void play() {
        this.isPlaying = true;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                step();
            }
        }, delay, period);
    }

    public void pause() {
        this.isPlaying = false;
        timer.cancel();
    }

    /**
     * Change the speed of the timer.
     *
     * @param faster true to execute instructions faster, false otherwise.
     */
    public void increaseSpeed(boolean faster) {
        if (faster) {
            period = period / 2;
            if (period == 0) {
                period = 1;
            }
        } else {
            period = 2 * period;
        }
        play();
    }
}
