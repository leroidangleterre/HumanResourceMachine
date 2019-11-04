import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class Terrain extends Displayable {

    // The grid that contains squares (walls, ground, ...) and in which workers evolve.
    private Square[][] grid;
    // The dimensions of the grid.
    private int nbCols, nbLines;
    private double elemSize;

    public enum TerrainMode {

        EDITION, PLAY
    }
    private TerrainMode currentMode;

    public enum TerrainTool {

        HOLE, GROUND, WALL, INPUT, OUTPUT, SELECTION
    }
    private TerrainTool currentTool;

    public Terrain(int nbLines, int nbCols) {
        super();

        this.currentMode = TerrainMode.EDITION;
        this.currentTool = TerrainTool.HOLE;

        pause();
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
        this.xMin = this.getSquare(0, 0).getXMin();
        this.xMax = this.getSquare(0, this.nbCols - 1).getXMax();
        this.yMin = this.getSquare(this.nbLines - 1, 0).getYMin();
        this.yMax = this.getSquare(0, 0).getYMax();
    }

    /**
     * Compute the next state of the terrain. Each worker executes one step of
     * the script.
     */
    public void evolve() {
    }

    /**
     * Paint the terrain, the workers, the data blocks.
     *
     * @param g the Graphics where the Displayable will be painted
     * @param panelHeight the height of the panel
     * @param x0 x-offset for the display
     * @param y0 y-offset for the display
     * @param zoom zoom factor
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        super.paint(g, panelHeight, x0, y0, zoom);
        for (int i = 0; i < nbLines; i++) {
            for (int j = 0; j < nbCols; j++) {
                grid[i][j].paint(g, panelHeight, x0, y0, zoom);
            }
        }
    }

    public double getWidth() {
        return this.nbCols;
    }

    /**
     * Switch between Edition and Play modes.
     *
     * @return the newly selected mode
     */
    public TerrainMode switchModes() {
        if (currentMode == TerrainMode.EDITION) {
            currentMode = TerrainMode.PLAY;
        } else {
            currentMode = TerrainMode.EDITION;
        }
        return currentMode;
    }

    /**
     * Set the new tool.
     *
     * @param newTool
     */
    public void setTool(TerrainTool newTool) {
        this.currentTool = newTool;
    }

    /**
     * Action performed when a left click occurs. Usually place or move stuff.
     */
    @Override
    public void receiveLeftClick(double x, double y) {
        super.receiveLeftClick(x, y);
//        System.out.println("Terrain.receiveLeftClick()");
        if (currentTool == TerrainTool.SELECTION) {
            isSelecting = true;
//            System.out.println("IsSelecting becomes true");
        }
    }

    @Override
    public void receiveLeftRelease(double x, double y) {
        super.receiveLeftRelease(x, y);
//        System.out.println("Terrain.receiveLeftRelease()");

        if (currentTool == TerrainTool.SELECTION) {
            isSelecting = false;
            // TODO Select everything that is inside the selection rectangle.
        }
        switch (currentTool) {
            case HOLE:
            case GROUND:
            case INPUT:
            case OUTPUT:
            case WALL:
                this.placeSquares();
                break;
            default:
                break;
        }
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

    public Square getSquare(int numLine, int numCol) {
        return grid[numLine][numCol];
    }

    /**
     * Place squares of the current tool type in the region defined by the
     * click/release.
     */
    public void placeSquares() {

        int numLineClick = (int) ((this.yMax - yClick) / this.elemSize);
        int numColumnClick = (int) ((xClick - this.xMin) / this.elemSize);
        int numLineRelease = (int) ((this.yMax - yRelease) / this.elemSize);
        int numColumnRelease = (int) ((xRelease - this.xMin) / this.elemSize);

        // Special cases when the click happens outside the grid
        if (xClick < 0) {
            numColumnClick--;
        }
        if (xRelease < 0) {
            numColumnRelease--;
        }
        if (yClick > nbLines) {
            numLineClick--;
        }
        if (yRelease > nbLines) {
            numLineRelease--;
        }

        int bottomLine = Math.max(numLineClick, numLineRelease);
        int topLine = Math.min(numLineClick, numLineRelease) + 1;
        int leftCol = Math.min(numColumnClick, numColumnRelease) + 1;
        int rightCol = Math.max(numColumnClick, numColumnRelease);

        for (int line = topLine; line < bottomLine; line++) {
            for (int col = leftCol; col < rightCol; col++) {
                Square newSquare;
                switch (currentTool) {
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
                    case GROUND:
                    default: // Default is ground.
                        newSquare = new Ground((col + 0.5) * elemSize,
                                (nbLines - 0.5 - line) * elemSize, elemSize);
                        break;
                }
                setSquare(newSquare, line, col);
            }
        }
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
}
