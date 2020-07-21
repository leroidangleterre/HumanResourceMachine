package humanresourcemachine;

import astar.AstarComputer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class Window extends JFrame implements KeyListener {

    int WINDOW_WIDTH = 1600;
    int WINDOW_HEIGHT = 1000;

    JSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;
    private static final double SPLIT_PANE_EIGHTY = .8;

    private JPanel buttonsPanel;
    private JPanel controlPanel;

    private AstarComputer astar;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        split = null;

        this.setLayout(new BorderLayout());

        prepareButtonsPanel();

        prepareControlPanel();

        this.pack();

        resetSizes();

        this.addKeyListener(this);

        this.setVisible(true);

        astar = null;
    }

    public Window(MyDefaultComponent leftComponent, MyDefaultComponent rightComponent) {
        this();
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        split.setDividerLocation(SPLIT_PANE_HALF);
        split.add(leftComponent);
        split.add(rightComponent);
        this.add(split, BorderLayout.CENTER);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        if (split != null) {
            split.setResizeWeight(SPLIT_PANE_EIGHTY);
            split.setDividerLocation(SPLIT_PANE_EIGHTY);
        }
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

        String buttonNames[] = {"Selection", "Hole", "Ground", "Input", "Output", "Wall", "Worker", "Datacube", "Move", "Push", "Pickup", "Drop", "Jump", "If", "DeleteInstr"};
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
            newButton.addKeyListener(this);
            buttonsPanel.add(newButton, rank);
            rank++;
        }
        buttonsPanel.addKeyListener(this);
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
        stepButton.addKeyListener(this);

        JButton playPauseButton = new JButton("Play");
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                if (script.isPlaying()) {
                    script.pause();
                    playPauseButton.setText("Play");
                } else {
                    script.play();
                    playPauseButton.setText("Pause");
                }
            }
        });
        playPauseButton.addKeyListener(this);

        JButton playAllButton = new JButton("Play All");
        playAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                script.playAll();
            }
        });
        playAllButton.addKeyListener(this);

        JButton resetAllButton = new JButton("Reset All");
        resetAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                script.load();
                Terrain terrain = (Terrain) split.getLeftComponent();
                terrain.reloadSokoban();

                script.initWorkers(terrain.getWorkers());
                terrain.repaint();
                script.repaint();
            }
        });
        resetAllButton.addKeyListener(this);

        JButton fasterButton = new JButton("+++");
        fasterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                script.increaseSpeed(true);
            }
        });
        fasterButton.addKeyListener(this);

        JButton slowerButton = new JButton("---");
        slowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Script script = (Script) split.getRightComponent();
                script.increaseSpeed(false);
            }
        });
        slowerButton.addKeyListener(this);

        JButton saveScriptButton = new JButton("Save script");
        saveScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).save();
            }
        });
        saveScriptButton.addKeyListener(this);

        JButton loadScriptButton = new JButton("Load script");
        loadScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).load();
            }
        });
        loadScriptButton.addKeyListener(this);
        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Script) split.getRightComponent()).clear();
            }
        });
        clearButton.addKeyListener(this);
        JButton resetWorkersButton = new JButton("Reset workers");
        resetWorkersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Terrain) split.getLeftComponent()).resetWorkers();
                repaint();
            }
        });
        resetWorkersButton.addKeyListener(this);

        controlPanel.add(stepButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(playAllButton);
        controlPanel.add(resetAllButton);
        controlPanel.add(fasterButton);
        controlPanel.add(slowerButton);
        controlPanel.add(saveScriptButton);
        controlPanel.add(loadScriptButton);
        controlPanel.add(resetWorkersButton);
        controlPanel.add(clearButton);

        this.add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        CardinalPoint cardPoint;
        switch (e.getKeyChar()) {
        case '8':
            cardPoint = CardinalPoint.NORTH;
            break;
        case '6':
            cardPoint = CardinalPoint.EAST;
            break;
        case '4':
            cardPoint = CardinalPoint.WEST;
            break;
        case '2':
            cardPoint = CardinalPoint.SOUTH;
            break;
        default:
            cardPoint = CardinalPoint.CENTER;
            break;
        }

        ((Terrain) split.getLeftComponent()).pushWorkers(cardPoint.toString());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void receiveAstarComputer(AstarComputer astarComputer) {
        astar = astarComputer;
    }
}
