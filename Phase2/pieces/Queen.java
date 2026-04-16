package pieces;

import utils.Position;

/**
 * Represents a Queen chess piece.
 * Displayed using Unicode chess queen symbols.
 */
public class Queen extends Piece {

    /**
     * Constructs a Queen with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public Queen(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a queen.
     * White queen: ♕, Black queen: ♛
     *
     * @return Unicode queen symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2655" : "\u265B";
    }

    /**
     * Returns the piece type name.
     *
     * @return "Queen"
     */
    @Override
    public String getPieceName() {
        return "Queen";
    }
}
