package model;

import pieces.Piece;

/**
 * Stores all data needed to describe and undo a single chess move.
 * Used by the move history panel and the undo system.
 */
public class MoveRecord {

    /** The piece that moved. */
    private Piece piece;

    /** Source and destination coordinates. */
    private int fromRow, fromCol, toRow, toCol;

    /** The piece that was captured, or null if no capture. */
    private Piece captured;

    /**
     * Constructs a MoveRecord storing all details of one move.
     *
     * @param piece    the piece that moved
     * @param fromRow  source row
     * @param fromCol  source column
     * @param toRow    destination row
     * @param toCol    destination column
     * @param captured the captured piece, or null
     */
    public MoveRecord(Piece piece, int fromRow, int fromCol,
                      int toRow, int toCol, Piece captured) {
        this.piece    = piece;
        this.fromRow  = fromRow;
        this.fromCol  = fromCol;
        this.toRow    = toRow;
        this.toCol    = toCol;
        this.captured = captured;
    }

    /** @return the piece that moved */
    public Piece getPiece()    { return piece; }

    /** @return source row */
    public int getFromRow()    { return fromRow; }

    /** @return source column */
    public int getFromCol()    { return fromCol; }

    /** @return destination row */
    public int getToRow()      { return toRow; }

    /** @return destination column */
    public int getToCol()      { return toCol; }

    /** @return captured piece, or null */
    public Piece getCaptured() { return captured; }

    /**
     * Returns a human-readable move string for the history panel.
     * Example: "White Pawn: E2 → E4"
     *
     * @return formatted move string
     */
    @Override
    public String toString() {
        String color = piece.getColor().substring(0, 1).toUpperCase()
                     + piece.getColor().substring(1);
        String from = "" + (char)('A' + fromCol) + (8 - fromRow);
        String to   = "" + (char)('A' + toCol)   + (8 - toRow);
        String cap  = (captured != null) ? " [x " + captured.getPieceName() + "]" : "";
        return color + " " + piece.getPieceName() + ": " + from + " → " + to + cap;
    }
}


