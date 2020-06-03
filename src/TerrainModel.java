import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author arthurmanoha
 */
public class TerrainModel extends MyDefaultModel implements Observer, Observable {

    // The grid that contains squares (walls, ground, ...) and in which workers evolve.
    protected Square[][] grid;
    // The dimensions of the grid.
    private final int nbCols, nbLines;
    private final double elemSize;
    private ArrayList<Observer> observersList;

    private ArrayList<Notification> notifList;

    int nbCallsUpdate;

    public TerrainModel(int nbLines, int nbCols) {

        this.nbLines = nbLines;
        this.nbCols = nbCols;
        grid = new Square[nbLines][];
        elemSize = 1;
        for (int i = 0; i < nbLines; i++) {
            grid[i] = new Square[nbCols];
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = new Ground((j + 0.5) * elemSize, (nbLines - 0.5 - i) * elemSize, elemSize);
            }
        }

        int line = 0;
        int col = 2;
        grid[line][col] = new Wall((col + 0.5) * elemSize, (nbLines - 0.5 - line) * elemSize, elemSize);

        for (int i = 0; i < nbLines; i++) {
            grid[i][0].createDataCube(10 + i);
        }

        Square s = this.getSquare(0, 0);

        this.xMin = this.getSquare(0, 0).getXMin();
        this.xMax = this.getSquare(0, this.nbCols - 1).getXMax();
        this.yMin = this.getSquare(this.nbLines - 1, 0).getYMin();
        this.yMax = this.getSquare(0, 0).getYMax();

        observersList = new ArrayList<>();
        notifList = new ArrayList<>();

