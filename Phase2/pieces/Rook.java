package pieces;

import utils.Position;

/**
 * Represents a Rook chess piece.
 * Displayed using Unicode chess rook symbols.
 */
public class Rook extends Piece {

    /**
     * Constructs a Rook with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position starting position on the board
     */
    public Rook(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns the Unicode symbol for a rook.
     * White rook: ♖, Black rook: ♜
     *
     * @return Unicode rook symbol
     */
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2656" : "\u265C";
    }

    /**
     * Returns the piece type name.
     *
     * @return "Rook"
     */
    @Override
    public String getPieceName() {
        return "Rook";
    }
}

