import java.util.Random;

/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 15;
        int nbCols = 15;

        Terrain terrain = new Terrain(nbLines, nbCols);
        Script script = new Script();

        ((TerrainModel) (terrain.getModel())).addObserver(script);

        ((ScriptModel) (script.getModel())).addObserver((Observer) (terrain.getModel()));

        ((ScriptModel) (script.getModel())).addObserver(script);

        ((TerrainModel) (terrain.getModel())).addObserver(terrain);

        MyDefaultComponent leftPanel = terrain;
        MyDefaultComponent rightPanel = script;
        Window w = new Window(leftPanel, rightPanel);

        script.load();

        for (int line = 0; line < nbLines; line++) {
            for (int col = 0; col < nbCols; col++) {
                if (new Random().nextInt(10) <= 1) {
                    ((TerrainModel) (terrain.getModel())).addNewWorker(line, col);
                }
                if (new Random().nextInt(10) <= 2) {
                    ((TerrainModel) (terrain.getModel())).addDatacube(line, col);
                }
            }
        }
        w.invalidate();
        w.revalidate();
    }
}
