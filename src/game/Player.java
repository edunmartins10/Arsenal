package game;

/**
 * Represents a player in the chess game.
 */
public class Player {

    private String color;

    /**
     * Creates a player with a color.
     */
    public Player(String color) {
        this.color = color;
    }

    /**
     * Returns the player's color.
     */
    public String getColor() {
        return color;
    }
}