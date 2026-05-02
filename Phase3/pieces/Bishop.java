package pieces;

import utils.Position;

/**
 * Represents a Bishop chess piece.
 * Displayed using Unicode chess bishop symbols.
 */
public class Bishop extends Piece {

    /**
     * Constructs a Bishop with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public Bishop(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a bishop.
     * White bishop: ♗, Black bishop: ♝
     *
     * @return Unicode bishop symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2657" : "\u265D";
    }

    /**
     * Returns the piece type name.
     *
     * @return "Bishop"
     */
    @Override
    public String getPieceName() {
        return "Bishop";
    }
}

