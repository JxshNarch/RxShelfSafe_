import database.DataStore;
import frame.LoginScreen;
import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.JButton;
/**
 * RxShelfSafe - Main entry point of the program.
 */
public class RxShelfSafe {
    public static void main(String[] args) {

        //Make all buttons show a hand cursor when the mouse is over them
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                if (e.getSource() instanceof JButton) {
                    ((JButton) e.getSource()).setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
        // Load all data from the text file
        DataStore data = new DataStore();
        // Show the login window
        LoginScreen login = new LoginScreen(data);
        login.setVisible(true);
    }
}
