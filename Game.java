package game; 

import board.Board;
import utils.Position;

import java.util.Scanner;

/**
 * Controls the main flow of the chess game.
 */
public class Game {

    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private Scanner scanner;

    /**
     * Creates a new game.
     */
    public Game() {
        board = new Board();
        whitePlayer = new Player("white");
        blackPlayer = new Player("black");
        currentPlayer = whitePlayer;
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the game loop.
     */
    public void play() {
        boolean running = true;

        while (running) {
            board.display();
            System.out.println();
            System.out.println(currentPlayer.getColor() + "'s turn");
            System.out.print("Enter move (example: E2 E4) or type quit: ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                running = false;
            } else if (isValidMoveFormat(input)) {
                String[] parts = input.split(" ");
                Position from = parsePosition(parts[0]);
                Position to = parsePosition(parts[1]);

                if (from != null && to != null) {
                    board.movePiece(from, to);
                    switchTurn();
                } else {
                    System.out.println("Invalid board position. Try again.");
                }
            } else {
                System.out.println("Invalid move format. Use format like E2 E4.");
            }

            System.out.println();
        }

        System.out.println("Game ended.");
        scanner.close();
    }

    /**
     * Checks whether the move format is correct.
     */
    private boolean isValidMoveFormat(String input) {
        return input.matches("^[A-Ha-h][1-8] [A-Ha-h][1-8]$");
    }

    /**
     * Converts chess notation like E2 into a Position object.
     */
    private Position parsePosition(String chessPosition) {
        chessPosition = chessPosition.toUpperCase();

        int column = chessPosition.charAt(0) - 'A';
        int rank = chessPosition.charAt(1) - '0';
        int row = 8 - rank;

        if (utils.Utils.isValidCoordinate(row, column)) {
            return new Position(row, column);
        }

        return null;
    }

    /**
     * Switches turns between white and black.
     */
    private void switchTurn() {
        if (currentPlayer == whitePlayer) {
            currentPlayer = blackPlayer;
        } else {
            currentPlayer = whitePlayer;
        }
    }
}
