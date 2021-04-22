package edu.duke.ece651_g10.server;


import edu.duke.ece651_g10.shared.JSONCommunicator;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class implements the server of the client-server model.
 * The server class is responsible for:2121
 * 1. Wait for the connections and begin the game.
 * 2. At each turn: accept the commands from the players.
 * 3. Execute the attack command at the end of the turn.
 * 4. After the game has end, end the connections and close.
 * Maintained by Guancheng Fu
 */
public class Server {

    static int password = 0;
    // The server socket the server is listening to, no need to be stored in the database.
    private ServerSocket serverSocket;

    // No need to be stored in db.
    private GameFactory gameFactory;

    // Each game has a specific identification.
    // This need to be stored in the database.
    HashMap<Integer, Game> games;

    // Add a map from client to multiple players.
    // Add a map from client to multiple games.
    HashMap<String, List<Player>> clientPlayerInfo;

    // Do not store.
    HashMap<String, Socket> clientSocket;

    /**
     * Map from client's password -> A list of games where the players are in.
     */
    HashMap<String, List<Game>> clientGames;

    PasswordGenerator serverPasswordGenerator;

    ExecutorService threadPool;

    // Not stored.
    volatile HashMap<Game, List<RequestHandleTask>> waitClients;

    /**
     * Get the hash map of client information
     *
     * @return hash map of the client information
     */
    public HashMap<String, List<Player>> getClientInfo() {
        return clientPlayerInfo;
    }

    /**
     * Get the hash map of clientGames
     *
     * @return hash map of the client games
     */
    public HashMap<String, List<Game>> getClientGames() {
        return clientGames;
    }

    /**
     * Setup the server socket.
     *
     * @param port The port to listen to.
     * @throws IOException if the port is unavailable. This exception should be solved in app.
     */
    private void setServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Generate a JSON object of type: pong
     * <p>
     * This is the response to the ping message.
     *
     * @param prompt the information
     * @return the constructed JSONObject
     */
    private static JSONObject generatePongJSON(String prompt) {
        return new JSONObject().put("type", "pong").put("prompt", prompt);
    }


