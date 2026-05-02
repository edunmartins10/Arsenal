package model;

import pieces.*;

/**
 * AI chess opponent using a minimax algorithm with alpha-beta pruning.
 * Evaluates board positions and selects the best move for the black player.
 */
public class ChessAI {

    /** Search depth for minimax (higher = stronger but slower). */
    private static final int DEPTH = 3;

    /** Piece values for board evaluation. */
    private static final int PAWN_VALUE   = 100;
    private static final int KNIGHT_VALUE = 320;
    private static final int BISHOP_VALUE = 330;
    private static final int ROOK_VALUE   = 500;
    private static final int QUEEN_VALUE  = 900;
    private static final int KING_VALUE   = 20000;

    /**
     * Finds the best move for the black AI player.
     *
     * @param board the current board state
     * @return an int array {fromRow, fromCol, toRow, toCol} representing the best move,
     *         or null if no moves available
     */
    public static int[] getBestMove(BoardModel board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece p = board.getPiece(fromRow, fromCol);
                if (p != null && p.getColor().equals("black")) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (MoveValidator.isLegalMove(board, fromRow, fromCol, toRow, toCol)) {
                                // Simulate move
                                Piece captured = board.movePiece(fromRow, fromCol, toRow, toCol);
                                int score = minimax(board, DEPTH - 1, Integer.MIN_VALUE,
                                                   Integer.MAX_VALUE, false);
                                // Undo move
                                board.setPiece(fromRow, fromCol, p);
                                board.setPiece(toRow, toCol, captured);
                                p.setPosition(new utils.Position(fromRow, fromCol));
                                if (captured != null)
                                    captured.setPosition(new utils.Position(toRow, toCol));

                                if (score > bestScore) {
                                    bestScore = score;
                                    bestMove = new int[]{fromRow, fromCol, toRow, toCol};
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning.
     * Maximizes score for black, minimizes for white.
     *
     * @param board       the board state
     * @param depth       remaining search depth
     * @param alpha       alpha value for pruning
     * @param beta        beta value for pruning
     * @param isMaximizing true if maximizing (black's turn)
     * @return the evaluated score
     */
    private static int minimax(BoardModel board, int depth, int alpha, int beta,
                                boolean isMaximizing) {
        if (depth == 0) return evaluateBoard(board);

        String color = isMaximizing ? "black" : "white";

        if (MoveValidator.isCheckmate(board, color))
            return isMaximizing ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            outer:
            for (int fr = 0; fr < 8; fr++) {
                for (int fc = 0; fc < 8; fc++) {
                    Piece p = board.getPiece(fr, fc);
                    if (p != null && p.getColor().equals("black")) {
                        for (int tr = 0; tr < 8; tr++) {
                            for (int tc = 0; tc < 8; tc++) {
                                if (MoveValidator.isLegalMove(board, fr, fc, tr, tc)) {
                                    Piece captured = board.movePiece(fr, fc, tr, tc);
                                    int score = minimax(board, depth - 1, alpha, beta, false);
                                    board.setPiece(fr, fc, p);
                                    board.setPiece(tr, tc, captured);
                                    p.setPosition(new utils.Position(fr, fc));
                                    if (captured != null)
                                        captured.setPosition(new utils.Position(tr, tc));
                                    maxScore = Math.max(maxScore, score);
                                    alpha = Math.max(alpha, score);
                                    if (beta <= alpha) break outer;
                                }
                            }
                        }
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            outer:
            for (int fr = 0; fr < 8; fr++) {
                for (int fc = 0; fc < 8; fc++) {
                    Piece p = board.getPiece(fr, fc);
                    if (p != null && p.getColor().equals("white")) {
                        for (int tr = 0; tr < 8; tr++) {
                            for (int tc = 0; tc < 8; tc++) {
                                if (MoveValidator.isLegalMove(board, fr, fc, tr, tc)) {
                                    Piece captured = board.movePiece(fr, fc, tr, tc);
                                    int score = minimax(board, depth - 1, alpha, beta, true);
                                    board.setPiece(fr, fc, p);
                                    board.setPiece(tr, tc, captured);
                                    p.setPosition(new utils.Position(fr, fc));
                                    if (captured != null)
                                        captured.setPosition(new utils.Position(tr, tc));
                                    minScore = Math.min(minScore, score);
                                    beta = Math.min(beta, score);
                                    if (beta <= alpha) break outer;
                                }
                            }
                        }
                    }
                }
            }
            return minScore;
        }
    }

    /**
     * Evaluates the board position from black's perspective.
     * Positive scores favor black, negative scores favor white.
     *
     * @param board the board to evaluate
     * @return the evaluation score
     */
    private static int evaluateBoard(BoardModel board) {
        int score = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null) {
                    int value = getPieceValue(p);
                    score += p.getColor().equals("black") ? value : -value;
                }
            }
        }
        return score;
    }

    /**
     * Returns the material value of a piece.
     *
     * @param piece the piece to evaluate
     * @return integer value
     */
    private static int getPieceValue(Piece piece) {
        if (piece instanceof Pawn)   return PAWN_VALUE;
        if (piece instanceof Knight) return KNIGHT_VALUE;
        if (piece instanceof Bishop) return BISHOP_VALUE;
        if (piece instanceof Rook)   return ROOK_VALUE;
        if (piece instanceof Queen)  return QUEEN_VALUE;
        if (piece instanceof King)   return KING_VALUE;
        return 0;
    }
}
