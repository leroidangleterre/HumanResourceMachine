import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author arthurmanoha
 */
public class Terrain extends MyDefaultComponent implements Observer {

    protected TerrainTool currentTool;

    public Terrain(int nbLines, int nbCols) {

        model = new TerrainModel(nbLines, nbCols);
        currentTool = TerrainTool.SELECTION;
    }

    /**
     * Compute the next state of the terrain. Each worker executes one step of
     * the script.
     */
    public void evolve() {
    }

    @Override
    public void paintComponent(Graphics g) {
        eraseAll(g);
        this.paint(g, panelHeight, x0, y0, zoom);
        super.paintComponent(g);
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

        for (Square s : ((TerrainModel) model).getSquares()) {
            s.paint(g, panelHeight, x0, y0, zoom);
        }
    }

    /**
     * Set the new tool.
     *
     * @param newTool
     *
     */
    public void setTool(TerrainTool newTool) {
        this.currentTool = newTool;
    }

    /**
     * Set the new tool.
     *
     * @param s the String in capital letters (ex: "HOLE", or "SELECTION")
     */
    public void setTool(String s) {
        switch (s) {
            case "SELECTION":
                this.setTool(TerrainTool.SELECTION);
                break;
            case "GROUND":
                this.setTool(TerrainTool.GROUND);
                break;
            case "HOLE":
                this.setTool(TerrainTool.HOLE);
                break;
            case "INPUT":
                this.setTool(TerrainTool.INPUT);
                break;
            case "OUTPUT":
                this.setTool(TerrainTool.OUTPUT);
                break;
            case "WALL":
                this.setTool(TerrainTool.WALL);
                break;
            case "WORKER":
                this.setTool(TerrainTool.WORKER);
                break;
            default:
            // Keep the current tool unchanged.
        }
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
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = true;
            isSelecting = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = true;
        }
    }

    /**
     * Action performed when a left click release is received.
     *
     * @param e The event recceived by the panel
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        TerrainModel tModel = ((TerrainModel) model);

        double elemSize = tModel.getElemSize();

        xRelease = e.getX();
        yRelease = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickIsActive = false;

            double yClickInTerrain = (panelHeight - yClick - y0) / zoom;
            double yReleaseInTerrain = (panelHeight - yRelease - y0) / zoom;

            double xClickInTerrain = (xClick - x0) / zoom;
            double xReleaseInTerrain = (xRelease - x0) / zoom;

            int numLineClick = (int) ((model.getYMax() - yClickInTerrain) / elemSize);
            int numColumnClick = (int) ((xClickInTerrain - model.getXMin()) / elemSize);
            int numLineRelease = (int) ((model.getYMax() - yReleaseInTerrain) / elemSize);
            int numColumnRelease = (int) ((xReleaseInTerrain - model.getYMin()) / elemSize);

            int bottomLine = Math.max(numLineClick, numLineRelease);
            int topLine = Math.min(numLineClick, numLineRelease);
            int leftCol = Math.min(numColumnClick, numColumnRelease);
            int rightCol = Math.max(numColumnClick, numColumnRelease);

            if (currentTool == TerrainTool.SELECTION && isSelecting) {
                tModel.unselectEverything();
            }
            // Click in a square and release in another one: regular selection by rectangle.
            for (int line = topLine; line <= bottomLine; line++) {
                for (int col = leftCol; col <= rightCol; col++) {
                    switch (currentTool) {
                        case SELECTION:
                            if (isSelecting) {
                                tModel.setSelected(line, col);
                            }
                            break;
                        case HOLE:
                        case GROUND:
                        case INPUT:
                        case OUTPUT:
                        case WALL:
                            tModel.placeOneSquare(line, col, currentTool);
                            break;
                        case WORKER:
                            tModel.placeOneSquare(line, col, currentTool);
                            break;
                        default:
                            break;
                    }
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            wheelClickIsActive = false;
        }
        isSelecting = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        repaint();
    }

    @Override
    public void receiveCommand(String s) {
        setTool(s);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    // As an Observer, we receive info from Observable objects (the Terrain);
    @Override
    public void update(Notification n) {
        switch (n.getName()) {
            case "TerrainRepaint":
                repaint();
                break;
            default:
                break;
        }
    }

    /**
     * Count how many cubes are either in squares or carried by workers.
     *
     * @return the number of cubes.
     */
    public int getNbCubes() {
        return ((TerrainModel) model).getNbCubes();
    }
}
