package pieces;

import utils.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a queen chess piece.
 */
public class Queen extends Piece {

    /**
     * Creates a queen with color and position.
     */
    public Queen(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the queen.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the queen.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wQ" : "bQ";
    }
}
