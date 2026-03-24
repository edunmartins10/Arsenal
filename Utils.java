package utils;

/**
 * Utility helper methods for the chess project.
 */
public class Utils {

    /**
     * Checks whether a row and column are inside the board.
     */
    public static boolean isValidCoordinate(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }
}
