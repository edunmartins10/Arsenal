package pieces;

import utils.Position;
import java.util.ArrayList; 
import java.util.List;

/**
 * Represents a king chess piece.
 */
public class King extends Piece {

    /**
     * Creates a king with color and position.
     */
    public King(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the king.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the king.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wK" : "bK";
    }
}
