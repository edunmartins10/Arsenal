package game;

import java.util.Scanner;

/**
 * Controls the main flow of the chess game.
 */
public class Game {

    private char[][] board;

    /**
     * Creates a new game and initializes the board.
     */
    public Game() {
        board = new char[8][8];
        initializeBoard();
    }

    /**
     * Sets up a simple board for demonstration.
     */
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = '.';
            }
        }

        board[1][0] = 'P';
        board[6][0] = 'p';
    }

    /**
     * Runs the game loop.
     */
    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();

            System.out.print("Enter move (row1 col1 row2 col2) or -1 to quit: ");
            int r1 = scanner.nextInt();

            if (r1 == -1) {
                break;
            }

            int c1 = scanner.nextInt();
            int r2 = scanner.nextInt();
            int c2 = scanner.nextInt();

            board[r2][c2] = board[r1][c1];
            board[r1][c1] = '.';
        }

        scanner.close();
    }

    /**
     * Prints the board in the console.
     */
    private void printBoard() {
        System.out.println("Chessboard:");
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
