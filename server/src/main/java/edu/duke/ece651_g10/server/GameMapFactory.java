package edu.duke.ece651_g10.server;

/**
 * This class will be a factory used to create maps based on the arguments given.
 */
public interface GameMapFactory {
    /**
     * Create the map for the game.
     *
     * @param numberOfPlayers      The number of Players within the game.
     * @param territoriesPerPlayer The territories belong to each player.
     * @return The map generated for this game.
     */
    public GameMap createGameMap(int numberOfPlayers, int territoriesPerPlayer);
}
