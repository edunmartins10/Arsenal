package board; 

import pieces.*;
import utils.Position;

/**
 * Represents the chess board.
 * Stores pieces in an 8x8 array and can display the board.
 */
public class Board {

    private Piece[][] board;

    /**
     * Creates a board and sets up the starting positions.
     */
    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    /**
     * Sets up all pieces in their starting positions.
     */
    public void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }

        board[0][0] = new Rook("black", new Position(0, 0));
        board[0][1] = new Knight("black", new Position(0, 1));
        board[0][2] = new Bishop("black", new Position(0, 2));
        board[0][3] = new Queen("black", new Position(0, 3));
        board[0][4] = new King("black", new Position(0, 4));
        board[0][5] = new Bishop("black", new Position(0, 5));
        board[0][6] = new Knight("black", new Position(0, 6));
        board[0][7] = new Rook("black", new Position(0, 7));

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn("black", new Position(1, col));
        }

        board[7][0] = new Rook("white", new Position(7, 0));
        board[7][1] = new Knight("white", new Position(7, 1));
        board[7][2] = new Bishop("white", new Position(7, 2));
        board[7][3] = new Queen("white", new Position(7, 3));
        board[7][4] = new King("white", new Position(7, 4));
        board[7][5] = new Bishop("white", new Position(7, 5));
        board[7][6] = new Knight("white", new Position(7, 6));
        board[7][7] = new Rook("white", new Position(7, 7));

        for (int col = 0; col < 8; col++) {
            board[6][col] = new Pawn("white", new Position(6, col));
        }
    }

    /**
     * Returns the piece at a given position.
     */
    public Piece getPiece(Position position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Moves a piece from one position to another.
     */
    public void movePiece(Position from, Position to) {
        Piece piece = board[from.getRow()][from.getColumn()];

        if (piece != null) {
            board[to.getRow()][to.getColumn()] = piece;
            board[from.getRow()][from.getColumn()] = null;
            piece.setPosition(to);
        }
    }

    /**
     * Displays the board in the console.
     */
    public void display() {
        System.out.println("   A  B  C  D  E  F  G  H");

        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + "  ");

            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    System.out.print("## ");
                } else {
                    System.out.print(board[row][col].getSymbol() + " ");
                }
            }

            System.out.println();
        }
    }
}
