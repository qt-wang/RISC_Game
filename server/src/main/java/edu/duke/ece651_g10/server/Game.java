package edu.duke.ece651_g10.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Function;

/**
 * This class represents a game that is running on the server.
 * The server may have multiple games that are concurrently running on the server.
 */
public class Game implements Runnable{

    // Indicates the players in this game.
    // Each player has a unique integer represent its identity.
    private HashMap<Integer, Player> players;

    static int gameIdentifier = 0;

    //This is the unique game identification of the game.
    private int gameId;

    private int numPlayers;
    // The game map of the game.
    private GameMap playMap;

    private RuleChecker moveRuleChecker;

    private RuleChecker attackRuleChecker;

    private GameBoardView view;

    private OrderProcessor orderProcessor;

    private boolean gameEnds;

    private int numUnitPerPlayer;

    /**
     * Construct a game based on the arguments given in the argument list.
     *
     * @param map               The play map used in this game.
     * @param moveRuleChecker   The rule checker used to check whether the move order is valid or not.
     * @param attackRuleChecker The attack checker used to check whether the attack order is valid or not.
     * @param orderProcessor    An order processor used to process the orders.
     * @param view              The text board view used to represent this game.
     * @param numUnitPerPlayer  The number of units belong to a player in this game.
     */
    public Game(GameMap map, RuleChecker moveRuleChecker, RuleChecker attackRuleChecker, OrderProcessor orderProcessor, GameBoardView view, int numUnitPerPlayer, int numPlayers) {
        this.playMap = map;
        this.players = new HashMap<>();
        this.moveRuleChecker = moveRuleChecker;
        this.attackRuleChecker = attackRuleChecker;
        this.view = view;
        this.orderProcessor = orderProcessor;
        this.gameEnds = false;
        this.numUnitPerPlayer = numUnitPerPlayer;
        this.numPlayers = numPlayers;
        synchronized (Game.class) {
            this.gameId = gameIdentifier++;
        }
    }

    public int getGameId() {
        return this.gameId;
    }

    /**
     * Add a new player to this game.
     *
     * @param p The new player's information.
     */
    public void addPlayer(Player p) {
        int playerNums = players.size();
        p.joinGame();
        if (!players.containsValue(p) && playerNums < numPlayers) {
            System.out.println("Player added successfully");
            players.put(playerNums + 1, p);
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
     * generate a JSONObject of type: inform
     *
     * @param playerId the player's id
     * @param prompt   the information
     * @return the constructed JSONObject
     */
    public JSONObject generateInfoJSON(int playerId, String prompt, String type) {
        JSONObject info = new JSONObject().put("type", type);
        info = info.put("prompt", prompt).put("playerID", playerId);
        boolean isLost = players.get(playerId).getIsLost();
        info = isLost ? info.put("playerStatus", "L") : info.put("playerStatus", "A");
        info = gameEnds ? info.put("playerStatus", "E") : info.put("playerStatus", info.get("playerStatus"));
        return info;
    }


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

    public JSONObject secondPhaseInformation(int playerId, String otherTerritoryMessage) {
        //StringBuilder sb = new StringBuilder("Second phase, attack territories\n");
        String str = "Second phase, attack territories\n";
        str = phaseInformation(str, playerId);
        //sb.append(phaseInformation(sb.toString()))
        str += otherTerritoryMessage;
        return generateInfoJSON(playerId, str, "play");
    }

    private void sendToPlayer(int playerId, JSONObject obj) throws IOException {
        Player p = players.get(playerId);
        p.getJCommunicator().send(obj);
    }

    private JSONObject receiveJSONObject(int playerId) throws IOException {
        JSONObject obj = players.get(playerId).getJCommunicator().receive();
        return obj;
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


    private void sendValidResponse(int playerId) throws IOException {
        sendToPlayer(playerId, generateInfoJSON(playerId, "valid\n", "test"));
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
            String orderType = obj.getString("orderType"),
                    sourceT = obj.getString("sourceTerritory"),
                    destT = obj.getString("destTerritory");
            int unitNum = obj.getInt("unitNumber");
            if (orderType.equals("move")) {
                Order order = new MoveOrder(playerId, sourceT, destT, unitNum, this.playMap);
                return order;
            } else if (orderType.equals("attack")) {
                Order order = new AttackOrder(playerId, sourceT, destT, unitNum, this.playMap, players.get(playerId));
                return order;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendInvalidResponse(int playerId) throws IOException {
        sendToPlayer(playerId, generateInfoJSON(playerId, "invalid\n", "test"));
    }

    /**
     * Play one turn of the game.
     * Initially, the player should be in the game.
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
                sendValidResponse(playerId);
                receiveCommit = true;
                continue;
            }
            synchronized (this) {
                Order order = toOrder(playerId, obj);
                String message = null;
                if (order instanceof MoveOrder) {
                    message = moveRuleChecker.checkOrder(order, playMap);
                } else if (order instanceof AttackOrder) {
                    message = attackRuleChecker.checkOrder(order, playMap);
                } else {
                    assert (false);
                }
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


    public JSONObject firstPhaseInformation(int playerId) {
        StringBuilder sb = new StringBuilder("First phase, soldiers distribution\n");
        sb.append(view.territoryForUser(players.get(playerId)));
        return generateInfoJSON(playerId, sb.toString(), "placement");
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
            synchronized (this) {
                Order order = toOrder(playerId, obj);
                if (order == null) {
                    sendInvalidResponse(playerId);
                    continue;
                }
                assert (order instanceof MoveOrder);
//                sendValidResponse(playerId);
//                orderProcessor.acceptOrder(order);
                //String message = ruleChecker.checkOrder(order, this.playMap);
                String message = moveRuleChecker.checkOrder(order, this.playMap);
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

    /**
     * Return the player who has won the game.
     *
     * @return Player if the game ends and the player has won the game.
     * @reutrn null If the game is not end.
     */
    private Player checkGameEnds() {
        return playMap.allBelongsToSamePlayer();
    }


    private void updatePlayerInfo() {
        for (Player p : players.values()) {
            if (p.getIsLost()) {
                continue;
            } else {
                if (playMap.getTerritoriesForPlayer(p).size() == 0) {
                    p.setIsLost();
                }
            }
        }
    }

    private void sendToAllPlayer(String message, String type) throws IOException {
        for (Player p : players.values()) {
            sendToPlayer(p.getPlayerID(), generateInfoJSON(p.getPlayerID(), message, type));
        }
    }

    public int getNumPlayers() {
        return players.size();
    }

    public boolean canGameStart() {
        return players.size() == numPlayers;
    }

    /**
     * Run this game, this should be the only method posted to the outer world.
     * ie. Server newServer(port)
     * newServer.run() will automatically start the game until the game is over.
     */
    @Override
    public void run() {
        // Create the map used in this game.
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
            playMap.addUnitToEachTerritory();
            updatePlayerInfo();
        }
        gameEnds = true;
        String message = "Game ends, the winner is player " + winner.getPlayerID();
        message += "\n";
        try {
            sendToAllPlayer(message, "play");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
