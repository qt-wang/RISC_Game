package edu.duke.ece651_g10.server;

/**
 * This interface indicates what methods should a view has to display all its information to its players.
 * Maintained by Guancheng Fu.
 */
public interface GameBoardView {
    /**
     * Return a string which should describe the territory information about the player.
     * @param player The player who should own the territories.
     * @return The string representation about the information.
     */
    public String territoryForUser(Player player);
}
