package gui;

import model.BoardModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * The main application window for the chess game.
 * Contains the menu bar (New Game, Save, Load, Settings),
 * the chess board panel, the history panel, and the turn indicator.
 */
public class ChessFrame extends JFrame {

    /** The board data model. */
    private BoardModel boardModel;

    /** The main board GUI panel. */
    private ChessBoardPanel boardPanel;

    /** The move history side panel. */
    private HistoryPanel historyPanel;

    /** Label showing whose turn it is. */
    private JLabel turnLabel;

    /**
     * Constructs and displays the main chess game window.
     */
    public ChessFrame() {
        super("Chess Game — Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        boardModel   = new BoardModel();
        historyPanel = new HistoryPanel();
        boardPanel   = new ChessBoardPanel(boardModel, historyPanel);
        historyPanel.setBoardPanel(boardPanel);

        // ── Turn label ────────────────────────────────────────────────
        turnLabel = new JLabel("White's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setOpaque(true);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // ── Layout ────────────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.add(turnLabel, BorderLayout.NORTH);
        center.add(boardPanel, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.EAST);

        // ── Menu bar ──────────────────────────────────────────────────
        setJMenuBar(buildMenuBar());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Builds the menu bar with Game and Settings menus.
     *
     * @return the constructed JMenuBar
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 40, 40));

        // ── Game menu ─────────────────────────────────────────────────
        JMenu gameMenu = styledMenu("Game");

        JMenuItem newGame = styledMenuItem("New Game");
        newGame.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new game? Current game will be lost.",
                "New Game", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boardPanel.resetGame();
            }
        });

        JMenuItem saveGame = styledMenuItem("Save Game");
        saveGame.addActionListener(e -> saveGame());

        JMenuItem loadGame = styledMenuItem("Load Game");
        loadGame.addActionListener(e -> loadGame());

        JMenuItem exitItem = styledMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        // ── Settings menu ─────────────────────────────────────────────
        JMenu settingsMenu = styledMenu("Settings");

        JMenuItem customize = styledMenuItem("Customize Board...");
        customize.addActionListener(e -> new SettingsDialog(this, boardPanel).setVisible(true));
        settingsMenu.add(customize);

        menuBar.add(gameMenu);
        menuBar.add(settingsMenu);

        return menuBar;
    }

    /**
     * Saves the current board state to a text file using a JFileChooser.
     * Each line stores: row col pieceClass color
     */
    private void saveGame() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Game");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    pieces.Piece p = boardModel.getPiece(r, c);
                    if (p != null) {
                        pw.println(r + " " + c + " "
                            + p.getClass().getSimpleName() + " " + p.getColor());
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Game saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads a previously saved board state from a text file.
     * Clears the current board and restores pieces from file.
     */
    private void loadGame() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load Game");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Clear board first
            for (int r = 0; r < 8; r++)
                for (int c = 0; c < 8; c++)
                    boardModel.setPiece(r, c, null);

            historyPanel.clearHistory();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length < 4) continue;

                int row   = Integer.parseInt(parts[0]);
                int col   = Integer.parseInt(parts[1]);
                String cls = parts[2];
                String color = parts[3];
                utils.Position pos = new utils.Position(row, col);

                pieces.Piece piece = switch (cls) {
                    case "Pawn"   -> new pieces.Pawn(color, pos);
                    case "Rook"   -> new pieces.Rook(color, pos);
                    case "Knight" -> new pieces.Knight(color, pos);
                    case "Bishop" -> new pieces.Bishop(color, pos);
                    case "Queen"  -> new pieces.Queen(color, pos);
                    case "King"   -> new pieces.King(color, pos);
                    default       -> null;
                };
                if (piece != null) boardModel.setPiece(row, col, piece);
            }

            boardPanel.repaint();
            JOptionPane.showMessageDialog(this, "Game loaded successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the turn label to show whose turn it is.
     * Called by ChessBoardPanel after each move.
     *
     * @param turn "white" or "black"
     */
    public void updateTurnLabel(String turn) {
        String display = turn.substring(0, 1).toUpperCase() + turn.substring(1) + "'s Turn";
        turnLabel.setText(display);
        turnLabel.setBackground(turn.equals("white")
            ? new Color(200, 200, 200)
            : new Color(50, 50, 50));
        turnLabel.setForeground(turn.equals("white") ? Color.BLACK : Color.WHITE);
    }

    /**
     * Creates a styled JMenu for the menu bar.
     *
     * @param text the menu label
     * @return styled JMenu
     */
    private JMenu styledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return menu;
    }

    /**
     * Creates a styled JMenuItem for the menu bar.
     *
     * @param text the menu item label
     * @return styled JMenuItem
     */
    private JMenuItem styledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return item;
    }
}

