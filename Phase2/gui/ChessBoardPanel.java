package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.BoardModel;
import model.MoveRecord;
import pieces.King;
import pieces.Piece;

/**
 * The main chess board GUI panel.
 * Renders the 8x8 board with pieces, handles click-to-move and
 * drag-and-drop interactions, detects captures and King capture (game over).
 */
public class ChessBoardPanel extends JPanel {

    /** Size of each square in pixels. */
    private int squareSize = 80;

    /** The board data model. */
    private BoardModel boardModel;

    /** Reference to the history/undo panel. */
    private HistoryPanel historyPanel;

    /** Colors for light and dark squares. */
    private Color lightColor = new Color(240, 217, 181);
    private Color darkColor  = new Color(181, 136, 99);

    /** Currently selected square for click-to-move (-1 means none). */
    private int selectedRow = -1;
    private int selectedCol = -1;

    /**
     * Piece style index: 0=Classic, 1=Blue and Red, 2=Green and Purple, 3=Gold and Silver.
     */
    private int pieceStyle = 0;

    /** White piece colors per style. */
    private static final Color[] WHITE_PIECE_COLORS = {
        Color.WHITE,
        new Color(100, 149, 237),
        new Color(60, 179, 113),
        new Color(255, 215, 0)
    };

    /** Black piece colors per style. */
    private static final Color[] BLACK_PIECE_COLORS = {
        Color.BLACK,
        new Color(178, 34, 34),
        new Color(128, 0, 128),
        new Color(192, 192, 192)
    };

    /** Whose turn it is: "white" or "black". */
    private String currentTurn = "white";

    /** True when the game has ended. */
    private boolean gameOver = false;

    /** Drag state tracking. */
    private boolean isDragging = false;
    private Piece draggedPiece = null;
    private int dragFromRow = -1, dragFromCol = -1;
    private int dragX = -1, dragY = -1;

    /** Move history stack for undo. */
    private List<MoveRecord> moveHistory = new ArrayList<>();

