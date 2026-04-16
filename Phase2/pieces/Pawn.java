package pieces;

import utils.Position;

/**
 * Represents a Pawn chess piece.
 * Displayed using Unicode chess pawn symbols.
 */
public class Pawn extends Piece {

    /**
     * Constructs a Pawn with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public Pawn(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a pawn.
     * White pawn: ♙, Black pawn: ♟
     *
     * @return Unicode pawn symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2659" : "\u265F";
    }

    /**
     * Returns the piece type name.
     *
     * @return "Pawn"
     */
    @Override
    public String getPieceName() {
        return "Pawn";
    }
}

