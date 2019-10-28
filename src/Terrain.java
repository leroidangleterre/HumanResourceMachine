import java.awt.Color;
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

    public Terrain(int nbLines, int nbCols) {
        pause();
        this.nbLines = nbLines;
        this.nbCols = nbCols;
        grid = new Square[nbLines][];
        for (int i = 0; i < nbLines; i++) {
            grid[i] = new Square[nbCols];
            for (int j = 0; j < nbCols; j++) {
                Color color;
                switch ((i + j) % 3) {
                    case 0:
                        color = Color.red;
                        break;
                    case 1:
                        color = Color.blue;
                        break;
                    case 2:
                        color = Color.green;
                        break;
                    default:
                        color = Color.orange;
                        break;
                }
                grid[i][j] = new Square(i, j, 0.8, color);
            }
        }
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
     */
    public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom) {
        for (int i = 0; i < nbLines; i++) {
            for (int j = 0; j < nbLines; j++) {
                grid[i][j].paint(g, panelHeight, x0, y0, zoom);
            }
        }
    }

    public double getWidth() {
        return this.nbCols;
    }

}
