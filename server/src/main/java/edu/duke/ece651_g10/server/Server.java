package edu.duke.ece651_g10.server;


import edu.duke.ece651_g10.shared.JSONCommunicator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

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

    // Each game has a specific identification.
    // HashMap<Integer, Game> games;
    Game game;

    // Add a map from client to multiple players.
    // Add a map from client to multiple games.
    HashMap<String, List<Player>> clientInfo;

    HashMap<String, Socket> clientSocket;


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
        // This operation will cause the read from the socket block forever if there are
        // no content within the socket.
        //serverSocket.setSoTimeout(0);
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
                case "needPass":
                    // The user needs a password.
                    String password = serverPasswordGenerator.generate();
                    clientSocket.put(password, socket);
                    // Now send the password back.
                    break;
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
        this.mapFactory = factory;
        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(null)))));
        GameMap map = mapFactory.createGameMap(3, 3);
        this.threadPool = Executors.newCachedThreadPool();
        game = new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), 20, 3, this.threadPool, this);
        this.serverPasswordGenerator = serverPasswordGenerator;
        clientSocket = new HashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Create a new game, which should run in a new thread.
     * For later use.
     *
     * @param numPlayer
     * @param numUnitPerPlayer
     * @param numTerritoryPerPlayer
     * @param moveChecker
     * @param attackRuleChecker
     * @param orderProcessor
     */
    private void createGame(int numPlayer, int numUnitPerPlayer, int numTerritoryPerPlayer, RuleChecker moveChecker, RuleChecker attackRuleChecker, OrderProcessor orderProcessor) {

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