    /**
     * Constructs the chess board panel and wires up mouse listeners.
     *
     * @param boardModel   the board data model
     * @param historyPanel the history/undo side panel
     */
    public ChessBoardPanel(BoardModel boardModel, HistoryPanel historyPanel) {
        this.boardModel   = boardModel;
        this.historyPanel = historyPanel;

        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));

        // ── Mouse Pressed ──────────────────────────────────────────────
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) return;

                int col = e.getX() / squareSize;
                int row = e.getY() / squareSize;
                if (!inBounds(row, col)) return;

                Piece piece = boardModel.getPiece(row, col);

                // Start drag if clicking own piece
                if (piece != null && piece.getColor().equals(currentTurn)) {
                    isDragging  = true;
                    draggedPiece = piece;
                    dragFromRow  = row;
                    dragFromCol  = col;
                    dragX = e.getX();
                    dragY = e.getY();
                    selectedRow = -1;
                    selectedCol = -1;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameOver) return;

                int col = e.getX() / squareSize;
                int row = e.getY() / squareSize;

                if (isDragging && draggedPiece != null) {
                    isDragging = false;
                    if (inBounds(row, col) && !(row == dragFromRow && col == dragFromCol)) {
                        tryMove(dragFromRow, dragFromCol, row, col);
                    }
                    draggedPiece = null;
                    dragFromRow = dragFromCol = -1;
                    dragX = dragY = -1;
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver || isDragging) return;

                int col = e.getX() / squareSize;
                int row = e.getY() / squareSize;
                if (!inBounds(row, col)) return;

                Piece piece = boardModel.getPiece(row, col);

                if (selectedRow == -1) {
                    // First click — select a piece
                    if (piece != null && piece.getColor().equals(currentTurn)) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    // Second click — attempt move
                    if (row == selectedRow && col == selectedCol) {
                        // Deselect
                        selectedRow = selectedCol = -1;
                    } else if (piece != null && piece.getColor().equals(currentTurn)) {
                        // Switch selection to new piece
                        selectedRow = row;
                        selectedCol = col;
                    } else {
                        tryMove(selectedRow, selectedCol, row, col);
                        selectedRow = selectedCol = -1;
                    }
                    repaint();
                }
            }
        });

        // ── Mouse Dragged ──────────────────────────────────────────────
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    dragX = e.getX();
                    dragY = e.getY();
                    repaint();
                }
            }
        });
    }

    /**
     * Attempts to move a piece from (fromRow, fromCol) to (toRow, toCol).
     * Validates the move is not onto own piece, performs capture if needed,
     * records history, switches turn, and checks for King capture.
     *
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     */
    private void tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece moving   = boardModel.getPiece(fromRow, fromCol);
        Piece target   = boardModel.getPiece(toRow, toCol);

        if (moving == null) return;

        // Cannot capture own piece
        if (target != null && target.getColor().equals(currentTurn)) return;

        // Execute move
        Piece captured = boardModel.movePiece(fromRow, fromCol, toRow, toCol);

        // Record move
        MoveRecord record = new MoveRecord(moving, fromRow, fromCol, toRow, toCol, captured);
        moveHistory.add(record);
        historyPanel.addMove(record);

        repaint();

        // Check if a King was captured → game over
        if (captured instanceof King) {
            gameOver = true;
            String winner = currentTurn.substring(0, 1).toUpperCase() + currentTurn.substring(1);
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                    winner + " wins by capturing the King!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            });
            return;
        }

        // Switch turn
        currentTurn = currentTurn.equals("white") ? "black" : "white";

        // Notify frame to update turn label
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof ChessFrame) {
            ((ChessFrame) w).updateTurnLabel(currentTurn);
        }
    }

    /**
     * Undoes the last move, restoring the board and captured piece if any.
     * Called by the HistoryPanel undo button.
     */
    public void undoLastMove() {
        if (moveHistory.isEmpty() || gameOver) return;

        MoveRecord last = moveHistory.remove(moveHistory.size() - 1);

        // Restore moving piece to original square
        boardModel.setPiece(last.getFromRow(), last.getFromCol(), last.getPiece());
        // Restore captured piece (or clear destination)
        boardModel.setPiece(last.getToRow(), last.getToCol(), last.getCaptured());

        // Switch turn back
        currentTurn = currentTurn.equals("white") ? "black" : "white";

        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof ChessFrame) {
            ((ChessFrame) w).updateTurnLabel(currentTurn);
        }

        repaint();
    }

    /**
     * Resets the board to the initial state for a new game.
     */
    public void resetGame() {
        boardModel.initializeBoard();
        moveHistory.clear();
        historyPanel.clearHistory();
        selectedRow = selectedCol = -1;
        currentTurn = "white";
        gameOver    = false;
        isDragging  = false;
        draggedPiece = null;

        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof ChessFrame) {
            ((ChessFrame) w).updateTurnLabel(currentTurn);
        }
        repaint();
    }

    /**
     * Sets the light square color (used by Settings).
     *
     * @param c the new light square color
     */
    public void setLightColor(Color c) { lightColor = c; repaint(); }

    /**
     * Sets the dark square color (used by Settings).
     *
     * @param c the new dark square color
     */
    public void setDarkColor(Color c)  { darkColor  = c; repaint(); }

    /**
     * Sets the square size (used by Settings for board size).
     *
     * @param size the new square size in pixels
     */
    public void setSquareSize(int size) {
        squareSize = size;
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        revalidate();
        repaint();
    }

    /**
     * Sets the piece color style (used by Settings).
     * 0=Classic, 1=Blue and Red, 2=Green and Purple, 3=Gold and Silver.
     *
     * @param style the style index
     */
    public void setPieceStyle(int style) {
        this.pieceStyle = style;
        repaint();
    }

    /**
     * Returns the current square size in pixels.
     *
     * @return square size
     */
    public int getSquareSize() { return squareSize; }

    /**
     * Paints the board: squares, highlights, pieces, and dragged piece.
     *
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean isLight = (row + col) % 2 == 0;
                Color base = isLight ? lightColor : darkColor;

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    base = new Color(106, 168, 79); // green highlight
                }

                g2.setColor(base);
                g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Draw piece (skip dragged piece's origin square)
                Piece piece = boardModel.getPiece(row, col);
                if (piece != null && !(isDragging && row == dragFromRow && col == dragFromCol)) {
                    drawPiece(g2, piece, col * squareSize, row * squareSize);
                }
            }
        }

        // Draw rank and file labels
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        for (int i = 0; i < 8; i++) {
            g2.setColor(i % 2 == 0 ? darkColor : lightColor);
            g2.drawString(String.valueOf(8 - i), 3, i * squareSize + 14);
            g2.setColor(i % 2 == 0 ? lightColor : darkColor);
            g2.drawString(String.valueOf((char)('A' + i)),
                          i * squareSize + squareSize - 12,
                          8 * squareSize - 3);
        }

        // Draw dragged piece following cursor
        if (isDragging && draggedPiece != null) {
            drawPiece(g2, draggedPiece,
                      dragX - squareSize / 2,
                      dragY - squareSize / 2);
        }
    }

    /**
     * Draws a single chess piece centered in its square using Unicode symbols.
     *
     * @param g2     the Graphics2D context
     * @param piece  the piece to draw
     * @param x      the x pixel coordinate of the square's top-left
     * @param y      the y pixel coordinate of the square's top-left
     */
    private void drawPiece(Graphics2D g2, Piece piece, int x, int y) {
        int fontSize = (int)(squareSize * 0.65);
        g2.setFont(new Font("Serif", Font.PLAIN, fontSize));

        String symbol = piece.getSymbol();

        // Shadow for readability
        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(symbol,
                      x + squareSize / 2 - fontSize / 3 + 2,
                      y + squareSize / 2 + fontSize / 3 + 2);

        // Piece color based on selected style
        g2.setColor(piece.isWhite() ? WHITE_PIECE_COLORS[pieceStyle] : BLACK_PIECE_COLORS[pieceStyle]);
        g2.drawString(symbol,
                      x + squareSize / 2 - fontSize / 3,
                      y + squareSize / 2 + fontSize / 3);
    }

    /**
     * Checks if a row/col is within board bounds.
     *
     * @param row row index
     * @param col column index
     * @return true if within 0-7 range
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}