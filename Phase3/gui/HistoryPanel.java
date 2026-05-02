package gui;

import model.MoveRecord;
import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Side panel that displays the move history, captured pieces,
 * and provides an Undo button to revert the last move.
 */
public class HistoryPanel extends JPanel {

    /** List model backing the move history JList. */
    private DefaultListModel<String> moveListModel;

    /** Displays move history entries. */
    private JList<String> moveList;

    /** Stores captured white pieces for display. */
    private List<Piece> capturedByBlack = new ArrayList<>();

    /** Stores captured black pieces for display. */
    private List<Piece> capturedByWhite = new ArrayList<>();

    /** Label showing captured pieces. */
    private JLabel capturedLabel;

    /** Reference to board panel for undo calls. */
    private ChessBoardPanel boardPanel;

    /**
     * Constructs the HistoryPanel with move list, captured pieces area, and Undo button.
     */
    public HistoryPanel() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(220, 0));
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Move History");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Move list
        moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        moveList.setBackground(new Color(60, 60, 60));
        moveList.setForeground(Color.WHITE);
        moveList.setFont(new Font("Monospaced", Font.PLAIN, 11));
        moveList.setSelectionBackground(new Color(80, 80, 80));

        JScrollPane scrollPane = new JScrollPane(moveList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel — captured pieces + undo
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(new Color(40, 40, 40));

        capturedLabel = new JLabel("<html>Captured:<br>None</html>");
        capturedLabel.setForeground(new Color(200, 200, 200));
        capturedLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        capturedLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        bottomPanel.add(capturedLabel, BorderLayout.CENTER);

        JButton undoBtn = new JButton("⟲ Undo");
        undoBtn.setBackground(new Color(180, 60, 60));
        undoBtn.setForeground(Color.WHITE);
        undoBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        undoBtn.setFocusPainted(false);
        undoBtn.setBorderPainted(false);
        undoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        undoBtn.addActionListener(e -> {
            if (boardPanel != null) {
                boardPanel.undoLastMove();
                undoLastFromHistory();
            }
        });
        bottomPanel.add(undoBtn, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the reference to the board panel so the undo button can call it.
     *
     * @param panel the ChessBoardPanel
     */
    public void setBoardPanel(ChessBoardPanel panel) {
        this.boardPanel = panel;
    }

    /**
     * Adds a completed move to the history list and updates captured pieces.
     *
     * @param record the MoveRecord describing the move
     */
    public void addMove(MoveRecord record) {
        moveListModel.addElement((moveListModel.size() + 1) + ". " + record.toString());
        moveList.ensureIndexIsVisible(moveListModel.size() - 1);

        if (record.getCaptured() != null) {
            Piece cap = record.getCaptured();
            if (cap.isWhite()) {
                capturedByBlack.add(cap);
            } else {
                capturedByWhite.add(cap);
            }
            updateCapturedLabel();
        }
    }

    /**
     * Removes the last entry from the move history (called on undo).
     */
    public void undoLastFromHistory() {
        if (moveListModel.isEmpty()) return;

        // Check if last move had a capture and remove it
        String last = moveListModel.getElementAt(moveListModel.size() - 1);
        if (last.contains("[x ")) {
            // Remove last captured piece from appropriate list
            if (last.startsWith("White")) {
                if (!capturedByBlack.isEmpty())
                    capturedByBlack.remove(capturedByBlack.size() - 1);
            } else {
                if (!capturedByWhite.isEmpty())
                    capturedByWhite.remove(capturedByWhite.size() - 1);
            }
            updateCapturedLabel();
        }

        moveListModel.remove(moveListModel.size() - 1);
    }

    /**
     * Clears all move history and captured piece data (called on New Game).
     */
    public void clearHistory() {
        moveListModel.clear();
        capturedByBlack.clear();
        capturedByWhite.clear();
        updateCapturedLabel();
    }

    /**
     * Updates the captured pieces label with current captured piece symbols.
     */
    private void updateCapturedLabel() {
        StringBuilder sb = new StringBuilder("<html>Captured:<br>");

        if (capturedByWhite.isEmpty() && capturedByBlack.isEmpty()) {
            sb.append("None");
        } else {
            if (!capturedByWhite.isEmpty()) {
                sb.append("White took: ");
                for (Piece p : capturedByWhite) sb.append(p.getSymbol()).append(" ");
                sb.append("<br>");
            }
            if (!capturedByBlack.isEmpty()) {
                sb.append("Black took: ");
                for (Piece p : capturedByBlack) sb.append(p.getSymbol()).append(" ");
            }
        }

        sb.append("</html>");
        capturedLabel.setText(sb.toString());
    }
}

