package edu.duke.ece651_g10.server;

/**
 * A Game factory used to generate games.
 */
public interface GameFactory {

    public Game createFixedGame(int people);

}
