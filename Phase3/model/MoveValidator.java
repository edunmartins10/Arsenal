package model;

import pieces.*;
import utils.Position;

/**
 * Validates chess moves according to standard chess rules.
 * Checks legal movement patterns for each piece type,
 * detects check, and filters moves that would leave the King in check.
 */
public class MoveValidator {

    /**
     * Returns true if moving a piece from (fromRow, fromCol) to (toRow, toCol)
     * is a legal chess move, considering piece movement rules and check.
     *
     * @param board   the current board model
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     * @return true if the move is legal
     */
    public static boolean isLegalMove(BoardModel board, int fromRow, int fromCol,
                                       int toRow, int toCol) {
        Piece moving = board.getPiece(fromRow, fromCol);
        if (moving == null) return false;

        Piece target = board.getPiece(toRow, toCol);

        // Cannot capture own piece
        if (target != null && target.getColor().equals(moving.getColor())) return false;

        // Check piece-specific movement rules
        if (!isValidPieceMove(board, moving, fromRow, fromCol, toRow, toCol)) return false;

        // Make sure move doesn't leave own King in check
        if (wouldLeaveKingInCheck(board, fromRow, fromCol, toRow, toCol, moving.getColor())) 
            return false;

        return true;
    }

    /**
     * Checks if a piece's movement pattern is valid (ignoring check).
     *
     * @param board   the board model
     * @param piece   the piece being moved
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     * @return true if movement pattern is valid
     */
    private static boolean isValidPieceMove(BoardModel board, Piece piece,
                                             int fromRow, int fromCol,
                                             int toRow, int toCol) {
        if (piece instanceof Pawn)   return isValidPawnMove(board, piece, fromRow, fromCol, toRow, toCol);
        if (piece instanceof Rook)   return isValidRookMove(board, fromRow, fromCol, toRow, toCol);
        if (piece instanceof Knight) return isValidKnightMove(fromRow, fromCol, toRow, toCol);
        if (piece instanceof Bishop) return isValidBishopMove(board, fromRow, fromCol, toRow, toCol);
        if (piece instanceof Queen)  return isValidQueenMove(board, fromRow, fromCol, toRow, toCol);
        if (piece instanceof King)   return isValidKingMove(fromRow, fromCol, toRow, toCol);
        return false;
    }

    /**
     * Validates pawn movement including forward moves, captures, and initial two-square advance.
     */
    private static boolean isValidPawnMove(BoardModel board, Piece piece,
                                            int fromRow, int fromCol,
                                            int toRow, int toCol) {
        int direction = piece.isWhite() ? -1 : 1;
        int startRow  = piece.isWhite() ? 6 : 1;

        // One step forward
        if (toCol == fromCol && toRow == fromRow + direction 
                && board.getPiece(toRow, toCol) == null)
            return true;

        // Two steps forward from starting position
        if (toCol == fromCol && fromRow == startRow
                && toRow == fromRow + 2 * direction
                && board.getPiece(fromRow + direction, fromCol) == null
                && board.getPiece(toRow, toCol) == null)
            return true;

        // Diagonal capture
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction) {
            Piece target = board.getPiece(toRow, toCol);
            if (target != null && !target.getColor().equals(piece.getColor()))
                return true;
        }

        return false;
    }

    /**
     * Validates rook movement (horizontal and vertical, no jumping).
     */
    private static boolean isValidRookMove(BoardModel board,
                                            int fromRow, int fromCol,
                                            int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) return false;
        return isPathClear(board, fromRow, fromCol, toRow, toCol);
    }

    /**
     * Validates knight movement (L-shape, can jump over pieces).
     */
    private static boolean isValidKnightMove(int fromRow, int fromCol,
                                              int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);
        return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
    }

    /**
     * Validates bishop movement (diagonal, no jumping).
     */
    private static boolean isValidBishopMove(BoardModel board,
                                              int fromRow, int fromCol,
                                              int toRow, int toCol) {
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) return false;
        return isPathClear(board, fromRow, fromCol, toRow, toCol);
    }

    /**
     * Validates queen movement (rook + bishop combined).
     */
    private static boolean isValidQueenMove(BoardModel board,
                                             int fromRow, int fromCol,
                                             int toRow, int toCol) {
        return isValidRookMove(board, fromRow, fromCol, toRow, toCol)
            || isValidBishopMove(board, fromRow, fromCol, toRow, toCol);
    }

    /**
     * Validates king movement (one square in any direction).
     */
    private static boolean isValidKingMove(int fromRow, int fromCol,
                                            int toRow, int toCol) {
        return Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1
            && !(toRow == fromRow && toCol == fromCol);
    }

    /**
     * Checks if the path between two squares is clear of pieces.
     * Used for rook, bishop, and queen moves.
     *
     * @param board   the board model
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     * @return true if no pieces block the path
     */
    private static boolean isPathClear(BoardModel board,
                                        int fromRow, int fromCol,
                                        int toRow, int toCol) {
        int dr = Integer.signum(toRow - fromRow);
        int dc = Integer.signum(toCol - fromCol);
        int r = fromRow + dr;
        int c = fromCol + dc;

        while (r != toRow || c != toCol) {
            if (board.getPiece(r, c) != null) return false;
            r += dr;
            c += dc;
        }
        return true;
    }

    /**
     * Simulates a move and checks if it leaves the moving player's King in check.
     *
     * @param board   the board model
     * @param fromRow source row
     * @param fromCol source column
     * @param toRow   destination row
     * @param toCol   destination column
     * @param color   the color of the moving player
     * @return true if the move would leave the King in check
     */
    public static boolean wouldLeaveKingInCheck(BoardModel board,
                                                 int fromRow, int fromCol,
                                                 int toRow, int toCol,
                                                 String color) {
        // Simulate the move
        Piece moving   = board.getPiece(fromRow, fromCol);
        Piece captured = board.getPiece(toRow, toCol);

        board.setPiece(toRow, toCol, moving);
        board.setPiece(fromRow, fromCol, null);
        if (moving != null) moving.setPosition(new Position(toRow, toCol));

        boolean inCheck = isKingInCheck(board, color);

        // Undo the simulation
        board.setPiece(fromRow, fromCol, moving);
        board.setPiece(toRow, toCol, captured);
        if (moving != null) moving.setPosition(new Position(fromRow, fromCol));

        return inCheck;
    }

    /**
     * Checks if the King of the given color is currently in check.
     *
     * @param board the board model
     * @param color the color to check ("white" or "black")
     * @return true if the King is in check
     */
    public static boolean isKingInCheck(BoardModel board, String color) {
        // Find King position
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p instanceof King && p.getColor().equals(color)) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }
        if (kingRow == -1) return false;

        // Check if any opponent piece can attack the King
        String opponent = color.equals("white") ? "black" : "white";
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor().equals(opponent)) {
                    if (isValidPieceMove(board, p, r, c, kingRow, kingCol))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given color is in checkmate (no legal moves available).
     *
     * @param board the board model
     * @param color the color to check
     * @return true if the color is in checkmate
     */
    public static boolean isCheckmate(BoardModel board, String color) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece p = board.getPiece(fromRow, fromCol);
                if (p != null && p.getColor().equals(color)) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (isLegalMove(board, fromRow, fromCol, toRow, toCol))
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given color is in stalemate (not in check but no legal moves).
     *
     * @param board the board model
     * @param color the color to check
     * @return true if stalemate
     */
    public static boolean isStalemate(BoardModel board, String color) {
        if (isKingInCheck(board, color)) return false;
        return isCheckmate(board, color);
    }
}

