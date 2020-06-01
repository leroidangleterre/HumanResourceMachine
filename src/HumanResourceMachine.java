/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 12;
        int nbCols = 12;

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

        int line = 0;
        int col = 1;
        ((TerrainModel) (terrain.getModel())).addNewWorker(line, col);
        col = 2;
        ((TerrainModel) (terrain.getModel())).addNewWorker(line, col);

        w.invalidate();
        w.revalidate();
    }
}
