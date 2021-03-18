package edu.duke.ece651_g10.server;


import edu.duke.ece651_g10.shared.JSONCommunicator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.function.Function;

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

    private GameBoardView view;

    private int numPlayer;

    private OrderProcessor orderProcessor;

    private boolean gameEnds;
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
     * @param numPlayer                   The number of players allowed in this class.
     * @throws IOException If the port is unavailable.
     */
    public Server(int port, int numUnitPerPlayer, int numTerritoryPerPlayer, int maximumNumberPlayersAllowed, GameMapFactory factory, RuleChecker ruleChecker, int numPlayer, OrderProcessor orderProcessor) throws IOException {
        setServerSocket(port);
        this.numTerritoryPerPlayer = numTerritoryPerPlayer;
        this.numUnitPerPlayer = numUnitPerPlayer;
        this.maximumNumberPlayersAllowed = maximumNumberPlayersAllowed;
        this.ruleChecker = ruleChecker;
        this.mapFactory = factory;
        this.numPlayer = numPlayer;
        players = new HashMap<>();
        setServerSocket(port);
        this.orderProcessor = orderProcessor;
        gameEnds = false;
    }


    // This constructor is for test uses.
    public Server(HashMap<Integer, Player> players) {
        this.players = players;
        gameEnds = false;
    }

    /**
     * only for tests.
     */
    public Server(int port, int numPlayer, int numUnitPerPlayer, int numTerritoryPerPlayer, GameMapFactory factory, RuleChecker ruleChecker, OrderProcessor orderProcessor) throws IOException {
        this.numPlayer = numPlayer;
        this.numTerritoryPerPlayer = numTerritoryPerPlayer;
        this.numUnitPerPlayer = numUnitPerPlayer;
        players = new HashMap<>();
        setServerSocket(port);
        this.mapFactory = factory;
        gameEnds = false;


        this.ruleChecker = ruleChecker;
        this.mapFactory = factory;
        this.numPlayer = numPlayer;
        players = new HashMap<>();
        this.orderProcessor = orderProcessor;
        gameEnds = false;
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
            JSONCommunicator jc = new JSONCommunicator(br, bw);
            String msg = jc.receive().getString("prompt");
            System.out.println("Client：" + msg);
            Player player = new Player(s, jc);
            players.put(player.getPlayerID(), player);
            jc.send(generateInfoJSON(player.getPlayerID(), "You've connected to the server.\n"));
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

    private class UnitsDistributionTask implements Runnable {
        int playerId;
        //CyclicBarrier barrier;

        UnitsDistributionTask(int playerId) {
            this.playerId = playerId;
            //this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                setupInitialUnitsDistribution(playerId);
                //barrier.await();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class PlayOneTurnTask implements Runnable {

        int playerId;
        //CyclicBarrier barrier;

        PlayOneTurnTask(int playerId) {
            this.playerId = playerId;
            //this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                playOneTurn(playerId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void test() {
//        Function<Integer, Runnable> todo = new Function<Integer, Runnable>() {
//            @Override
//            public Runnable apply(Integer integer) {
//                return new UnitsDistributionTask(integer);
//            }
//        }
//    }


    private Function<Integer, Runnable> getUnitsDistributionTask() {
        return new Function<Integer, Runnable>() {
            @Override
            public Runnable apply(Integer integer) {
                return new UnitsDistributionTask(integer);
            }
        };
    }

    private Function<Integer, Runnable> getPlayOneTurnTask() {
        return new Function<Integer, Runnable>() {
            @Override
            public Runnable apply(Integer integer) {
                return new PlayOneTurnTask(integer);
            }
        };
    }


    private void runTasksForAllPlayer(Function<Integer, Runnable> toDo) {
        CyclicBarrier barrier = new CyclicBarrier(players.size() + 1);
        for (int i = 1; i <= players.size(); i++) {
            // We create multiple tasks here.
            Runnable task = toDo.apply(i);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        task.run();
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

//
//    private void runTasksForAllPlayer(int taskNumber) {
//        CyclicBarrier barrier = new CyclicBarrier(players.size());
//        for (int i = 1; i <= players.size(); i++) {
//            int currentPlayerId = i;
//            // We create multiple tasks here.
//            Runnable task;
//            switch (taskNumber) {
//                case 2:
//                    task = new PlayOneTurnTask(i, barrier);
//                    break;
//                default:
//                    task = new UnitsDistributionTask(i, barrier);
//            }
//            Thread t = new Thread(task);
//            t.start();
//        }
//    }


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
        runTasksForAllPlayer(getUnitsDistributionTask());
        Player winner = null;
        // All threads has finished the execution of the units distribution.
        while ((winner = checkGameEnds()) == null) {
            // We create multiple threads to tell the user what to do.
            runTasksForAllPlayer(getPlayOneTurnTask());
            //When this is done.
            orderProcessor.executeEndTurnOrders();
            // Send the updated information to all players.
            //sendToAllPlayer(getWholeGameInformation());
        }
        gameEnds = true;
        String message = "Game ends, the winner is player " + winner.getPlayerID();
        message += "\n";
        sendToAllPlayer(message);
    }

    /**
     * Get all the alive user's information within the map.
     * In the format:
     * Player 1:
     * ---------------------
     * Info ignored.
     * Player 2:
     * ---------------------
     * Info ignored....
     *
     * @return The string representation.
     */
    String getWholeGameInformation() {
        StringBuilder sb = new StringBuilder();
        for (Player p : players.values()) {
            if (!p.getIsLost()) {
                sb.append("Player ");
                sb.append(p.getPlayerID());
                sb.append(":\n");
                sb.append("-----------------------\n");
                sb.append(view.territoryForUser(p));
            }
        }
        return sb.toString();
    }

    private void sendValidResponse(int playerId) throws IOException {
        sendToPlayer(playerId, generateInfoJSON(playerId, "valid\n"));
        //TODO: Change this later to send a inform message.
    }

    private void sendInvalidResponse(int playerId) throws IOException {
        // TODO: Change this later to send a inform message.
        sendToPlayer(playerId, generateInfoJSON(playerId, "invalid\n"));
    }


    private void sendToAllPlayer(String message) throws IOException {
        for (Player p : players.values()) {
            sendToPlayer(p.getPlayerID(), generateInfoJSON(p.getPlayerID(), message));
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
    private Order receiveOrder(int playerId) throws IOException {
        //return null;
        JSONObject info = players.get(playerId).getJCommunicator().receive();
        return toOrder(playerId, info);
    }


    /**
     * Get the player info about the specific player, construct a message like:
     * Player playerId:
     * A (for alive) or L (for lose)
     *
     * @param playerId The player id of the player.
     * @return The string to represent the information about the player.
     */
    String getPlayerInfo(int playerId) {
        boolean isLost = players.get(playerId).getIsLost();
        StringBuilder sb = new StringBuilder();
        sb.append("Player ");
        sb.append(playerId);
        sb.append(":\n");
        if (isLost) {
            sb.append("L\n");
        } else {
            sb.append("A\n");
        }
        return sb.toString();
    }

    /**
     * generate a JSONObject of type: inform
     *
     * @param playerId the player's id
     * @param prompt   the information
     * @return the constructed JSONObject
     */
    public JSONObject generateInfoJSON(int playerId, String prompt) {
        JSONObject info = new JSONObject().put("type", "inform");
        info = info.put("prompt", prompt).put("playerID", playerId);
        boolean isLost = players.get(playerId).getIsLost();
        info = isLost ? info.put("playerStatus", "L") : info.put("playerStatus", "A");
        info = gameEnds ? info.put("playerStatus", "E") : info.put("playerStatus", info.get("playerStatus"));
        return info;
    }

    /**
     * generate a JSONObject of type: inform
     *
     * @param playerId   the player's id
     * @param prompt     the information
     * @param askingType the type of asking
     * @return the constructed JSONObject
     */
    public JSONObject generateAskJSON(int playerId, String prompt, String askingType) {
        assert (askingType.equals("initial") || askingType.equals("regular"));
        JSONObject ask = new JSONObject().put("type", "ask").put("asking", askingType);
        ask = ask.put("prompt", prompt).put("playerID", playerId);
        boolean isLost = players.get(playerId).getIsLost();
        ask = isLost ? ask.put("playerStatus", "L") : ask.put("playerStatus", "A");
        return ask;
    }

    /**
     * Return whether the obj is a order object.
     *
     * @param obj The json object from the client.
     * @return True if the object can be used to construct a order.
     */
    private boolean isOrderMessage(JSONObject obj) {
        String type = getMessageType(obj);
        if (type.equals("order")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return whether the obj is a commit message.
     *
     * @param obj The json object from the client.
     * @return True if the object can be used to construct a commit message.
     */
    private boolean isCommitMessage(JSONObject obj) {
        String type = getMessageType(obj);
        if (type.equals("commit")) {
            return true;
        }
        return false;
    }

    /**
     * get the String mapped to "type" in the JSONObject
     * call this first when you receive any JSONObject before parsing!
     *
     * @param obj a JSONObject
     * @return the content or null if not exists
     */
    public String getMessageType(JSONObject obj) {
        try {
            String ans = obj.getString("type");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the information in a inform JSONObject
     *
     * @param obj the obj received
     * @return the prompt field or null if not exists
     */
    public String getPrompt(JSONObject obj) {
        assert (getMessageType(obj) != null && getMessageType(obj).equals("inform"));
        try {
            String ans = obj.getString("prompt");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * parse order JSONObject into order object
     *
     * @param playerId the id of the player where the obj from
     * @param obj      the JSONObject
     * @return an order object or null if the obj is not a parsable one
     */
    public Order toOrder(int playerId, JSONObject obj) {
        try {
            //Should we check this?
//            if (!isOrderMessage(obj)) {
//                return null;
//            }
            String orderType = obj.getString("orderType"),
                    sourceT = obj.getString("sourceTerritory"),
                    destT = obj.getString("destTerritory");
            int unitNum = obj.getInt("unitNumber");
            if (orderType.equals("move")) {
                Order order = new MoveOrder(playerId, sourceT, destT, unitNum, this.playMap);
                return order;
            } else if (orderType.equals("attack")) {
                Order order = new AttackOrder(playerId, sourceT, destT, unitNum, this.playMap);
                return order;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    void setView(GameBoardTextView view) {
        this.view = view;
    }

    /**
     * Generate the first phase information for every player.
     *
     * @param playerId The player's id.
     * @return The information string for first phase distribution.
     */

//    public String firstPhaseInformation(int playerId) {
//        String str = new String("First phase, soldiers distribution\n");
//        return phaseInformation(str, playerId);
//    }

    /**
     * Provide the phase information used to send to the users.
     *
     * @param phaseInfo The phase info indicates which phase the player in.
     * @param playerId  The player's id number.
     * @return The string representation of the message.
     */
    String phaseInformation(String phaseInfo, int playerId) {
        StringBuilder sb = new StringBuilder(phaseInfo);
        //sb.append(getPlayerInfo(playerId));
        sb.append("Player ");
        sb.append(playerId);
        sb.append(":\n");
        sb.append("-----------------------\n");
        sb.append(view.territoryForUser(players.get(playerId)));
        return sb.toString();
    }

    public JSONObject firstPhaseInformation(int playerId) {
        StringBuilder sb = new StringBuilder("First phase, soldiers distribution\n");
        sb.append(view.territoryForUser(players.get(playerId)));
        return generateInfoJSON(playerId, sb.toString());
    }

//    /**
//     * This message is sent to the player at the beginning of the round.
//     * And after the player send a command back to the server (so that they can see the effect
//     * of their moves.).
//     *
//     * @param playerId              The id number of the player, starts from 1.
//     * @param otherTerritoryMessage The other players' territory information, this should not changed
//     *                              during this round (player should not get other players' information
//     *                              during the round).
//     * @return A String represents the message that was sent to the user.
//     */
//    public String secondPhaseInformation(int playerId, String otherTerritoryMessage) {
//        String str = new String("Second phase, attack territories\n");
//        str = phaseInformation(str, playerId);
//        str += otherTerritoryMessage;
//        return str;
//    }

    public JSONObject secondPhaseInformation(int playerId, String otherTerritoryMessage) {
        //StringBuilder sb = new StringBuilder("Second phase, attack territories\n");
        String str = "Second phase, attack territories\n";
        str = phaseInformation(str, playerId);
        //sb.append(phaseInformation(sb.toString()))
        str += otherTerritoryMessage;
        return generateInfoJSON(playerId, str);
    }

    private JSONObject receiveJSONObject(int playerId) throws IOException {
        JSONObject obj = players.get(playerId).getJCommunicator().receive();
        return obj;
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
        while (!receiveCommit) {
            JSONObject obj = receiveJSONObject(playerId);
            if (isCommitMessage(obj)) {
                sendValidResponse(playerId);
                receiveCommit = true;
                continue;
            }
            Order order = toOrder(playerId, obj);
            if (order == null) {
                sendInvalidResponse(playerId);
                continue;
            }

            // TODO: Remove this later.
            synchronized (this) {
                assert (order instanceof MoveOrder);
//                sendValidResponse(playerId);
//                orderProcessor.acceptOrder(order);
                //String message = ruleChecker.checkOrder(order, this.playMap);
                String message = ruleChecker.checkOrder(order, this.playMap);
                // If valid, then send valid to user.
                if (message == null) {
                    sendValidResponse(playerId);
                    orderProcessor.acceptOrder(order);
                    //sendToPlayer(playerId, firstPhaseInformation(playerId));
                } else {
                    sendInvalidResponse(playerId);
                }
            }
        }
    }


    /**
     * Get all the territory information for all other players (alive player).
     *
     * @param playerId The current player Id
     * @return A String representation of the player information.
     */
    String getEnemyTerritoryInformation(int playerId) {
        // We need to iterate through all the players.
        StringBuilder sb = new StringBuilder();
        for (Player p : players.values()) {
            if (p.getPlayerID() == playerId || p.getIsLost()) {
                continue;
            } else {
                sb.append("Player ");
                sb.append(p.getPlayerID());
                sb.append(":\n");
                sb.append("-----------------------\n");
                sb.append(view.territoryForUser(p));
            }
        }
        return sb.toString();
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
     *
     * @param playerId The player who will receive the playOneTurn message.
     */
    private void playOneTurn(int playerId) throws IOException {
        // Record other player's information, this should not change while this turn.
        String otherTerritoriesInformation = getEnemyTerritoryInformation(playerId);
        //String information = secondPhaseInformation(playerId, otherTerritoriesInformation);
        sendToPlayer(playerId, secondPhaseInformation(playerId, otherTerritoriesInformation));
        boolean receiveCommit = false;
        while (!receiveCommit) {
            JSONObject obj = receiveJSONObject(playerId);
            if (isCommitMessage(obj)) {
                receiveCommit = true;
                continue;
            }
            Order order = toOrder(playerId, obj);
            synchronized (this) {
                String message = ruleChecker.checkOrder(order, playMap);
                if (message == null) {
                    sendValidResponse(playerId);
                    orderProcessor.acceptOrder(order);
                    //sendToPlayer(playerId, secondPhaseInformation(playerId, otherTerritoriesInformation));
                } else {
                    sendInvalidResponse(playerId);
                }
            }
            
        }
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
        return playMap.allBelongsToSamePlayer();
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
