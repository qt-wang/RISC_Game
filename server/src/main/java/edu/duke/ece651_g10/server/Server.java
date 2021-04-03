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
    // The server socket the server is listening to.
    private ServerSocket serverSocket;

    // The factory used to create the map.
    private GameMapFactory mapFactory;

    private GameFactory gameFactory;

    // Each game has a specific identification.
    Game game;
    HashMap<Integer, Game> games;

    // Add a map from client to multiple players.
    // Add a map from client to multiple games.
    HashMap<String, List<Player>> clientInfo;

    HashMap<String, Socket> clientSocket;

    /**
     * Map from client's password -> A list of games where the players are in.
     */
    HashMap<String, List<Game>> clientGames;

    PasswordGenerator serverPasswordGenerator;

    ExecutorService threadPool;


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
        int size = games.size();
        object.put("numberOfGames", size);
        for (int i = 0; i < size; i++) {
            object.put(Integer.toString(i), games.get(i).presentGameInfo());
        }
    }

    /**
     * Append all the open game information to the JSON object object.
     *
     * @param password The provided password from the client.
     * @param object   The object to append the new information
     */
    void appendOpenGameInformation(String password, JSONObject object) {
        Set<Game> inGames = new HashSet<>(clientGames.get(password));
        for (int i = 0; i < games.size(); i++) {
            if (!games.get(i).isGameFull() && inGames.contains(games.get(i))) {
                object.put(Integer.toString(i), games.get(i).presentGameInfo());
            }
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

        //TODO: later abstract out this as a method?
        private void handleJSONObject(JSONObject obj) throws IOException {
            String str = obj.getString("type");

            if (str.equals("ping")) {
                jc.send(generatePongJSON("Hello client, I can here you!"));
                return;
            }
            assert (str.equals("connection"));
            String subType = obj.getString("sub");
            switch (str) {
                case "needPass": {
                    // The user needs a password.
                    // Generate the password.
                    String password = serverPasswordGenerator.generate();
                    clientGames.put(password, new LinkedList<>());
                    clientInfo.put(password, new LinkedList<>());
                    JSONObject response = JSONCommunicator.generateServerResponse("valid", "", "connection");
                    jc.send(response);
                    break;
                }
                case "providePass":
                case "listMyGame": {
                    // Get its password.
                    String providedPassword = obj.getString("password");
                    // Check whether the password is stored in it.
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
                case "listOpenGame": {
                    String providedPassword = obj.getString("password");
                    // Check whether the password is stored in it.
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
                case "joinGame": {
                    String providedPassword = obj.getString("password");
                    int gameId = Integer.parseInt(obj.getString("gameId"));
                    String reason = addPlayerToGame(providedPassword, gameId, socket, jc);
                    if (reason == null) {
                        // We should not monitor on this port anymore.
                        running = false;
                        JSONObject response = JSONCommunicator.generateServerResponse("valid\n", "", "connection");
                        jc.send(response);
                    } else {
                        jc.sendServerInvalidResponse(reason);
                    }
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
                    obj = jc.receive();
                    handleJSONObject(obj);
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
     * @param port    The port the server is listening for.
     * @param factory
     * @throws IOException
     */
    public Server(int port, GameMapFactory factory, PasswordGenerator serverPasswordGenerator) throws IOException {
        setServerSocket(port);
        gameFactory = new V2GameFactory(this);
        this.threadPool = Executors.newCachedThreadPool();
        this.serverPasswordGenerator = serverPasswordGenerator;
        clientSocket = new HashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        clientInfo = new HashMap<>();
        //TODO: shall we only have fixed number of games? what if game ends?
        for (int i = 0; i < 5; i++) {
            games.put(i, gameFactory.createRandomGame());
        }
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
        for (Player p: clientInfo.get(password)) {
            if (game.containsPlayer(p)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Add the player to game with gameId "gameId"
     *
     * @param password The password provided by the user.
     * @param gameId   The game id which represents a open game.
     */
    private synchronized String addPlayerToGame(String password, int gameId, Socket socket, JSONCommunicator jc) {
        String success = checkValidOpenGame(password, gameId);
        if (success != null) {
            return success;
        }
        if (isPlayerInGame(password, gameId)) {
            // Mark the player as enter the game.
            Player p = getPlayerWithPassword(password, gameId);
            assert (p!=null);
            p.joinGame();
        } else {
            // Create a new player, add it to game.
            Player newPlayer = new Player(socket, jc);
            clientInfo.get(password).add(newPlayer);
            game.addPlayer(newPlayer);
        }
        return null;
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
