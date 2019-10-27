import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 8;
        int nbCols = 10;
        Terrain terrain = new Terrain(nbLines, nbCols);
        Script script = new Script();

        GraphicPanel leftPanel = new GraphicPanel(terrain);
        GraphicPanel rightPanel = new GraphicPanel(terrain); // TODO: add the Script to the rightPanel
        Window w = new Window(leftPanel, rightPanel);
        leftPanel.repaint();
    }
}
