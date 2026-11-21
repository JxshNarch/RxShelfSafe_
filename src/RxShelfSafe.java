import database.DataStore;
import frame.LoginScreen;
import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * RxShelfSafe - Main entry point of the program.
 * 
 * This is a simplified version of the original project.
 * Made to look like a 2nd year BSIT student project that just learned
 * Data Structures (especially Linked Lists).
 * 
 * What the program does:
 * - Tracks medicine batches and their expiration dates
 * - Uses a Singly Linked List (BatchList) to store the batches
 * - Staff can request removal of bad batches
 * - Admin can approve/delete or cancel those requests
 * - Simple text-file storage (no database needed)
 * 
 * Default logins:
 *   admin / admin123   (full access)
 *   staff / staff123   (limited access)
 */
public class RxShelfSafe {

    public static void main(String[] args) {

        // Make all buttons show a hand cursor when the mouse is over them
        // (small quality-of-life improvement)
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                if (e.getSource() instanceof JButton) {
                    ((JButton) e.getSource()).setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);

        // Load all data from the text file (or create sample data)
        DataStore data = new DataStore();

        // Show the login window
        LoginScreen login = new LoginScreen(data);
        login.setVisible(true);
    }
}