    /**
     * Append all the game information related to client with password "password" to the JSON object.
     *
     * @param password The provided password from the client.
     * @param object   The object to append the new information.
     */
    void appendGameInformation(String password, JSONObject object) {
        List<Game> games = clientGames.get(password);
        int count = 0;
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).gameEnds) {
                continue;
            }
            object.put(Integer.toString(count), games.get(i).presentGameInfo());
            count += 1;
        }
        object.put("numberOfGames", count);
    }

    /**
     * Append all the open game information to the JSON object object.
     *
     * @param password The provided password from the client.
     * @param object   The object to append the new information
     */
    void appendOpenGameInformation(String password, JSONObject object) {
        Set<Game> inGames = new HashSet<>(clientGames.get(password));
        int count = 0;
        for (int i = 0; i < games.size(); i++) {
            if (!games.get(i).isGameFull() && !inGames.contains(games.get(i))) {
                object.put(Integer.toString(count), games.get(i).presentGameInfo());
                count += 1;
            }
        }
        object.put("numberOfGames", count);
    }

    void cancelUserLogin(String password, int gameId, RequestHandleTask task) {
        Game game = games.get(gameId);
        Player p = getPlayerWithPassword(password, gameId);
        synchronized (this) {
            List<RequestHandleTask> temp = waitClients.get(game);
            task.currentGame = null;
            temp.remove(task);
        }
        assert (p != null);
        p.leaveGame();
        synchronized (game) {
            game.getCurrentWaitGroup().increase();
        }
    }

    /**
     * An inner class which is used to handle the multiple connection request from multiple clients.
     * One client connection use one thread to handle it?
     */
    class RequestHandleTask implements Runnable {
        JSONCommunicator jc;
        Socket socket;
        Boolean running;
        Game currentGame;

        private void handleJSONObject(JSONObject obj) throws IOException {
            String str = obj.getString("type");

            if (str.equals("ping")) {
                jc.send(generatePongJSON("Hello client, I can here you!"));
                return;
            }
            assert (str.equals("connection"));
            String subType = obj.getString("sub");
            switch (subType) {
                case "needPass": {
                    // The user needs a password.
                    // Generate the password.
                    String password = serverPasswordGenerator.generate();
                    synchronized (Server.class) {
                        clientGames.put(password, new LinkedList<>());
                        clientPlayerInfo.put(password, new LinkedList<>());
                        // Store the server information.
                        MongoDBClient.addServer2DB(Server.this);
                    }
                    JSONObject response = JSONCommunicator.generateServerResponse("valid", "", "connection");
                    response.put("password", password);
                    jc.send(response);
                    break;
                }
                case "providePass":
                case "listMyGame": {
                    // Get its password.
                    String providedPassword = obj.getString("password");
                    // Check whether the password is stored in it.
                    synchronized (Server.class) {
                        if (clientGames.containsKey(providedPassword)) {
                            // Valid, generate the response JSON object.
                            JSONObject response = JSONCommunicator.generateServerResponse("valid\n", "", "connection");
                            appendGameInformation(providedPassword, response);
                            jc.send(response);
                        } else {
                            // Send invalid response back to it.
                            jc.sendServerInvalidResponse("Invalid password\n");
                        }
                        break;
                    }
                }
                case "listOpenGame": {
                    String providedPassword = obj.getString("password");
                    // Check whether the password is stored in it.
                    synchronized (Server.class) {
                        if (clientGames.containsKey(providedPassword)) {
                            JSONObject response = JSONCommunicator.generateServerResponse("valid\n", "", "connection");
                            // Then append open games to the response, we need to exclude all the games the players in.
                            appendOpenGameInformation(providedPassword, response);
                            jc.send(response);
                        } else {
                            jc.sendServerInvalidResponse("Invalid password\n");
                        }
                        break;
                    }
                }
                case "joinGame": {
                    String providedPassword = obj.getString("password");
                    int gameId = obj.getInt("gameId");
                    String reason = null;
                    // Check whether the game is a valid game for the client.
                    String success = checkValidOpenGame(providedPassword, gameId);
                    if (success != null) {
                        reason = success;
                        jc.sendServerInvalidResponse(reason);
                        break;
                    }
                    joinClientToGame(providedPassword, gameId, socket, jc, this);
                    break;
                }
                case "cancelLogIn": {
                    String providedPassword = obj.getString("password");
                    int gameId = obj.getInt("gameId");
                    cancelUserLogin(providedPassword, gameId, this);
                    break;
                }
                case "createNewGame": {
                    String providedPassword = obj.getString("password");
                    int numberOfPlayers = obj.getInt("numberOfPlayers");
                    Game newGame = gameFactory.createFixedGame(numberOfPlayers);
                    // Add the player into the new game.
                    Player newPlayer = new Player(socket, jc);
                    newPlayer.setTechnologyResourceTotal(20);
                    newPlayer.setFoodResourceTotal(500);
                    clientPlayerInfo.get(providedPassword).add(newPlayer);
                    newGame.addPlayer(newPlayer);
                    MongoDBClient.addGame2DB(newGame);
                    clientGames.get(providedPassword).add(newGame);
                    newPlayer.leaveGame();
                    JSONObject response = JSONCommunicator.generateServerResponse("valid\n", "", "connection");
                    jc.send(response);
                    games.put(newGame.getGameId(), newGame);
                    MongoDBClient.addServer2DB(Server.this);
                    MongoDBClient.addGame2DB(newGame);
                    break;
                }
            }
        }

        @Override
        public void run() {
            //This method should handle all the exceptions.
            // Use Server.this to refer to the outer class.
            JSONObject obj = null;
            while (this.running) {
                //Keep receive new JSON objects from the client.
                try {
                    obj = jc.nonBlockingRead();
                    if (obj != null) {
                        System.out.println(obj);
                        handleJSONObject(obj);
                    }
                    // Check if the wait game can be done.
                    synchronized (RequestHandleTask.class) {
                        if (!this.running) {
                            break;
                        }
                        if (currentGame != null) {
                            // check if it is ready.
                            synchronized (currentGame) {
                                synchronized (Server.class) {
                                    if (currentGame.getCurrentWaitGroup() == null) {
                                        break;
                                    } else {
                                        if (currentGame.getCurrentWaitGroup().getState()) {
                                            // it is done.
                                            // Add all the players into the game.
                                            this.running = false;
                                            for (RequestHandleTask t : waitClients.get(currentGame)) {
                                                t.running = false;
                                            }
                                            waitClients.put(currentGame, new LinkedList<>());
                                            currentGame = null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.running = false;
                }
            }
            System.out.println("finish loop 1");
        }

        RequestHandleTask(JSONCommunicator jc, Socket socket) {
            this.jc = jc;
            this.socket = socket;
            running = true;
        }
    }

    /**
     * Construct a server.
     *
     * @param port The port the server is listening for.
     * @throws IOException
     */
    public Server(int port, PasswordGenerator serverPasswordGenerator) throws IOException {
        setServerSocket(port);
        games = new HashMap<>();
        gameFactory = new V2GameFactory(this);
        this.serverPasswordGenerator = serverPasswordGenerator;
        clientSocket = new HashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        clientPlayerInfo = new HashMap<>();
        clientGames = new HashMap<>();
        waitClients = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            games.put(i, gameFactory.createFixedGame(2));
        }
    }

    /**
     * Construct the server back from the database.
     *
     * @param port                    The port the server is listening for.
     * @param serverPasswordGenerator The server password generator, this should also be resumed from the server.
     * @param gameList                A list of games the server previously owned.
     * @param clientInfoList          The client info list.
     *                                The key is the password, the value is a list of player id that the client use.
     * @param playerList              A list of players that are previously in the server.
     *                                The key is the player id, the value is the player.
     * @param clientGamesInfo         The client games list.
     *                                The key is the password, the value is a list of game id that the client in.
     * @param gameIdentifier          The previous stored game identifier (static variable)
     * @param playerIdentifier        The previous stored player identifier (static variable)
     */
    public Server(int port, PasswordGenerator serverPasswordGenerator, ArrayList<Game> gameList, HashMap<String, List<Integer>> clientInfoList, HashMap<Integer, Player> playerList,
                  HashMap<String, List<Integer>> clientGamesInfo, int gameIdentifier, int playerIdentifier) throws IOException {
        setServerSocket(port);
        Game.setGameIdentifier(gameIdentifier);
        Player.setAvailableId(playerIdentifier);
        games = new HashMap<>();
        for (Game game : gameList) {
            game.setRefServer(this);
            game.setServerTaskPool(this.threadPool);
            games.put(game.getGameId(), game);
        }
        gameFactory = new V2GameFactory(this);
        clientPlayerInfo = new HashMap<>();
        // We need to iterate through the clientInfoList to fit the values back into clientInfo.
        for (Map.Entry<String, List<Integer>> entry : clientInfoList.entrySet()) {
            List<Player> players = new LinkedList<>();
            for (Integer i : entry.getValue()) {
                players.add(playerList.get(i));
            }
            clientPlayerInfo.put(entry.getKey(), players);
        }
        clientSocket = new HashMap<>();
        clientGames = new HashMap<>();
        // We need to iterate through the clientGamesInfo to set it up.
        for (Map.Entry<String, List<Integer>> entry : clientGamesInfo.entrySet()) {
            List<Game> tempGameList = new LinkedList<>();
            for (Integer i : entry.getValue()) {
                tempGameList.add(this.games.get(i));
            }
            clientGames.put(entry.getKey(), tempGameList);
        }
        this.serverPasswordGenerator = serverPasswordGenerator;
        this.threadPool = Executors.newCachedThreadPool();
        waitClients = new HashMap<>();
    }


    /**
     * Check if the game is a valid open game for client with password "password"
     *
     * @param password The password provided by the user.
     * @param gameId   The game id which represents the game.
     * @return If not valid, then send back the reason.
     * Otherwise, return null.
     */
    private String checkValidOpenGame(String password, int gameId) {
        if (!games.containsKey(gameId)) {
            return "Invalid game id\n";
        } else {
            Game game = games.get(gameId);
            boolean playerInGame = isPlayerInGame(password, gameId);
            if (game.isGameFull() && !playerInGame) {
                return "The game is full!\n";
            }
            return null;
        }
    }

    /**
     * Check if player is in this game with game id "gameId"
     *
     * @param password The client's password.
     * @param gameId   The id of the game.
     * @return True if player in game with gameId "gameId"
     * Otherwise, return false.
     */
    private boolean isPlayerInGame(String password, int gameId) {
        Game game = games.get(gameId);
        List<Game> inGame = clientGames.get(password);
        boolean playerInGame = inGame.contains(game);
        return playerInGame;
    }

    private Player getPlayerWithPassword(String password, int GameId) {
        Game game = games.get(GameId);
        synchronized (Server.class) {
            for (Player p : clientPlayerInfo.get(password)) {
                if (game.containsPlayer(p)) {
                    return p;
                }
            }
        }
        return null;
    }

    private void startGame(Game game) {
        Thread newThread = new Thread(game);
        newThread.start();
    }

    /**
     * Add the client to the game.
     *
     * @param providedPassword Client provide this password to the server.
     * @param gameId           The game id.
     * @param task             The request handle task.
     * @param socket           The socket of the related client.
     */
    public void joinClientToGame(String providedPassword, int gameId, Socket socket, JSONCommunicator jc, RequestHandleTask task) throws IOException {
        Game joinedGame = games.get(gameId);
        // Player already in the game, do not need to generate the new player.
        if (isPlayerInGame(providedPassword, gameId)) {
            Player p = getPlayerWithPassword(providedPassword, gameId);
            p.setJCommunicate(jc);
            p.joinGame();
        } else {
            Player newPlayer = new Player(socket, jc);
            newPlayer.setTechnologyResourceTotal(20);
            newPlayer.setFoodResourceTotal(500);
            synchronized (Server.class) {
                clientPlayerInfo.get(providedPassword).add(newPlayer);
                clientGames.get(providedPassword).add(joinedGame);
                MongoDBClient.addServer2DB(this);
            }
            joinedGame.addPlayer(newPlayer);
            synchronized (joinedGame) {
                MongoDBClient.addGame2DB(joinedGame);
            }
        }
        synchronized (RequestHandleTask.class) {
            synchronized (joinedGame) {
                joinedGame.getCurrentWaitGroup().decrease();
                if (joinedGame.getCurrentWaitGroup().getState()) {
                    synchronized (Server.class) {
                        task.running = false;
                        for (RequestHandleTask t : waitClients.get(joinedGame)) {
                            t.currentGame = null;
                            t.running = false;
                        }
                        waitClients.put(joinedGame, new LinkedList<>());
                    }
                } else {
                    task.currentGame = joinedGame;
                    synchronized (Server.class) {
                        if (!waitClients.containsKey(joinedGame)) {
                            waitClients.put(joinedGame, new LinkedList<>());
                        }
                        waitClients.get(joinedGame).add(task);
                    }
                }
                if (joinedGame.canGameStart()) {
                    startGame(joinedGame);
                }
                JSONObject response = JSONCommunicator.generateServerResponse("valid\n", "", "connection");
                jc.send(response);
            }
        }
    }


    /**
     * Keep receive new orders, handle these orders.
     * Order likes:
     * Give out new passwords.
     * Connect one user with one game.
     * newServer.run() will automatically start the game server and run multiple games automatically.
     */
    public void run() throws IOException {
        // Keep listening for new commands forever.
        while (true) {
            Socket s = this.serverSocket.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            JSONCommunicator jc = new JSONCommunicator(br, bw);
            // Create a new task to be handled by threadPool.
            threadPool.execute(new RequestHandleTask(jc, s));
        }
    }

}
