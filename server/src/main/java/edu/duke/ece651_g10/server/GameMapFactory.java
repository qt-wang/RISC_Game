package edu.duke.ece651_g10.server;

/**
 * This class will be a factory used to create maps based on the arguments given.
 * Maintained by Guancheng Fu
 */
public interface GameMapFactory {
    /**
     * Create the map for the game.
     * A map should contain a list of territories.
     * Each territory should have its neighbors, and form a connected graph.
     * @param numberOfPlayers      The number of Players within the game.
     * @return The map generated for this game.
     */
    public GameMap createGameMap(int numberOfPlayers);
}
