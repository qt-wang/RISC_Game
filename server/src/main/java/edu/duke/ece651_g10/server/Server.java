package edu.duke.ece651_g10.server;



import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * This class implements the server of the client-server model.
 * The server class is responsible for:
 * 1. Wait for the connections and begin the game.
 * 2. At each turn: accept the commands from the players.
 * 3. Execute the attack command at the end of the turn.
 * 4. After the game has end, end the connections and close.
 * Maintained by Guancheng Fu
 */
public class Server {
    // The number of units belong to each person.
    private int numUnitPerPlayer;

    // The number of territories belong to each player.
    private int numTerritoryPerPlayer;

    private int maximumNumberPlayersAllowed;

    // Not sure.
    private HashMap<Integer, Player> players;

    // The server socket the server is listening to.
    private ServerSocket serverSocket;

    // The game map of the game.
    private GameMap playMap;

    // The factory used to create the map.
    private GameMapFactory mapFactory;

    // The rule checker used to check the rules of the game.
    private RuleChecker ruleChecker;

    /**
     * Setup the server socket.
     *
     * @param port The port to listen to.
     * @throws IOException if the port is unavailable. This exception should be solved in app.
     */
    private void setServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        // This operation will cause the read from the socket block forever if there are
        // no content within the socket.
        serverSocket.setSoTimeout(0);
    }

    /**
     * Begin a server.
     * Begins to listen on a specific port.
     *
     * @param port                        The port to listen to.
     * @param numUnitPerPlayer            Number of units per player has when the game begin.
     * @param numTerritoryPerPlayer       Number of territories per player has when the game begin.
     * @param maximumNumberPlayersAllowed The maximum number of players allowed in the game.
     * @param factory                     The factory used to generate the map.
     * @param ruleChecker                 The ruleChecker is used to check the validness of the orders.
     *                                    Assume that the ruleChecker can check any commands by entering ruleCheck.checkOrder(order);
     * @throws IOException If the port is unavailable.
     */
    public Server(int port, int numUnitPerPlayer, int numTerritoryPerPlayer, int maximumNumberPlayersAllowed, GameMapFactory factory, RuleChecker ruleChecker) throws IOException {
        setServerSocket(port);
        this.numTerritoryPerPlayer = numTerritoryPerPlayer;
        this.numUnitPerPlayer = numUnitPerPlayer;
        this.maximumNumberPlayersAllowed = maximumNumberPlayersAllowed;
        this.ruleChecker = ruleChecker;
        this.mapFactory = factory;
        players = new HashMap<>();
    }

    /**
     * This method begins listen to the socket and accept connections from the client.
     * The server will end this stage if:
     * 1. If There are 5 players (The maximum number of players allowed).
     * 2. If there are at least 2 players and there are no connections in wait seconds.
     * This method should also setup the players field of the class.
     *
     * @param waitSeconds The maximum time the server will wait if no other players try to wait.
     */
    private void acceptConnections(int waitSeconds) {

    }

    /**
     * Run this game, this should be the only method posted to the outer world.
     * ie. Server newServer(port)l
     * newServer.run() will automatically start the game until the game is over.
     */
    public void run() {

    }


    /**
     * Assign territories to each player, changed the attributes for the territories.
     * Each player shall pick (or be assigned) one such group as her starting territories.
     */
    private void assignInitialTerritories() {

    }

    /**
     * Setup the units distribution of the territories for each player.
     * Each player shall have the same number of initial units, which she may place in her territories as she wishes.
     * This phase should occur simultaneously.
     * Consider using a ThreadPool for this, and waiting all the threads to be done.
     */
    private void setupInitialUnitsDistribution() {

    }

    /**
     * Play one turn of the game.
     * One turn of the game includes:
     * 0. Distribute the current map to the player (The player's own view).
     * 1. Read inputs from each user (Or orders)
     * -- Print the related map information to the user.
     * 2. Wait until each player to enter the commit message (What is the commit message?)
     * 0, 1, 2 might be handled in multi-thread functions, ie listenForCommands.
     * 3. After timeout seconds if no player are enter new commands, end the turn.
     * 4. Execute all the commands.
     * 5. Change the ownership of the territories, add one unit to each territory.
     * 6. Distributes the result.
     */
    private void playOneTurn(int timeout) {

    }

    /**
     * This function might do the following things until receive the commit command from the user.
     * 0. Distribute the current map to the player (The player's own view).
     * 1. Read inputs from each user (Or orders)
     * 2. Verify the commands use the RuleChecker.
     * -- Print the related map information to the user.
     *
     * @param timeout
     */
    private void listenForCommands(int timeout) {

    }


    /**
     * Return the player who has won the game.
     *
     * @return Player if the game ends and the player has won the game.
     * @reutrn null If the game is not end.
     */
    private Player checkGameEnds() {
        return null;
    }

    /**
     * Generate the win information for the game.
     *
     * @param player The winner of the game.
     * @return
     */
    private String distributeWinInfo(Player player) {
        return null;
    }

    /**
     * Invoke this function at the end of the turn.
     * This function should check the condition of each player.
     * If the player has lost, set up a flag on the player.
     */
    private void checkPlayerConditions() {

    }

}
