package pieces;
 
import utils.Position;
import java.util.List;

/**
 * Abstract base class for all chess pieces.
 * Stores common data shared by every piece.
 */
public abstract class Piece {

    protected String color;
    protected Position position;

    /**
     * Creates a piece with a color and position.
     */
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns the color of the piece.
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the current position of the piece.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the current position of the piece.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Moves the piece to a new position.
     */
    public void move(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Returns all possible moves for the piece.
     */
    public abstract List<Position> possibleMoves();

    /**
     * Returns the text symbol used to display the piece.
     */
    public abstract String getSymbol();
}
