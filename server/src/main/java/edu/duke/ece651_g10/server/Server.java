package edu.duke.ece651_g10.server;


import edu.duke.ece651_g10.shared.JSONCommunicator;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.*;

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
    private RuleChecker moveOrderChecker;

    private RuleChecker attackOrderChecker;

    private GameBoardView view;

    private int numPlayer;

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
        //serverSocket.setSoTimeout(0);
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
     * @param moveOrderChecker            The moveOrderChecker is used to check the validness of the move Orders.
     * @param attackOrderChecker          The attackOrderChecker is used to check the validness of the attack orders.
     *                                    Assume that the ruleChecker can check any commands by entering ruleCheck.checkOrder(order);
     * @param numPlayer                   The number of players allowed in this class.
     * @throws IOException If the port is unavailable.
     */
    public Server(int port, int numUnitPerPlayer, int numTerritoryPerPlayer, int maximumNumberPlayersAllowed, GameMapFactory factory, RuleChecker moveOrderChecker, RuleChecker attackOrderChecker, int numPlayer) throws IOException {
        setServerSocket(port);
        this.numTerritoryPerPlayer = numTerritoryPerPlayer;
        this.numUnitPerPlayer = numUnitPerPlayer;
        this.maximumNumberPlayersAllowed = maximumNumberPlayersAllowed;
        this.moveOrderChecker = moveOrderChecker;
        this.attackOrderChecker = attackOrderChecker;
        this.mapFactory = factory;
        this.numPlayer = numPlayer;
        players = new HashMap<>();
        setServerSocket(port);
    }


    // This constructor is for test uses.
    public Server(HashMap<Integer, Player> players) {
        this.players = players;
    }

    /**
     * only for tests.
     */
    public Server(int port, int numPlayer, int numUnitPerPlayer, int numTerritoryPerPlayer, GameMapFactory factory) throws IOException {
        this.numPlayer = numPlayer;
        this.numTerritoryPerPlayer = numTerritoryPerPlayer;
        this.numUnitPerPlayer = numUnitPerPlayer;
        players = new HashMap<>();
        setServerSocket(port);
        this.mapFactory = factory;
    }


    /**
     * This method begins listen to the socket and accept connections from the client.
     * The server will end this stage if:
     * 1. If There are 5 players (The maximum number of players allowed).
     * 2. If there are at least 2 players and there are no connections in wait seconds.
     * This method should also setup the players field of the class.
     * This method should fill the hashmap (called players)
     * For each player, it should has a number associated with him, starts from 0.
     * This should also setup the socket of the player.
     */
    //TODO: Change to private later and put it into the run function.
    public void acceptConnections() throws InterruptedException, IOException {
        int connectedPlayer = 0;
        //accept connections from the clients
        while (connectedPlayer < this.numPlayer) {
            System.out.println("Waiting for client to connect");
            Socket s = this.serverSocket.accept();
            System.out.println("Connected with client");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            JSONCommunicator jc = new JSONCommunicator(br,bw);
            String msg = jc.receive().getString("prompt");
            System.out.println("Client：" + msg);
            Player player = new Player(s, jc);
            players.put(player.getPlayerID(), player);
            jc.send(generateInfoJSON(player.getPlayerID(),"You've connected to the server.\n"));
            connectedPlayer += 1;
        }
    }


    private void setUpMap() {
        System.out.println("Setup map!");
        int numberOfPlayers = players.size();
        this.playMap = mapFactory.createGameMap(this.numPlayer, numTerritoryPerPlayer);
        // TODO: Setup the view to be used in this class.
        view = new GameBoardTextView(playMap);
        System.out.println("Finish setup");
    }


    /**
     * Run this game, this should be the only method posted to the outer world.
     * ie. Server newServer(port)
     * newServer.run() will automatically start the game until the game is over.
     */
    public void run() throws IOException, InterruptedException {
        // Create the map used in this game.
        acceptConnections();
        setUpMap();
        assignInitialTerritories();
        CyclicBarrier unitsDistributionBarrier = new CyclicBarrier(players.size());
        for (int i = 1; i <= players.size(); i++) {
            //Create a thread to run.
            int port = players.get(i).getSocketNumber();
            int currentPlayerId = i;
            Thread t = new Thread() {
                @Override
                public void run() {
                    //TODO: Change this later to add arguments.
                    try {
                        setupInitialUnitsDistribution(currentPlayerId);
                        unitsDistributionBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
        // All threads has finished the execution of the units distribution.
        while (checkGameEnds() == null) {
            playOneTurn(30);
        }
    }

    //TODO:Implement.
    private void sendToPlayer(int playerId, JSONObject obj) throws IOException {
        Player p = players.get(playerId);
        p.getJCommunicator().send(obj);
    }

    /**
     * Assign territories to each player, changed the attributes for the territories.
     * Each player shall pick (or be assigned) one such group as her starting territories.
     */
    private void assignInitialTerritories() {
        HashMap<Integer, HashSet<Territory>> groups = playMap.getInitialGroups();
        for (int i = 1; i <= players.size(); i++) {
            // Get the player.
            Player p = players.get(i);
            Territory end = null;
            for (Territory t : groups.get(i)) {
                t.setOwner(p);
                end = t;
            }
            end.setUnitNumber(numUnitPerPlayer);
        }
    }

    //TODO: Change this later.
    private Order receiveOrder(int port) {
        return null;
    }


//    /**
//     * Get the player info about the specific player, construct a message like:
//     * Player playerId:
//     * A (for alive) or L (for lose)
//     *
//     * @param playerId The player id of the player.
//     * @return The string to represent the information about the player.
//     */
//    String getPlayerInfo(int playerId) {
//        boolean isLost = players.get(playerId).getIsLost();
//        StringBuilder sb = new StringBuilder();
//        sb.append("Player ");
//        sb.append(playerId);
//        sb.append(":\n");
//        if (isLost) {
//            sb.append("L\n");
//        } else {
//            sb.append("A\n");
//        }
//        return sb.toString();
//    }

    /**
     * generate a JSONObject of type: inform
     * @param content the information
     * @return the constructed JSONObject
     */
    private JSONObject generateInfoJSON(int playerId, String content){
        JSONObject info = new JSONObject().put("type","inform");
        info = info.put("prompt",content).put("playerID",playerId);
        boolean isLost = players.get(playerId).getIsLost();
        info = isLost ? info.put("playerStatus","L") : info.put("playerStatus","A");
        return info;
    }

    /**
     * Generate the first phase information for every player.
     *
     * @param playerId The player's id.
     * @return The information string for first phase distribution.
     */
    public JSONObject firstPhaseInformation(int playerId) {
        StringBuilder sb = new StringBuilder("First phase, soldiers distribution\n");
        sb.append(view.territoryForUser(players.get(playerId)));
        return generateInfoJSON(playerId,sb.toString());
    }

    /**
     * Setup the units distribution of the territories for each player.
     * Each player shall have the same number of initial units, which she may place in her territories as she wishes.
     * This phase should occur simultaneously.
     * Consider using a ThreadPool for this, and waiting all the threads to be done.
     */
    private void setupInitialUnitsDistribution(int playerId) throws IOException {
        // Assume we can receive orders from the client.
        // Send the view to the user.
        // The player should not see the units distributions of other players.
        boolean receiveCommit = false;

        sendToPlayer(playerId, firstPhaseInformation(playerId));
        // also append the information.
//        while (!receiveCommit) {
//
//        }
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
