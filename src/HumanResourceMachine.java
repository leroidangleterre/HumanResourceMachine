/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 8;
        int nbCols = 12;

        int nbInstructions = 7;

        Terrain terrain = new Terrain(nbLines, nbCols);
        Script script = new Script();

        ((TerrainModel) (terrain.getModel())).addObserver(script);

        ((ScriptModel) (script.getModel())).addObserver((Observer) (terrain.getModel()));

        ((ScriptModel) (script.getModel())).addObserver(script);

        ((TerrainModel) (terrain.getModel())).addObserver(terrain);

        MyDefaultComponent leftPanel = terrain;
        MyDefaultComponent rightPanel = script;
        Window w = new Window(leftPanel, rightPanel);

        for (int i = 0; i < nbInstructions; i++) {
            Instruction inst;
            if (i == 2) {
                inst = new PickupInstruction(CardinalPoint.SOUTH);
            } else if (i == 5) {
                inst = new DropInstruction();
            } else {
                if (i < 2) {
                    inst = new MoveInstruction(CardinalPoint.SOUTH);
                } else {
                    inst = new MoveInstruction(CardinalPoint.EAST);
                }
            }
            script.addInstruction(inst);
        }

        int line = 0;
        int col = 1;
        ((TerrainModel) (terrain.getModel())).addNewWorker(line, col);

        w.revalidate();
    }
}
