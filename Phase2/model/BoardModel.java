package model;

import pieces.*;
import utils.Position;

/**
 * Represents the chess board state.
 * Manages an 8x8 grid of pieces, handles moves, captures,
 * and tracks move history for undo functionality.
 */
public class BoardModel {

    /** 8x8 grid storing pieces. Null means empty square. */
    private Piece[][] grid;

    /**
     * Constructs a BoardModel and places all pieces in starting positions.
     */
    public BoardModel() {
        grid = new Piece[8][8];
        initializeBoard();
    }

    /**
     * Places all 32 chess pieces in their standard starting positions.
     */
    public void initializeBoard() {
        // Clear board
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                grid[r][c] = null;

        // Black back rank
        grid[0][0] = new Rook("black",   new Position(0, 0));
        grid[0][1] = new Knight("black", new Position(0, 1));
        grid[0][2] = new Bishop("black", new Position(0, 2));
        grid[0][3] = new Queen("black",  new Position(0, 3));
        grid[0][4] = new King("black",   new Position(0, 4));
        grid[0][5] = new Bishop("black", new Position(0, 5));
        grid[0][6] = new Knight("black", new Position(0, 6));
        grid[0][7] = new Rook("black",   new Position(0, 7));

        // Black pawns
        for (int c = 0; c < 8; c++)
            grid[1][c] = new Pawn("black", new Position(1, c));

        // White back rank
        grid[7][0] = new Rook("white",   new Position(7, 0));
        grid[7][1] = new Knight("white", new Position(7, 1));
        grid[7][2] = new Bishop("white", new Position(7, 2));
        grid[7][3] = new Queen("white",  new Position(7, 3));
        grid[7][4] = new King("white",   new Position(7, 4));
        grid[7][5] = new Bishop("white", new Position(7, 5));
        grid[7][6] = new Knight("white", new Position(7, 6));
        grid[7][7] = new Rook("white",   new Position(7, 7));

        // White pawns
        for (int c = 0; c < 8; c++)
            grid[6][c] = new Pawn("white", new Position(6, c));
    }

    /**
     * Returns the piece at the given position, or null if empty.
     *
     * @param row the row index (0-7)
     * @param col the column index (0-7)
     * @return the Piece at that square, or null
     */
    public Piece getPiece(int row, int col) {
        return grid[row][col];
    }

    /**
     * Moves a piece from one square to another.
     * If the destination has an opponent's piece, it is captured (removed).
     *
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     * @return the captured Piece, or null if no capture occurred
     */
    public Piece movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece captured = grid[toRow][toCol];
        Piece moving   = grid[fromRow][fromCol];

        grid[toRow][toCol]     = moving;
        grid[fromRow][fromCol] = null;

        if (moving != null)
            moving.setPosition(new Position(toRow, toCol));

        return captured;
    }

    /**
     * Places a piece directly onto the board at the given position.
     * Used by the undo system to restore previous state.
     *
     * @param row   row index
     * @param col   column index
     * @param piece the piece to place (can be null to clear)
     */
    public void setPiece(int row, int col, Piece piece) {
        grid[row][col] = piece;
        if (piece != null)
            piece.setPosition(new Position(row, col));
    }
}
