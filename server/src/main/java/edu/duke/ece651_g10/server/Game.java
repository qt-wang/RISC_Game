package edu.duke.ece651_g10.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * This class represents a game that is running on the server.
 * The server may have multiple games that are concurrently running on the server.
 */
public class Game implements Runnable {

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

    private RuleChecker upgradeTechChecker;

    private RuleChecker upgradeUnitChecker;

    private GameBoardView view;

    private OrderProcessor orderProcessor;

    private WaitGroup currentWaitGroup;

    /**
     * These two variables keep the game states.
     * i.e. whether the game is:
     * 1. waiting for enough players.
     * 2. running.
     * 3. ending.
     */
    private boolean gameEnds;

    boolean gameBegins;

    private int numUnitPerPlayer;

    private ExecutorService serverTaskPool;

    Server refServer;


    //TODO: may need to add State, so that the server can detect ended game.
    //public enum State {Waiting, Running, Ending};

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
    public Game(GameMap map, RuleChecker moveRuleChecker, RuleChecker attackRuleChecker, OrderProcessor orderProcessor, GameBoardView view, int numUnitPerPlayer, int numPlayers, ExecutorService serverTaskPool, Server refServer, RuleChecker upgradeTechChecker, RuleChecker upgradeUnitChecker) {
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
        this.serverTaskPool = serverTaskPool;
        this.refServer = refServer;
        this.gameBegins = false;
        this.upgradeUnitChecker = upgradeUnitChecker;
        this.upgradeTechChecker = upgradeTechChecker;
        currentWaitGroup = new WaitGroup(map.getTotalPlayers());
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

    public JSONObject generateServerResponse(String prompt, String reason, String type) {
        JSONObject response = new JSONObject().put("type", type);
        response = response.put("prompt", prompt);
        response = response.put("reason", reason);
        return response;
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
     * Try to receive a json object from player's buffered reader.
     *
     * @param playerId The player from which to read the json object.
     * @return The created json object, if not ready, return null.
     * @throws IOException
     */
    private JSONObject tryReceiveJSONObject(int playerId) throws IOException {
        JSONObject obj = players.get(playerId).getJCommunicator().nonBlockingRead();
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


    private void sendServerValidResponse(int playerId) throws IOException {
        players.get(playerId).getJCommunicator().sendServerValidResponse();
    }

    private void sendServerInvalidResponse(int playerId, String reason) throws IOException {
        //sendToPlayer(playerId, generateServerResponse("invalid\n", reason, "connection"));
        players.get(playerId).getJCommunicator().sendServerInvalidResponse(reason);
    }

    private void sendValidResponse(int playerId) throws IOException {
        sendToPlayer(playerId, generateInfoJSON(playerId, "valid\n", "connection"));
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
            String orderType = obj.getString("orderType");
            if (orderType.equals("move")) {
                String sourceT = obj.getString("sourceTerritory"),
                        destT = obj.getString("destTerritory");
                int uLevel = obj.getInt("unitLevel");
                int unitNum = obj.getInt("unitNumber");
                Order order = new MoveOrder(playerId, sourceT, destT, unitNum, this.playMap, players.get(playerId), uLevel);
                return order;
            } else if (orderType.equals("attack")) {
                String sourceT = obj.getString("sourceTerritory"),
                        destT = obj.getString("destTerritory");
                int uLevel = obj.getInt("unitLevel");
                int unitNum = obj.getInt("unitNumber");
                Order order = new AttackOrder(playerId, sourceT, destT, unitNum, this.playMap, players.get(playerId), uLevel);
                return order;
            } else if (orderType.equals("upgradeUnit")){
                String sourceT = obj.getString("sourceTerritory");
                int uLevel = obj.getInt("unitLevel");
                int unitNum = obj.getInt("unitNumber");
                Order order = new UpgradeUnitOrder(playerId, sourceT, unitNum, this.playMap, uLevel, players.get(playerId));
                return order;
            } else if (orderType.equals("upgradeTech")){
                Order order = new UpgradeTechOrder(playerId, this.playMap, players.get(playerId));
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
     * Check whether receive the quit command from the JSON object object.
     *
     * @param object The json object received from the players.
     * @return True if the received object represents a quit command.
     * Otherwise, return false.
     */
    private boolean checkQuitCommand(JSONObject object) {
        return getMessageType(object).equals("logout");
    }

    /**
     * Handle the quit command:
     * Possible things:
     * 1. mark the player as quit the logic.
     * 2. Ask the server to begin to monitor on the server.
     * This method is actually synchronized.
     */
    private void handleQuitCommand(int playerId, WaitGroup waitGroup) throws IOException {
        // We check again.
        if (waitGroup.getState()) {
            // All the players are ready.
            // just simply go to the next round.
            sendServerInvalidResponse(playerId, "The next round is ready to begin!\n");
        } else {
            // The player is allowed to logout.
            waitGroup.increase();
            Player logOutPlayer = players.get(playerId);
            // Now make game not to monitor on the socket.
            logOutPlayer.leaveGame();
            sendServerValidResponse(playerId);
            this.serverTaskPool.execute(this.refServer.new RequestHandleTask(logOutPlayer.getJCommunicator(), logOutPlayer.getSocket()));
        }
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
        //String otherTerritoriesInformation = getEnemyTerritoryInformation(playerId);
        //String information = secondPhaseInformation(playerId, otherTerritoriesInformation);
        //sendToPlayer(playerId, secondPhaseInformation(playerId, otherTerritoriesInformation));
        Player currentPlayer = players.get(playerId);
        Set<Territory> ownedTerritories = playMap.getTerritoriesForPlayer(currentPlayer);
        Set<Territory> notOwnedTerritories = playMap.getTerritoriesNotBelongToPlayer(currentPlayer);
        JSONObject fixedJSON = generateTerritoriesInfo(notOwnedTerritories);
        sendToPlayer(playerId, generateClientNeededInformation(playerId, "Attack", "valid\n", "", fixedJSON, generateTerritoriesInfo(ownedTerritories)));
        boolean receiveCommit = false;
        while (!receiveCommit) {
            JSONObject obj = receiveJSONObject(playerId);
            if (isCommitMessage(obj)) {
                sendServerValidResponse(playerId);
                receiveCommit = true;
                continue;
            }
            if (handleInvalidLogOutMessage(obj, playerId)) {
                continue;
            }
            synchronized (this) {
                Order order = toOrder(playerId, obj);
                String message = null;
                if (order instanceof MoveOrder) {
                    message = moveRuleChecker.checkOrder(order, playMap);
                } else if (order instanceof AttackOrder) {
                    message = attackRuleChecker.checkOrder(order, playMap);
                } else if (order instanceof UpgradeTechOrder) {
                    message = upgradeTechChecker.checkOrder(order, playMap);
                } else if (order instanceof UpgradeUnitOrder) {
                    message = upgradeUnitChecker.checkOrder(order, playMap);
                }
                if (message == null) {
                    orderProcessor.acceptOrder(order);
                    //sendToPlayer(playerId, secondPhaseInformation(playerId, otherTerritoriesInformation));
                    sendToPlayer(playerId, generateClientNeededInformation(playerId, "Attack", "valid\n", "", fixedJSON, generateTerritoriesInfo(ownedTerritories)));
                } else {
                    sendServerInvalidResponse(playerId, message);
                }
            }
        }
    }

    /**
     * generate the json object which describe the territories information about it.
     * The key is the territories' name, the value is the territories' information.
     *
     * @param territories The territories needed.
     * @return
     */
    public JSONObject generateTerritoriesInfo(Set<Territory> territories) {
        JSONObject result = new JSONObject();
        for (Territory t: territories) {
            result.put(t.getName(), t.presentTerritoryInformation());
        }
        return result;
    }

    /**
     * Generate the JSON object needed to update the information about the server.
     * @param playerId  The player id of the player.
     * @param sub       Can be "Placement" / "Attack"
     * @param prompt    Whether the previous command send from client is valid or not.
     * @param reason    The reason if invalid.
     * @param fixedJSON The fixed part of the territories' info.
     * @param variableJSON  The variable part of the territories' info.
     * @return  The json object sent to the server.
     */
    public JSONObject generateClientNeededInformation(int playerId, String sub, String prompt, String reason,
                                                      JSONObject fixedJSON, JSONObject variableJSON) {
        JSONObject object = new JSONObject();
        object.put("type", "Game").put("sub", sub).put("playerId", playerId).put("prompt", prompt).put("reason", reason);
        // Append the playerStatus
        boolean isLost = players.get(playerId).getIsLost();
        object = isLost ? object.put("playerStatus", "L") : object.put("playerStatus", "A");
        object = gameEnds ? object.put("playerStatus", "E") : object.put("playerStatus", object.get("playerStatus"));
        Player p = players.get(playerId);
        object.put("foodResource", p.getFoodResourceTotal());
        object.put("technologyResource", p.getTechnologyResourceTotal());
        object.put("technologyLevel", p.getTechnologyLevel());
        object.put("canUpgrade", p.getCanUpgradeInThisTurn());
        object.put("TerritoriesInformation", mergeJSONObject(fixedJSON, variableJSON));
        object.put("playerNumber", playMap.getTotalPlayers());
        return object;
    }

    public JSONObject firstPhaseInformation(int playerId) {
        StringBuilder sb = new StringBuilder("First phase, soldiers distribution\n");
        sb.append(view.territoryForUser(players.get(playerId)));
        return generateInfoJSON(playerId, sb.toString(), "placement");
    }


    /**
     * Handle logout message sent from invalid phase.
     * Such as during normal game play.
     *
     * @param object The received JSON object.
     * @return True, if the received message is a logout command.
     * Otherwise, return false.
     */
    private boolean handleInvalidLogOutMessage(JSONObject object, int playerId) throws IOException {
        if (object.getString("type").equals("logout")) {
            // Send invalid message back.
            sendServerInvalidResponse(playerId, "You can only logout after you commit!");
            return true;
        }
        return false;
    }

    static JSONObject mergeJSONObject(JSONObject Obj1, JSONObject Obj2) {
        JSONObject merged = new JSONObject(Obj1, JSONObject.getNames(Obj1));
        for(String key : JSONObject.getNames(Obj2))
        {
            merged.put(key, Obj2.get(key));
        }
        return merged;
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
        Player currentPlayer = players.get(playerId);
        Set<Territory> ownedTerritories = playMap.getTerritoriesForPlayer(currentPlayer);
        Set<Territory> notOwnedTerritories = playMap.getTerritoriesNotBelongToPlayer(currentPlayer);
        JSONObject fixedJSON = generateTerritoriesInfo(notOwnedTerritories);
        sendToPlayer(playerId, generateClientNeededInformation(playerId, "Placement", "valid\n", "", fixedJSON, generateTerritoriesInfo(ownedTerritories)));
        // Get fixed territories, and changeable territories.
        while (!receiveCommit) {
            JSONObject obj = receiveJSONObject(playerId);
            if (isCommitMessage(obj)) {
                sendServerValidResponse(playerId);
                receiveCommit = true;
                continue;
            }
            if (handleInvalidLogOutMessage(obj, playerId)) {
                continue;
            }
            synchronized (this) {
                System.out.println(obj);
                Order order = toOrder(playerId, obj);
                if (order == null) {
                    sendServerInvalidResponse(playerId, "Received null order!");
                    continue;
                }
                assert (order instanceof MoveOrder);
                String message = moveRuleChecker.checkOrder(order, this.playMap);
                // If valid, then send valid to user.
                if (message == null) {
                    orderProcessor.acceptOrder(order);
                    // Send upgrade information back to the client.
                    sendToPlayer(playerId, generateClientNeededInformation(playerId, "Placement", "valid\n", "", fixedJSON, generateTerritoriesInfo(ownedTerritories)));
                    System.out.println("Send back valid response");
                } else {
                    sendServerInvalidResponse(playerId, message);
                    System.out.println("Send back invalid response");
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

    /**
     * Allow the player to logout in this phase.
     */
    private void logOutPhase(int playerId, WaitGroup waitGroup) throws IOException {

        while (!waitGroup.getState()) {
            /**
             * If current player is not join this game,
             * then do not try to receive from its port.
             */
            if (!players.get(playerId).getInGameStatus()) {
                continue;
            }
            // Use non-blocking read to read a json object.
            JSONObject jsonObject = tryReceiveJSONObject(playerId);
            // Handle the logic.
            if (jsonObject != null) {
                // Check whether the json object is of type quit.
                if (checkQuitCommand(jsonObject)) {
                    // We receive a quit command, we handle the quit command.
                    // we check again.
                    synchronized (this) {
                        handleQuitCommand(playerId, waitGroup);
                    }
                } else {
                    sendServerInvalidResponse(playerId, "Can only issue logout command in this state!");
                }
            }
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
                // Can we add a new method at here?
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
        // Generate a waitGroup.
        WaitGroup waitGroup;
        synchronized (this) {
             waitGroup = new WaitGroup(players.size());
            this.currentWaitGroup = waitGroup;
        }
        for (int i = 1; i <= players.size(); i++) {
            // We create multiple tasks here.
            players.get(i).setWaitGroup(waitGroup);
            Runnable task = toDo.apply(i);
            int currentPlayer = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        task.run();
                        /**
                         * At this point, player commit in this round.
                         * ie. Commit after initial units distribution
                         * or Done in round operations.
                         */
                        waitGroup.decrease();

                        /**
                         * Add version 2, LogOut phase.
                         * The player is free to logout after they commit.
                         */
                        logOutPhase(currentPlayer, waitGroup);
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
        return players.size() == numPlayers && !gameBegins && currentWaitGroup.getState();
    }

    public WaitGroup getCurrentWaitGroup () {
        return currentWaitGroup;
    }

    /**
     * Run this game, this should be the only method posted to the outer world.
     * ie. Server newServer(port)
     * newServer.run() will automatically start the game until the game is over.
     */
    @Override
    public void run() {
        // Create the map used in this game.
        gameBegins = true;
        assignInitialTerritories();
        runTasksForAllPlayer(getUnitsDistributionTask());
        System.out.println("Initial units distribution done.");
        Player winner = null;
        // All threads has finished the execution of the units distribution.
        while ((winner = checkGameEnds()) == null) {
            // We create multiple threads to tell the user what to do.
            System.out.println("Ready to play the turn");
            runTasksForAllPlayer(getPlayOneTurnTask());
            //When this is done.
            orderProcessor.executeEndTurnOrders();
            playMap.addUnitToEachTerritory();
            updatePlayerInfo();
            //Update player's food resource and technology resource.
            playMap.updatePlayerResource();
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


    /**
     * Present the game info to the outer world.
     * Currently, display the following information:
     * gameId
     * numberOfTerritories
     * currentPlayers
     * totalPlayers
     *
     * @return
     */
    public JSONObject presentGameInfo() {
        JSONObject response = new JSONObject();
        response.put("gameId", gameId);
        response.put("numberOfTerritories", playMap.getNumberOfTerritoriesPerPlayer());
        response.put("currentPlayer", players.size());
        response.put("totalPlayers", playMap.getTotalPlayers());
        return response;
    }

    /**
     * If the game is full, then return true.
     * Otherwise return false.
     */
    public boolean isGameFull() {
        return players.size() == playMap.getTotalPlayers();
    }

    /**
     * If the game contains the player p, then return true.
     *
     * @param p The player to check.
     * @return True if the game contains the player.
     * Otherwise, return false.
     */
    public boolean containsPlayer(Player p) {
        if (players.containsValue(p)) {
            return true;
        } else {
            return false;
        }
    }
}
