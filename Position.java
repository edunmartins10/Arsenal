package utils;

/**
 * Represents a position on the chess board.
 * Stores row and column values.
 */
public class Position {

    private int row;
    private int column;

    /**
     * Constructor to create a position
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Get row
     */
    public int getRow() {
        return row;
    }

    /**
     * Get column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Set column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Convert position to string (like E2)
     */
    @Override
    public String toString() {
        char file = (char) ('A' + column);
        int rank = 8 - row;
        return "" + file + rank;
    }
}