import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * The window contains the terrain (as its split's left component) and the
 * script (right component).
 *
 * @author arthurmanoha
 */
public class Window extends JFrame {

    int WINDOW_WIDTH = 1600;
    int WINDOW_HEIGHT = 1000;

    JSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;

    private JPanel buttonsPanel;
    private JPanel controlPanel;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        split = null;

        this.setLayout(new BorderLayout());

        prepareButtonsPanel();

        prepareControlPanel();

        this.pack();

        resetSizes();

        this.setVisible(true);
    }

    public Window(MyDefaultComponent leftComponent, MyDefaultComponent rightComponent) {
        this();
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(SPLIT_PANE_HALF);
        split.add(leftComponent);
        split.add(rightComponent);
        this.add(split, BorderLayout.CENTER);
        resetSizes();
        repaint();
    }

    public void resetSizes() {

        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setLocationRelativeTo(null);

        if (split != null) {
            split.setResizeWeight(SPLIT_PANE_HALF);
            split.setDividerLocation(SPLIT_PANE_HALF);
        }
    }

    /**
     * Create the control buttons and place them on their panel.
     *
     */
    private void prepareButtonsPanel() {

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 10));

        String buttonNames[] = {"Selection", "Hole", "Ground", "Input", "Worker", "Datacube", "Move", "Pickup", "Drop", "Jump", "If", "DeleteInstr"};
        int rank = 0;
        for (String text : buttonNames) {
            JButton newButton = new JButton(text);
            newButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ((MyDefaultComponent) (split.getLeftComponent())).receiveCommand(text.toUpperCase());
                    ((MyDefaultComponent) (split.getRightComponent())).receiveCommand(text.toUpperCase());
                }
            });
            buttonsPanel.add(newButton, rank);
            rank++;
        }
        this.add(buttonsPanel, BorderLayout.NORTH);
    }

    /**
     * Create the control buttons and place them on the control panel.
     *
     */
    private void prepareControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                script.step();
            }
        });

        JButton saveScriptButton = new JButton("Save script");
        saveScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).save();
            }
        });

        JButton loadScriptButton = new JButton("Load script");
        loadScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).load();
            }
        });
        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).clear();
            }
        });

        JButton resetWorkersButton = new JButton("Reset workers");
        resetWorkersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Terrain) split.getLeftComponent()).resetWorkers();
                repaint();
            }
        });

        controlPanel.add(stepButton);
        controlPanel.add(saveScriptButton);
        controlPanel.add(loadScriptButton);
        controlPanel.add(resetWorkersButton);
        controlPanel.add(clearButton);

        this.add(controlPanel, BorderLayout.SOUTH);
    }
}
