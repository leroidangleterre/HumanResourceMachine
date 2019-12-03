import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Window extends JFrame {

    int WINDOW_WIDTH = 1600;
    int WINDOW_HEIGHT = 1000;

    JSplitPane split;
    private static final double SPLIT_PANE_HALF = 0.5;

    private JPanel buttonsPanel;

    public Window() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        split = null;

        this.setLayout(new BorderLayout());

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 10));
        prepareButtonsPanel();

        this.add(buttonsPanel, BorderLayout.NORTH);
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

        String buttonNames[] = {"Selection", "Hole", "Ground", "Input", "Worker", "Move", "Pickup", "Drop", "DeleteInstr"};
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
    }
}
