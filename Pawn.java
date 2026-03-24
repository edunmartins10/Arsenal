package pieces;

import utils.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends Piece {

    /**
     * Creates a pawn with color and position.
     */
    public Pawn(String color, Position position) {
        super(color, position);
    }

    /**
     * Returns possible moves for the pawn.
     * Phase 1 uses a basic placeholder list.
     */
    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    /**
     * Returns the display symbol for the pawn.
     */
    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wp" : "bp";
    }
}