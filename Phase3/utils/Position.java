package utils;

/**
 * Represents a position on the chess board using row and column indices.
 * Row 0 is the top of the board (black's back rank),
 * Row 7 is the bottom (white's back rank).
 */
public class Position {

    private int row;
    private int col;

    /**
     * Constructs a Position with the given row and column.
     *
     * @param row the row index (0-7)
     * @param col the column index (0-7)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row index of this position.
     *
     * @return row index (0-7)
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this position.
     *
     * @return column index (0-7)
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns true if this position is within the 8x8 board boundaries.
     *
     * @return true if valid board position
     */
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Returns a chess notation string for this position (e.g., "E2").
     *
     * @return chess notation string
     */
    @Override
    public String toString() {
        char file = (char) ('A' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    /**
     * Checks equality based on row and column values.
     *
     * @param obj the object to compare
     * @return true if both positions have the same row and column
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Returns a hash code based on row and column.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return 8 * row + col;
    }
}


