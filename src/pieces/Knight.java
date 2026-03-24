package pieces;

import utils.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a knight chess piece.
 */
public class Knight extends Piece {

    /**
     * Creates a knight with color and position.
     */
    public Knight(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the knight.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the knight.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wN" : "bN";
    }
}