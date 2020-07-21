package humanresourcemachine;

import astar.AstarComputer;

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

        script.initWorkers(terrain.getWorkers());

        TerrainModel terrainModel = (TerrainModel) terrain.getModel();

        w.receiveAstarComputer(new AstarComputer(terrainModel, (ScriptModel) script.getModel()));

        w.invalidate();
        w.revalidate();
    }
}
