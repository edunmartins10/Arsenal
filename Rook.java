package pieces;

import utils.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rook chess piece.
 */
public class Rook extends Piece {

    /**
     * Creates a rook with color and position.
     */
    public Rook(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the rook.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the rook.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wR" : "bR";
    }
}