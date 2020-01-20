import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public TerrainModel(int nbLines, int nbCols) {

        this.nbLines = nbLines;
        this.nbCols = nbCols;
        grid = new Square[nbLines][];
        elemSize = 1;
        for (int i = 0; i < nbLines; i++) {
            grid[i] = new Square[nbCols];
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = new Ground((j + 0.5) * elemSize, (nbLines - 0.5 - i) * elemSize, elemSize);
                if (j == 0) {
                    grid[i][j].createDataCube(10 * i + j);
                }
            }
        }
        this.xMin = this.getSquare(0, 0).getXMin();
        this.xMax = this.getSquare(0, this.nbCols - 1).getXMax();
        this.yMin = this.getSquare(this.nbLines - 1, 0).getYMin();
        this.yMax = this.getSquare(0, 0).getYMax();

        observersList = new ArrayList<>();
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
     * Move the given worker from its current position to its required position.
     * If the required position already contains a worker, the two workers are
     * swapped.
     *
     * @param w the worker that includes its own movement heading.
     */
    private void moveWorker(Worker w) {

        Square startPoint = findWorker(w);
        Square endPoint;

        int startLine = this.findLine(startPoint);
        int startCol = this.findColumn(startPoint);

        // Find the arrival square.
        int dLine = 0;
        int dCol = 0;
        switch (w.getDirection()) {
            case NORTH:
                dLine--;
                break;
            case SOUTH:
                dLine++;
                break;
            case EAST:
                dCol++;
                break;
            case WEST:
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

        Notification n = new Notification("TerrainRepaint", null);
        notifyObservers(n);
    }

    /**
     * The worker picks up the data block in the selected direction.
     *
     * @param w the worker that includes its own movement heading.
     */
    private void pickup(Worker w) {

        Square workerSquare = findWorker(w);
        Square pickupPoint;

        int startLine = this.findLine(workerSquare);
        int startCol = this.findColumn(workerSquare);

        // Find the arrival square.
        int dLine = 0;
        int dCol = 0;
        switch (w.getDirection()) {
            case NORTH:
                dLine--;
                break;
            case SOUTH:
                dLine++;
                break;
            case EAST:
                dCol++;
                break;
            case WEST:
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

        Square workerSquare = findWorker(w);

        if (!(workerSquare.containsDataCube())) {
            // Actually drop the cube
            workerSquare.addDataCube(w.removeDataCube());
        }
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

        switch (notif.getName()) {
            case "ScriptStep":
                break;
            case "WorkerMove":
                this.moveWorker((Worker) notif.getObject());
                break;
            case "WorkerPickup":
                this.pickup((Worker) notif.getObject());
                break;
            case "WorkerDrop":
                this.drop((Worker) notif.getObject());
                break;
            default:
                break;
        }

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
        for (Observer obs : observersList) {
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
}
