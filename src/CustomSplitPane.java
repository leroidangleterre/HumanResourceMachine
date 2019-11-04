import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JSplitPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class CustomSplitPane extends JSplitPane implements KeyListener {

    private boolean leftPanelActive;
    private boolean rightPanelActive;
    private boolean rightClickActive;
    private int xMouse;

    public CustomSplitPane(int orientation) {
        super(orientation);
        leftPanelActive = false;
        rightPanelActive = false;
        rightClickActive = false;
        xMouse = 0;

        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        System.out.println("CustomSplit: keyTyped");
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("CustomSplit: keyPressed");

        // One of the two panels will receive the event.
        GraphicPanel activePanel;
        if (leftPanelActive) {
            activePanel = (GraphicPanel) getLeftComponent();
        } else if (rightPanelActive) {
            activePanel = (GraphicPanel) getRightComponent();
        } else {
            if (xMouse < getDividerLocation()) {
                activePanel = (GraphicPanel) getLeftComponent();
            } else {
                activePanel = (GraphicPanel) getRightComponent();
            }
        }

        activePanel.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println("CustomSplit: keyReleased");
    }

    public void setLeftPanelActive(boolean param) {
        leftPanelActive = param;
    }

    public void setRightPanelActive(boolean param) {
        rightPanelActive = param;
    }

    public void setRightClickActive(boolean param) {
        rightClickActive = param;
    }

    public void setXMouse(int newXMouse) {
        xMouse = newXMouse;
    }
}
