package pieces;

import utils.Position;

/**
 * Abstract base class for all chess pieces.
 * Every piece has a color, a position, and a Unicode symbol for display.
 */
public abstract class Piece {

    /** The color of this piece: "white" or "black". */
    protected String color;

    /** The current board position of this piece. */
    protected Position position;

    /**
     * Constructs a Piece with the given color and starting position.
     *
     * @param color    "white" or "black"
     * @param position the starting position on the board
     */
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns the color of this piece.
     *
     * @return "white" or "black"
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the current position of this piece.
     *
     * @return current Position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Updates the position of this piece.
     *
     * @param position the new Position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Returns true if this piece belongs to the white player.
     *
     * @return true if white
     */
    public boolean isWhite() {
        return color.equals("white");
    }

    /**
     * Returns the Unicode character used to render this piece on the GUI board.
     *
     * @return Unicode chess symbol string
     */
    public abstract String getSymbol();

    /**
     * Returns the name of the piece type (e.g., "Pawn", "King").
     * Used for move history display.
     *
     * @return piece type name
     */
    public abstract String getPieceName();
}