        nbCallsUpdate = 0;
    }

    @Override
    public String toString() {
        return "TerrainModel;";
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
     * Get the square that contains the specified coordinates.
     *
     * @param x x-coordinate of the point we are looking for
     * @param y y-coordinate of the point we are looking for
     * @return the square that contains the given point
     */
    public Square getSquareFromCoordinates(double x, double y) {
        int numLine = (int) ((this.yMax - y) / this.elemSize);
        int numColumn = (int) ((x - this.xMin) / this.elemSize);

        if (numColumn < 0 || numLine < 0 || numColumn >= nbCols || numLine >= nbLines) {
            return null;
        }

        return this.grid[numLine][numColumn];
    }

    /**
     * Return the square located at the given line and column, or null if no
     * such square exists.
     *
     * @param numLine
     * @param numCol
     * @return
     */
    public final Square getSquare(int numLine, int numCol) {
        try {
            return grid[numLine][numCol];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public ArrayList<Square> getSquares() {
        ArrayList<Square> result = new ArrayList<>();
        for (int i = 0; i < nbLines; i++) {
            for (int j = 0; j < nbCols; j++) {
                result.add(grid[i][j]);
            }
        }
        return result;
    }

    /**
     * Place squares of the current tool type in the region defined by the
     * click/release.
     *
     * @param xLeft
     * @param yBottom
     * @param xRight
     * @param yTop
     * @param tool
     */
    public void placeSquares(double xLeft, double yBottom, double xRight, double yTop, TerrainTool tool) {

        int numLineTop = (int) (yTop / this.elemSize);
        int numColumnLeft = (int) (xLeft / this.elemSize);
        int numLineBottom = (int) (yBottom / this.elemSize);
        int numColumnRight = (int) (xRight / this.elemSize);

        // Special cases when the click happens outside the grid
        if (xLeft < 0) {
            numColumnLeft--;
        }
        if (xRight < 0) {
            numColumnRight--;
        }
        if (yBottom > nbLines) {
            numLineBottom--;
        }
        if (yTop > nbLines) {
            numLineTop--;
        }

        int bottomLine = Math.max(numLineBottom, numLineTop);
        int topLine = Math.min(numLineBottom, numLineTop);
        int leftCol = Math.min(numColumnLeft, numColumnRight);
        int rightCol = Math.max(numColumnLeft, numColumnRight);

        for (int line = topLine; line <= bottomLine; line++) {
            for (int col = leftCol; col <= rightCol; col++) {
                placeOneSquare(line, col, tool);
            }
        }
    }

    public void placeOneSquare(int line, int col, TerrainTool tool) {
        Square newSquare;
        switch (tool) {
        case HOLE:
            newSquare = new Hole((col + 0.5) * elemSize,
                    (nbLines - 0.5 - line) * elemSize, elemSize);
            break;
        case WALL:
            newSquare = new Wall((col + 0.5) * elemSize,
                    (nbLines - 0.5 - line) * elemSize, elemSize);
            break;
        case INPUT:
            newSquare = new Input((col + 0.5) * elemSize,
                    (nbLines - 0.5 - line) * elemSize, elemSize);
            break;
        case OUTPUT:
            newSquare = new Output((col + 0.5) * elemSize,
                    (nbLines - 0.5 - line) * elemSize, elemSize);
            break;
        case WORKER:
            // Do not create a new square.
            newSquare = getSquare(line, col);
            if (newSquare != null) {
                this.addNewWorker(newSquare);
            }
            break;
        case GROUND:
        default: // Default is ground.
            newSquare = new Ground((col + 0.5) * elemSize,
                    (nbLines - 0.5 - line) * elemSize, elemSize);
            break;
        }
        setSquare(newSquare, line, col);
    }

    public double getWidth() {
        return this.nbCols;
    }

    public int getNbLines() {
        return this.nbLines;
    }

    public int getNbColumns() {
        return this.nbCols;
    }

    /**
     * Get the size of an individual square.
     *
     * @return the physical size of the square
     */
    public double getElemSize() {
        return elemSize;
    }

    /**
     * Add a single worker to each appropriate square. A worker is added to each
     * square that lies at leas partly inside the rectangle defined by the given
     * coordinates
     *
     * @param xLeft the left side of the rectangle
     * @param yBottom the bottom side of the rectangle
     * @param xRight the right side of the rectangle
     * @param yTop the top side of the rectangle
     */
    public void placeWorkers(double xLeft, double yBottom, double xRight, double yTop) {

        int numLineTop = (int) (yTop / this.elemSize);
        int numColumnLeft = (int) (xLeft / this.elemSize);
        int numLineBottom = (int) (yBottom / this.elemSize);
        int numColumnRight = (int) (xRight / this.elemSize);

        // Special cases when the click happens outside the grid
        if (xLeft < 0) {
            numColumnLeft--;
        }
        if (xRight < 0) {
            numColumnRight--;
        }
        if (yBottom > nbLines) {
            numLineBottom--;
        }
        if (yTop > nbLines) {
            numLineTop--;
        }

        int bottomLine = Math.max(numLineBottom, numLineTop);
        int topLine = Math.min(numLineBottom, numLineTop);
        int leftCol = Math.min(numColumnLeft, numColumnRight);
        int rightCol = Math.max(numColumnLeft, numColumnRight);

        for (int line = topLine; line <= bottomLine; line++) {
            for (int col = leftCol; col <= rightCol; col++) {
                addNewWorker(getSquare(line, col));
            }
        }
    }

    public void addNewWorker(Square square) {

        Worker newWorker = new Worker();
        square.receiveWorker(newWorker);

        newWorker.addObserver(this);

        Notification n = new Notification("newWorker", newWorker);
        notifyObservers(n);
    }

    public void addNewWorker(int line, int col) {
        Square s = this.getSquare(line, col);
        addNewWorker(s);
    }

    private Square findWorker(Worker w) {
        for (Square s : getSquares()) {
            if (s.containsWorker(w)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Search and return the worker that has the given ID, if it exists.
     *
     * @param id the requested ID
     * @return the worker that has this ID, or null if it does not exist.
     */
    private Worker findWorker(int id) {
        for (Square s : getSquares()) {
            Worker w = s.getWorker(id);
            if (w != null) {
                return w;
            }
        }
        return null;
    }

    /**
     * Move the given worker from its current position to its required position.
     * If the required position already contains a worker, the two workers are
     * swapped.
     *
     * @param w the worker that includes its own movement heading.
     */
    private void moveWorker(Worker w, String direction) {

        Square startPoint = findWorker(w);
        Square endPoint;

        int startLine = this.findLine(startPoint);
        int startCol = this.findColumn(startPoint);

        // Find the arrival square.
        int dLine = 0;
        int dCol = 0;
        switch (direction) {
        case "N":
            dLine--;
            break;
        case "S":
            dLine++;
            break;
        case "E":
            dCol++;
            break;
        case "W":
            dCol--;
            break;
        default:
            break;
        }
        int endLine = startLine + dLine;
        int endCol = startCol + dCol;
        endPoint = this.getSquare(endLine, endCol);
        if (endPoint != null && !endPoint.containsWorker()) {
            startPoint.removeWorker();
            endPoint.receiveWorker(w);
        }

        w.setCurrentAddress(w.getCurrentAddress() + 1);
        Notification n = new Notification("TerrainRepaint", null);
        notifyObservers(n);
    }

    /**
     * The worker picks up the data block in the selected direction.
     *
     * @param w the worker that includes its own movement heading.
     */
    private void pickup(Worker w, String direction) {

        Square workerSquare = findWorker(w);
        Square pickupPoint;

        int startLine = this.findLine(workerSquare);
        int startCol = this.findColumn(workerSquare);

        // Find the arrival square.
        int dLine = 0;
        int dCol = 0;
        switch (direction) {
        case "NORTH":
            dLine--;
            break;
        case "SOUTH":
            dLine++;
            break;
        case "EAST":
            dCol++;
            break;
        case "WEST":
            dCol--;
            break;
        default:
            break;
        }
        int pickupLine = startLine + dLine;
        int pickupColumn = startCol + dCol;

        pickupPoint = getSquare(pickupLine, pickupColumn);
        if (pickupPoint != null && pickupPoint.containsDataCube() && !w.hasDataCube()) {
            DataCube cube = pickupPoint.removeDataCube();
            w.setDataCube(cube);
        }
        w.setCurrentAddress(w.getCurrentAddress() + 1);
        Notification n = new Notification("TerrainRepaint", null);
        notifyObservers(n);
    }

    /**
     * The worker drops the data block at its current position, only if the
     * square does not already hold a datab block.
     *
     * @param w the worker that includes its own movement heading.
     */
    private void drop(Worker w) {
//        System.out.println("            Dropping");

        Square workerSquare = findWorker(w);

        if (!(workerSquare.containsDataCube())) {
            // Actually drop the cube
            workerSquare.addDataCube(w.removeDataCube());
        }

        w.setCurrentAddress(w.getCurrentAddress() + 1);

        Notification n = new Notification("TerrainRepaint", null);
        notifyObservers(n);
    }

    /**
     * Place the given square at the required position. If the position is
     * outside the grid, do nothing.
     *
     * @param newSquare the future new square added to the grid
     * @param line
     * @param col
     */
    public void setSquare(Square newSquare, int line, int col) {
        if (line >= 0 && col >= 0 && line < nbLines && col < nbCols) {
            this.grid[line][col] = newSquare;
        }
        // Set the geographical coordinates of the square
        newSquare.xCenter = (col + 0.5) * elemSize;
        newSquare.yCenter = (nbLines - 0.5 - line);
    }

    /**
     * Mark the squares as selected or not.
     *
     */
    @Override
    public void selectContent() {

    }

    public void setSelected(int numLine, int numCol) {
        this.setSelected(numLine, numCol, true);
    }

    public void setSelected(int numLine, int numCol, boolean selected) {
        try {
            grid[numLine][numCol].setSelected(selected);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Override
    public void unselectEverything() {
        for (int line = 0; line < nbLines; line++) {
            for (int col = 0; col < nbCols; col++) {
                grid[line][col].setSelected(false);
            }
        }
    }

    public void moveSelection(double x, double y) {

    }

    /* As an observer, we get notified when the script is changed. */
    @Override
    public void update(Notification notif) {

        nbCallsUpdate++;

        Worker currentWorker;
        int newAddress;

        switch (notif.getName()) {
        case "WorkerMove": // TODO replace this with MoveInstruction.getName();
            String direction = notif.getOptions();
            this.moveWorker((Worker) notif.getObject(), direction);
            break;
        case "WorkerPickup":
            this.pickup((Worker) notif.getObject(), notif.getOptions());
            break;
        case "WorkerDrop":
            this.drop((Worker) notif.getObject());
            break;
        case "InstructionMove":
            System.out.println("TerrainModel: receive Move " + notif.getOptions());
            notifList.add(notif);
            break;
        case "TerrainApplyInstructions":
            applyNotifications();
            break;
        case "JumpInstruction":
            currentWorker = (Worker) notif.getObject();
            newAddress = Integer.parseInt(notif.getOptions());
            currentWorker.setCurrentAddress(newAddress);
            break;
        case "NoOperation":
            currentWorker = (Worker) notif.getObject();
            newAddress = currentWorker.getCurrentAddress() + 1;
            currentWorker.setCurrentAddress(newAddress);
            break;
        case "InstructionIf":
            System.out.println("\nTerrainModel receives If notification with options " + notif.getOptions());

            String options = notif.getOptions();
            int rank = 0;
            String optionList[] = options.split("_");

            currentWorker = (Worker) notif.getObject();
            Square s = findWorker(currentWorker);
            int col = findColumn(s);
            int line = findLine(s);

            char firstDirection = optionList[0].charAt(0);
            Square targetSquare = this.getNeighbor(line, col, firstDirection);

            Notification reply = new Notification("IfReply", currentWorker);

            String squareClass;
            int squareValue;
            int workerId;

            if (targetSquare == null) {
                System.out.println("target square is null.");
                squareClass = "null";
                squareValue = Integer.MIN_VALUE;
                workerId = -1;
            } else {
                // Get rid of "class " in "class Ground":
                squareClass = targetSquare.getClass().toString().substring(6);
                System.out.println("targetSquare: " + targetSquare);
                squareValue = targetSquare.getValue();
                System.out.println("after targetSquare.getValue(): " + squareValue);
                workerId = targetSquare.getWorkerId();
                System.out.println("after getWorkerId(): " + workerId);
            }

            reply.addOption(squareClass + " " + squareValue + " " + workerId);

            if (workerId != -1) {
                Worker coWorker = findWorker(workerId);
                if (coWorker.hasDataCube()) {
                    reply.addOption(coWorker.getCubeValue() + "");
                } else {
                    reply.addOption("first_coworker_no_cube");
                }
            } else {
                reply.addOption("first_coworker_no_cube");
            }

            if (optionList.length > 1) {
                if (optionList[1].length() > 0) {
                    char secondDirection = optionList[1].charAt(0);
                    Square secondTargetSquare = this.getNeighbor(line, col, secondDirection);

                    String secondSquareClass;
                    int secondSquareValue;
                    int secondWorkerId;

                    if (secondTargetSquare == null) {
                        System.out.println("second target square is null");
                        secondSquareClass = "null";
                        secondSquareValue = Integer.MIN_VALUE;
                        secondWorkerId = -1;
                    } else {
                        secondSquareClass = secondTargetSquare.getClass().toString().substring(6);
                        System.out.println("secondTargetSquare: " + secondTargetSquare);
                        secondSquareValue = secondTargetSquare.getValue();
                        secondWorkerId = secondTargetSquare.getWorkerId();
                    }
                    reply.addOption(secondSquareClass + " " + secondSquareValue + " " + secondWorkerId);

                    if (secondWorkerId != -1) {
                        Worker secondCoWorker = findWorker(secondWorkerId);
                        if (secondCoWorker.hasDataCube()) {
                            reply.addOption(secondCoWorker.getCubeValue() + "");
                        } else {
                            reply.addOption("second_coworker_no_cube");
                        }
                    } else {
                        reply.addOption("second_coworker_no_cube");
                    }
                }
            }

            System.out.println("Created notification with options: " + reply.getOptions());

//            if (isInteger(optionList[2])) {
//                // First possibility: we compare the square against a numeric value.
//                int number = Integer.valueOf(optionList[2]);
//                if (targetSquare.getValue() == number) {
//                    reply = new Notification("IfReply", true);
//                } else {
//                    reply = new Notification("IfReply", false);
//                }
//            } else if (isDirection(optionList[2])) {
//                // Second possibility: we compare the square against another square.
//
//            } else if (isSquareType(optionList[2])) {
//                // Third possibility: we compare the square against a type.
//            } else {
//                // Invalid comparison.
//            }
//            String replyOptions;
//            if (targetSquare == null) {
//                replyOptions = "out";
//            } else {
//                replyOptions = targetSquare.getClass().toString() + " " + currentWorker.getSerial();
//                if (targetSquare.containsDataCube()) {
//                    replyOptions += " " + targetSquare.getDataCube().getValue();
//                } else {
//                    replyOptions += " _";
//                }
//                if (targetSquare.containsWorker()) {
//                    replyOptions += " " + targetSquare.getWorkerId();
//                } else {
//                    replyOptions += " -1";
//                }
//            }
//            Notification reply = new Notification("IfReply", currentWorker, replyOptions);
//            System.out.println("Terrain sends back a notification \"IfReply\" with options <" + replyOptions + ">");
//            System.out.println("Options are: square class; current worker's serial; value of cube or '_'; other worker id or -1");
            this.notifyObservers(reply);
            break;

        case "workerToAddress":

            branchWorker(notif);
            break;
        default:
            break;
        }
    }

    private void applyNotifications() {

        for (Notification n : notifList) {
            switch (n.getName()) {
            case "InstructionMove":
                System.out.println("TerrainModel: other instruction move " + n.getName() + ", " + n.getOptions());
                this.moveWorker((Worker) n.getObject(), n.getOptions());
            default:
                System.out.println("    Terrain.applyNotification: other notification: " + n.getName());
                break;
            }
        }

        notifList.clear();
    }

    /* As an observable object, we notify the listeners when the TerrainModel changes. */
    @Override
    public void addObserver(Observer newObserver) {
        this.observersList.add(newObserver);
    }

    @Override
    public void removeObserver(Observer obs) {
        this.observersList.remove(obs);
    }

    @Override
    public void notifyObservers(Notification notif) {
//        System.out.println("TerrainModel is updating its " + observersList.size() + " observers.");
        for (Observer obs : observersList) {
//            System.out.println("    Notifying observer " + obs);
            obs.update(notif);
        }
    }

    /**
     * Find in which line a given square is.
     *
     * @param s the required square
     * @return the line number if the square is in the Terrain, -1 if it is not.
     */
    private int findLine(Square s) {
        int numLine = -1;
        for (int line = 0; line < nbLines; line++) {
            for (int col = 0; col < nbCols; col++) {
                if (this.grid[line][col].equals(s)) {
                    // Found the square at this line.
                    numLine = line;
                }
            }
        }
        return numLine;
    }

    /**
     * Find in which column a given square is.
     *
     * @param s the required square
     * @return the column number if the square is in the Terrain, -1 if it is
     * not.
     */
    private int findColumn(Square s) {
        int numColumn = -1;
        for (int line = 0; line < nbLines; line++) {
            for (int col = 0; col < nbCols; col++) {
                if (this.grid[line][col].equals(s)) {
                    // Found the square at this line.
                    numColumn = col;
                }
            }
        }
        return numColumn;
    }

    /**
     * Count how many cubes are either in squares or carried by workers.
     *
     * @return the number of cubes.
     */
    public int getNbCubes() {
        int count = 0;
        for (Square[] tab : grid) {
            for (Square s : tab) {
                count += s.getNbCubes();
            }
        }
        return count;
    }

    public void evaluateIf(Worker w, String conditions) {
//        System.out.println("TerrainModel.evaluateIf(" + conditions + ");");
    }

    private void branchWorker(Notification notif) {

        String options = notif.getOptions();
        String tab[] = options.split(" ");
        int workerID = Integer.parseInt(tab[0]);
        Worker w = this.findWorker(workerID);
        int newAddress;
        if (tab[1].equals("+1")) {
            // Branch to the next instruction
            newAddress = w.getCurrentAddress() + 1;
            w.setCurrentAddress(newAddress);
        } else {
            // Branch to the specified instruction
            newAddress = Integer.parseInt(tab[1]);
            w.setCurrentAddress(newAddress);
        }

    }

    /**
     * Replace all the worker at the first instruction od the script
     * so that they are ready to start working again
     */
    public void resetWorkers() {
        for (Square[] list : grid) {
            for (Square s : list) {
                s.resetWorker();
            }
        }
    }

    /**
     * Find and return the square that is the neighbor of (line, col) in the
     * given direction
     */
    private Square getNeighbor(int line, int col, char direction) {

        switch (direction) {
        case 'N':
            line = line - 1;
            break;
        case 'S':
            line = line + 1;
            break;
        case 'E':
            col = col + 1;
            break;
        case 'W':
            col = col - 1;
            break;
        case 'C':
            break;
        default:
            break;
        }
        return this.getSquare(line, col);
    }

    /**
     * Tell if a String represents an integer.
     *
     * @param s the integer candidate
     * @return true when the String actually represents an int, false otherwise
     */
    private boolean isInteger(String s) {
        try {
            int val = Integer.valueOf(s);
        } catch (NumberFormatException e) {
            // Not a String representing a number
            return false;
        }
        return true;
    }
}
