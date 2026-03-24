package pieces; 

import utils.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bishop chess piece.
 */
public class Bishop extends Piece {

    /**
     * Creates a bishop with color and position.
     */
    public Bishop(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the bishop.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the bishop.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wB" : "bB";
    }
}
