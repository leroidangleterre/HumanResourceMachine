import java.util.Random;

/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static String path = "C:\\Users\\arthu\\Documents\\Programmation\\Java\\HumanResourceMachine";

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

        TerrainModel terrainModel = (TerrainModel) terrain.getModel();

        int line = 8;
        for (int col = 1; col < 14; col++) {
            System.out.println("Adding worker and datacube");
            terrainModel.addNewWorker(line, col);
            terrainModel.addDatacube(line + 1, col);
        }

        w.invalidate();
        w.revalidate();
    }
}
