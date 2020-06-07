/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 3;
        int nbCols = 10;

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

        for (int col = 1; col < nbCols - 1; col++) {
            terrainModel.addDatacube(1, col);
        }
        terrainModel.addNewWorker(1, 1);
        terrainModel.setSquare(new Hole(), 1, 0);
        terrainModel.setSquare(new Hole(), 1, 9);

        w.invalidate();
        w.revalidate();
    }
}
