/**
 * Main class for the Human Resource Machine game.
 *
 */
public class HumanResourceMachine {

    public static void main(String args[]) {

        int nbLines = 8;
        int nbCols = 12;

        int nbInstructions = 12;

        Terrain terrain = new Terrain(nbLines, nbCols);
        Script script = new Script();

        MyDefaultComponent leftPanel = terrain;
        MyDefaultComponent rightPanel = script;
        Window w = new Window(leftPanel, rightPanel);

        for (int i = 0; i < nbInstructions; i++) {
            Instruction inst;
            if (3 * (i / 3) == i) {
                inst = new MoveInstruction();
            } else {
                inst = new Instruction();
            }
            script.addInstruction(inst);
        }

        w.revalidate();
    }
}
