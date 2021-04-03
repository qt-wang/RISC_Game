package edu.duke.ece651_g10.server;

public class V2GameFactory implements GameFactory {

    Server runServer;

    @Override
    public Game createRandomGame() {
        return null;
    }


    @Override
    public Game createFixedGame() {
        return null;
    }

    /**
     * Construct a Game factory.
     *
     * @param runServer The server where the game will run on.
     */
    public V2GameFactory(Server runServer) {
        this.runServer = runServer;
    }
}
