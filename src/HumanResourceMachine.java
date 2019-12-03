/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 8;
        int nbCols = 12;

        int nbInstructions = 4;

        Terrain terrain = new Terrain(nbLines, nbCols);
        Script script = new Script();

        MyDefaultComponent leftPanel = terrain;
        MyDefaultComponent rightPanel = script;
        Window w = new Window(leftPanel, rightPanel);

        for (int i = 0; i < nbInstructions; i++) {
            script.addInstruction(new Instruction());
        }
    }
}
