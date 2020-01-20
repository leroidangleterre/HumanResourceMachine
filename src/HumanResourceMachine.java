/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 8;
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

        script.addInstruction(new MoveInstruction(CardinalPoint.SOUTH));
        script.addInstruction(new MoveInstruction(CardinalPoint.SOUTH));
        script.addInstruction(new MoveInstruction(CardinalPoint.EAST));
        script.addInstruction(new MoveInstruction(CardinalPoint.EAST));
        script.addInstruction(new MoveInstruction(CardinalPoint.NORTH));
        script.addInstruction(new MoveInstruction(CardinalPoint.NORTH));
        script.addInstruction(new JumpInstruction());

        int line = 0;
        int col = 1;
        ((TerrainModel) (terrain.getModel())).addNewWorker(line, col);

        w.revalidate();
    }
}
