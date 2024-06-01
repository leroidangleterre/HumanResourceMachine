/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static String path = "C:\\Users\\arthu\\Documents\\Programmation\\Java\\HumanResourceMachine";

    public static void main(String args[]) {

        int nbLines = 10;
        int nbCols = 30;

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

        terrainModel.addNewWorker(2, 3);
//        terrainModel.addNewWorker(2, 8);

        int firstCol = 1;
        int lastCol = 28;
        for (int col = firstCol; col < lastCol; col++) {
            terrainModel.addDatacube(2, col);
        }
        terrainModel.setSquare(new Wall(), 2, firstCol);
        terrainModel.setSquare(new Wall(), 2, lastCol);

        w.invalidate();
        w.revalidate();
    }
}
