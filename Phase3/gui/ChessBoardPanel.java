package gui;

import model.BoardModel;
import model.ChessAI;
import model.MoveRecord;
import model.MoveValidator;
import pieces.King;
import pieces.Piece;
import utils.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main chess board GUI panel for Phase 3.
 * Integrates backend chess logic with the GUI, enforcing legal moves,
 * detecting check and checkmate, and supporting an optional AI opponent.
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

    /** Currently selected square for click-to-move. */
    private int selectedRow = -1;
    private int selectedCol = -1;

    /** Legal destination squares for the selected piece. */
    private List<int[]> legalMoves = new ArrayList<>();

    /** Whose turn it is. */
    private String currentTurn = "white";

    /** True when game has ended. */
    private boolean gameOver = false;

    /** True when AI mode is enabled. */
    private boolean aiMode = false;

    /** Drag state. */
    private boolean isDragging = false;
    private Piece draggedPiece = null;
    private int dragFromRow = -1, dragFromCol = -1;
    private int dragX = -1, dragY = -1;

    /** Move history for undo. */
    private List<MoveRecord> moveHistory = new ArrayList<>();

    /** Piece style index. */
    private int pieceStyle = 0;

    /** White piece colors per style. */
    private static final Color[] WHITE_PIECE_COLORS = {
        Color.WHITE, new Color(100, 149, 237),
        new Color(60, 179, 113), new Color(255, 215, 0)
    };

    /** Black piece colors per style. */
    private static final Color[] BLACK_PIECE_COLORS = {
        Color.BLACK, new Color(178, 34, 34),
        new Color(128, 0, 128), new Color(192, 192, 192)
    };

    /**
     * Constructs the chess board panel with full game logic integration.
     *
     * @param boardModel   the board data model
     * @param historyPanel the move history panel
     */
    public ChessBoardPanel(BoardModel boardModel, HistoryPanel historyPanel) {
        this.boardModel   = boardModel;
        this.historyPanel = historyPanel;
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));

        // ── Mouse Pressed ──────────────────────────────────────────────
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver || (aiMode && currentTurn.equals("black"))) return;
                int col = e.getX() / squareSize;
                int row = e.getY() / squareSize;
                if (!inBounds(row, col)) return;
                Piece piece = boardModel.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentTurn)) {
                    isDragging   = true;
                    draggedPiece = piece;
                    dragFromRow  = row;
                    dragFromCol  = col;
                    dragX = e.getX();
                    dragY = e.getY();
                    selectedRow = row;
                    selectedCol = col;
                    computeLegalMoves(row, col);
                    repaint();
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
                if (aiMode && currentTurn.equals("black")) return;
                int col = e.getX() / squareSize;
                int row = e.getY() / squareSize;
                if (!inBounds(row, col)) return;
                Piece piece = boardModel.getPiece(row, col);

                if (selectedRow == -1) {
                    if (piece != null && piece.getColor().equals(currentTurn)) {
                        selectedRow = row;
                        selectedCol = col;
                        computeLegalMoves(row, col);
                        repaint();
                    }
                } else {
                    if (row == selectedRow && col == selectedCol) {
                        selectedRow = selectedCol = -1;
                        legalMoves.clear();
                    } else if (piece != null && piece.getColor().equals(currentTurn)) {
                        selectedRow = row;
                        selectedCol = col;
                        computeLegalMoves(row, col);
                    } else {
                        tryMove(selectedRow, selectedCol, row, col);
                        selectedRow = selectedCol = -1;
                        legalMoves.clear();
                    }
                    repaint();
                }
            }
        });

        // ── Mouse Dragged ──────────────────────────────────────────────
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) { dragX = e.getX(); dragY = e.getY(); repaint(); }
            }
        });
    }

    /**
     * Computes all legal destination squares for the piece at (row, col).
     * Used to highlight valid moves on the board.
     *
     * @param row the piece's row
     * @param col the piece's column
     */
    private void computeLegalMoves(int row, int col) {
        legalMoves.clear();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (MoveValidator.isLegalMove(boardModel, row, col, r, c)) {
                    legalMoves.add(new int[]{r, c});
                }
            }
        }
    }

    /**
     * Attempts to move a piece, validating the move against chess rules.
     * Switches turn, triggers AI if enabled, and checks for checkmate.
     *
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     */
    private void tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!MoveValidator.isLegalMove(boardModel, fromRow, fromCol, toRow, toCol)) {
            return; // Illegal move — reject silently
        }

        Piece moving   = boardModel.getPiece(fromRow, fromCol);
        Piece captured = boardModel.movePiece(fromRow, fromCol, toRow, toCol);

        MoveRecord record = new MoveRecord(moving, fromRow, fromCol, toRow, toCol, captured);
        moveHistory.add(record);
        historyPanel.addMove(record);

        // Switch turn
        currentTurn = currentTurn.equals("white") ? "black" : "white";

        repaint();
        updateFrame();

        // Check for checkmate or stalemate
        if (MoveValidator.isCheckmate(boardModel, currentTurn)) {
            gameOver = true;
            String winner = currentTurn.equals("white") ? "Black" : "White";
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this,
                    winner + " wins by checkmate!",
                    "Checkmate!", JOptionPane.INFORMATION_MESSAGE));
            return;
        }

        if (MoveValidator.isStalemate(boardModel, currentTurn)) {
            gameOver = true;
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this,
                    "Stalemate! It's a draw.",
                    "Stalemate", JOptionPane.INFORMATION_MESSAGE));
            return;
        }

        // AI makes its move if enabled
        if (aiMode && currentTurn.equals("black") && !gameOver) {
            SwingUtilities.invokeLater(this::makeAIMove);
        }
    }

    /**
     * Makes the AI move for the black player using ChessAI.
     * Called after white makes a move in AI mode.
     */
    private void makeAIMove() {
        int[] move = ChessAI.getBestMove(boardModel);
        if (move != null) {
            tryMove(move[0], move[1], move[2], move[3]);
        }
    }

    /**
     * Undoes the last move, restoring board and captured piece.
     */
    public void undoLastMove() {
        if (moveHistory.isEmpty() || gameOver) return;
        MoveRecord last = moveHistory.remove(moveHistory.size() - 1);
        boardModel.setPiece(last.getFromRow(), last.getFromCol(), last.getPiece());
        boardModel.setPiece(last.getToRow(), last.getToCol(), last.getCaptured());
        currentTurn = currentTurn.equals("white") ? "black" : "white";
        updateFrame();
        repaint();
    }

    /**
     * Resets the game to the initial state.
     */
    public void resetGame() {
        boardModel.initializeBoard();
        moveHistory.clear();
        historyPanel.clearHistory();
        selectedRow = selectedCol = -1;
        legalMoves.clear();
        currentTurn = "white";
        gameOver    = false;
        isDragging  = false;
        draggedPiece = null;
        updateFrame();
        repaint();
    }

    /**
     * Enables or disables AI opponent mode.
     *
     * @param enabled true to enable AI, false for two-player mode
     */
    public void setAIMode(boolean enabled) {
        this.aiMode = enabled;
    }

    /** @return true if AI mode is active */
    public boolean isAIMode() { return aiMode; }

    /**
     * Sets the light square color.
     * @param c the new color
     */
    public void setLightColor(Color c) { lightColor = c; repaint(); }

    /**
     * Sets the dark square color.
     * @param c the new color
     */
    public void setDarkColor(Color c)  { darkColor  = c; repaint(); }

    /**
     * Sets the square size in pixels.
     * @param size the new size
     */
    public void setSquareSize(int size) {
        squareSize = size;
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        revalidate(); repaint();
    }

    /**
     * Sets the piece color style.
     * @param style style index 0-3
     */
    public void setPieceStyle(int style) { this.pieceStyle = style; repaint(); }

    /** @return current square size */
    public int getSquareSize() { return squareSize; }

    /**
     * Notifies the parent ChessFrame to update the turn label.
     */
    private void updateFrame() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof ChessFrame) ((ChessFrame) w).updateTurnLabel(currentTurn);
    }

    /**
     * Paints the board, highlights, pieces, check indicator, and dragged piece.
     *
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Find King in check position
        int checkKingRow = -1, checkKingCol = -1;
        if (MoveValidator.isKingInCheck(boardModel, currentTurn)) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = boardModel.getPiece(r, c);
                    if (p instanceof King && p.getColor().equals(currentTurn)) {
                        checkKingRow = r; checkKingCol = c;
                    }
                }
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean isLight = (row + col) % 2 == 0;
                Color base = isLight ? lightColor : darkColor;

                // Highlight selected square
                if (row == selectedRow && col == selectedCol)
                    base = new Color(106, 168, 79);

                // Highlight legal move squares
                for (int[] lm : legalMoves) {
                    if (lm[0] == row && lm[1] == col) {
                        base = boardModel.getPiece(row, col) != null
                            ? new Color(200, 80, 80)   // capture square
                            : new Color(130, 180, 100); // empty legal square
                    }
                }

                // Highlight King in check
                if (row == checkKingRow && col == checkKingCol)
                    base = new Color(220, 50, 50);

                g2.setColor(base);
                g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Draw piece
                Piece piece = boardModel.getPiece(row, col);
                if (piece != null && !(isDragging && row == dragFromRow && col == dragFromCol))
                    drawPiece(g2, piece, col * squareSize, row * squareSize);
            }
        }

        // Rank and file labels
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        for (int i = 0; i < 8; i++) {
            g2.setColor(i % 2 == 0 ? darkColor : lightColor);
            g2.drawString(String.valueOf(8 - i), 3, i * squareSize + 14);
            g2.setColor(i % 2 == 0 ? lightColor : darkColor);
            g2.drawString(String.valueOf((char)('A' + i)),
                          i * squareSize + squareSize - 12, 8 * squareSize - 3);
        }

        // Dragged piece
        if (isDragging && draggedPiece != null)
            drawPiece(g2, draggedPiece, dragX - squareSize / 2, dragY - squareSize / 2);
    }

    /**
     * Draws a chess piece using Unicode symbols with shadow for readability.
     *
     * @param g2    the Graphics2D context
     * @param piece the piece to draw
     * @param x     x pixel coordinate
     * @param y     y pixel coordinate
     */
    private void drawPiece(Graphics2D g2, Piece piece, int x, int y) {
        int fontSize = (int)(squareSize * 0.65);
        g2.setFont(new Font("Serif", Font.PLAIN, fontSize));
        String symbol = piece.getSymbol();

        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(symbol, x + squareSize/2 - fontSize/3 + 2,
                              y + squareSize/2 + fontSize/3 + 2);

        g2.setColor(piece.isWhite() ? WHITE_PIECE_COLORS[pieceStyle]
                                    : BLACK_PIECE_COLORS[pieceStyle]);
        g2.drawString(symbol, x + squareSize/2 - fontSize/3,
                              y + squareSize/2 + fontSize/3);
    }

    /**
     * Checks bounds.
     * @param row row index
     * @param col column index
     * @return true if within board
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
