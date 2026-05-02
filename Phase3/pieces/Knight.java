package pieces;

import utils.Position;

/**
 * Represents a Knight chess piece.
 * Displayed using Unicode chess knight symbols.
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public Knight(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a knight.
     * White knight: ♘, Black knight: ♞
     *
     * @return Unicode knight symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2658" : "\u265E";
    }

    /**
     * Returns the piece type name.
     *
     * @return "Knight"
     */
    @Override
    public String getPieceName() {
        return "Knight";
    }
}

