package pieces;

import utils.Position;

/**
 * Represents a King chess piece.
 * Displayed using Unicode chess king symbols.
 */
public class King extends Piece {

    /**
     * Constructs a King with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public King(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a king.
     * White king: ♔, Black king: ♚
     *
     * @return Unicode king symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2654" : "\u265A";
    }

    /**
     * Returns the piece type name.
     *
     * @return "King"
     */
    @Override
    public String getPieceName() {
        return "King";
    }
}
