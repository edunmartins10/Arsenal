import gui.ChessFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Chess Game GUI (Phase 2).
 * Launches the ChessFrame on the Swing Event Dispatch Thread.
 */
public class Main {

    /**
     * Starts the chess game application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessFrame::new);
    }
}
