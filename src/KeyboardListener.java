import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

    private final MyDefaultComponent panel;

    private static int NB_KL_INSTANCIATED = 0;
    private final int serialNumber;

    public KeyboardListener(MyDefaultComponent p) {
        super();
        this.serialNumber = NB_KL_INSTANCIATED;
        NB_KL_INSTANCIATED++;
//        System.out.println("Creating KeyboardListener " + serialNumber);
        this.panel = p;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        System.out.println("KeyboardListener.keyPressed()");

        if (panel == null) {
            System.out.println("KeyboardListener: panel is null");
        } else {
            switch (e.getKeyChar()) {
            case '0':
//                    System.out.println("KeyboardListener: KeyPressed: resetView");
                panel.resetView();
                break;
            case '4':
                panel.swipe(-1, 0);
                break;
            case '6':
                panel.swipe(+1, 0);
                break;
            case '8':
                panel.swipe(0, +1);
                break;
            case '2':
                panel.swipe(0, -1);
                break;
            case 'p':
//                    panel.togglePlayPause();
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // System.out.println("KeyReleased: ");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // System.out.println("KeyTyped: ");
    }

}
