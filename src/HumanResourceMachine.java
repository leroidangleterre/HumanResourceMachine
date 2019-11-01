import javax.swing.JFrame;

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

        GraphicPanel leftPanel = new GraphicPanel(terrain);
        GraphicPanel rightPanel = new GraphicPanel(script);
        Window w = new Window(leftPanel, rightPanel);
        MenuBar menubar = new MenuBar(terrain, script);
        w.setJMenuBar(menubar);

        for (int i = 0; i < 15; i++) {
            script.addInstruction(new Instruction());
        }
        leftPanel.repaint();
        rightPanel.repaint();

        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.pack();
        w.setVisible(true);
        w.resetSplitPosition();
    }
}
