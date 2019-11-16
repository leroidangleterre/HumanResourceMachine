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

    private boolean leftComponentActive;
    private boolean rightComponentActive;
    private boolean rightClickActive;
    private int xMouse;

    public CustomSplitPane(int orientation) {
        super(orientation);
        leftComponentActive = false;
        rightComponentActive = false;
        rightClickActive = false;
        xMouse = 0;

        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // One of the two components will receive the event.
        MyDefaultComponent activeComponent;
        if (leftComponentActive) {
            activeComponent = (MyDefaultComponent) getLeftComponent();
        } else if (rightComponentActive) {
            activeComponent = (MyDefaultComponent) getRightComponent();
        } else {
            if (xMouse < getDividerLocation()) {
                activeComponent = (MyDefaultComponent) getLeftComponent();
            } else {
                activeComponent = (MyDefaultComponent) getRightComponent();
            }
        }

        activeComponent.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void setLeftPanelActive(boolean param) {
        leftComponentActive = param;
    }

    public void setRightPanelActive(boolean param) {
        rightComponentActive = param;
    }

    public void setRightClickActive(boolean param) {
        rightClickActive = param;
    }

    public void setXMouse(int newXMouse) {
        xMouse = newXMouse;
    }

    public void receiveCommand(String s) {
        ((MyDefaultComponent) leftComponent).receiveCommand(s);
        ((MyDefaultComponent) rightComponent).receiveCommand(s);
    }
}
