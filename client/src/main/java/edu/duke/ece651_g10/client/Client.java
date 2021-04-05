package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The client of the game
 */
public class Client {
    final PrintStream out;
    final BufferedReader inputReader;

    private JSONObject currentJSON;
    public String playerStatus;

    int playerID;
    final HashSet<String> normalOrderSet;
    final HashMap<String, String> orderKeyValue;
    final HashMap<String, Runnable> commandMap;
    public SocketClient socketClient;
    HashMap<Integer, JSONObject> listedGames;
    private String password;

    public SocketClient getSocketClient() {
        return socketClient;
    }
    /**
     * The constructor of the Client
     *
     * @param out          The print stream
     * @param input        The buffered reader
     * @param socketClient the socket client
     */
    public Client(PrintStream out, BufferedReader input, SocketClient socketClient) throws IOException {
        this.playerStatus = "A";
        this.out = out;
        this.inputReader = input;
        this.socketClient = socketClient;
        socketClient.send(generatePingJSON("Testing, server, can you hear me?\n"));
        //JSONObject ans = socketClient.receive();
        setCurrentJSON(socketClient.receive());
        String prompt = currentJSON.getString("prompt");
        System.out.println(prompt);
        //FGC added
        //this.playerID = getPlayerId(ans);

        this.normalOrderSet = new HashSet<String>();
        normalOrderSet.add("M");
        normalOrderSet.add("A");

        this.orderKeyValue = new HashMap<String, String>();
        orderKeyValue.put("M", "move");
        orderKeyValue.put("A", "attack");
        orderKeyValue.put("D", "commit");

        this.commandMap = new HashMap<String, Runnable>();
        commandMap.put("placement", () -> {
            try {
                doPlacement();
            } catch (IOException e) {
                out.println("Meet IO exception.");
            }
        });
        commandMap.put("play", () -> {
            try {
                playGame();
            } catch (IOException e) {
                out.println("Meet IO exception.");
            }
        });
        commandMap.put("connection", () -> {
            try {
                //Changed by FGC
                //FGCConnection();
                connectGame();
            } catch (IOException e) {
                out.println("Meet IO exception.");
            }
        });
    }

    public void setListedGames(HashMap<Integer, JSONObject> listedGames) {
        this.listedGames = listedGames;
    }

    public HashMap<Integer, JSONObject> getListedGames() {
        return this.listedGames;
    }

    public void setCurrentJSON(JSONObject currentJSON) {
        this.currentJSON = currentJSON;
    }

    /**
     * generate a JSONObject of type: connection
     *
     * @param password the password to join the game
     * @return the constructed JSONObject
     */
    public JSONObject generateConnectJSON(String password) throws IOException {
        if (!password.equals("")) {
            return new JSONObject().put("type", "connection").put("sub", "providePass").put("password", password);
        } else {
            return new JSONObject().put("type", "connection").put("sub", "needPass");
        }
    }


    public void setPassword(String password) {
        this.password = password;
    }

    /**S
     * Send password to server.
     * @param password
     * @return
     */
    public JSONObject sendPasswordToServer(String password) {
        return new JSONObject().put("type", "connection").put("sub", "providePass").put("password", password);
    }


    /**
     * generate a JSONObject of type: inform
     *
     * @param prompt the information
     * @return the constructed JSONObject
     */
    public JSONObject generateInfoJSON(String prompt) {
        return new JSONObject().put("type", "inform").put("prompt", prompt);
    }

    /**
     * FGCAdded
     * Generate a JSONobject of type:ping
     * ping is used to test the connection with the server.
     *
     * @param prompt The information
     * @return the constructed JSON object.
     */
    public JSONObject generatePingJSON(String prompt) {
        return new JSONObject().put("type", "ping").put("prompt", prompt);
    }


    public void sendListOpenGameJSON() throws IOException {
        JSONObject object = new JSONObject().put("type", "connection").put("sub", "listOpenGame").put("password", password);
        this.socketClient.send(object);
    }

    /**
     * generate a commit JSONObject used to let the server end the receiving order
     * phase
     *
     * @return a newly constructed JSONObject with only one field "type" to be
     * "commit"
     */
    public JSONObject generateCommitJSON() {
        return new JSONObject().put("type", "commit");
    }

    /**
     * generate a JSONObject that contains order info
     *
     * @param orderType the type of the order, currently only attack and move
     * @param sourceT   the source territory name
     * @param destT     the destination territory name
     * @param unitNum   the number of units
     * @return the constructed JSONObject
     */
    public JSONObject generateOrderJSON(String orderType, String sourceT, String destT, int unitNum) {
        assert (orderType.equals("attack") || orderType.equals("move"));
        return new JSONObject().put("type", "order").put("orderType", orderType).put("sourceTerritory", sourceT)
                .put("destTerritory", destT).put("unitNumber", unitNum);
    }

    /**
     * get the String mapped to "type" in the JSONObject
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
     * get the String mapped to "type" in the JSONObject
     *
     * @return the content or null if not exists
     */
    public String getCurrentMessageType() {
        try {
            String ans = currentJSON.getString("type");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the player's id from the JSONObject
     *
     * @param obj a JSONObject
     * @return the player's id or null if not exists
     */
    public Integer getPlayerId(JSONObject obj) {
        try {
            int ans = obj.getInt("playerID");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the player status field from the json object
     *
     * @param obj the json object
     * @return the string representing the player's status
     */
    public String getPlayerStatus(JSONObject obj) {
        try {
            String ans = obj.getString("playerStatus");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the Prompt field from the json object
     *
     * @param obj the json object
     * @return the prompt string
     */
    public String getPrompt(JSONObject obj) {
        try {
            String ans = obj.getString("prompt");
            return ans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read the input from the user
     *
     * @param prompt The String will print in the terminal before the user inputs
     *               data
     * @return the string that user input
     */
    public String readString(String prompt) throws IOException {
        out.println(prompt);
        String str = inputReader.readLine();
        return str;
    }

    /**
     * Read the action from the user
     *
     * @param prompt        The String will print in the terminal before the user
     *                      inputs data
     * @param legalInputSet The set of legal input
     * @return the valid action
     */
    public String readAction(String prompt, HashSet<String> legalInputSet) throws IOException {
        String action = readString(prompt);
        if (!legalInputSet.contains(action.toUpperCase())) {
            out.println("Please input valid actions.");
            return readAction(prompt, legalInputSet);
        }
        return action.toUpperCase();
    }

    /**
     * Read the number from the user
     *
     * @param prompt The String will print in the terminal before the user inputs
     *               data
     * @return A number
     */
    public String readInteger(String prompt) throws IOException {
        try {
            int number = Integer.parseInt(readString(prompt));
            return String.valueOf(number);
        } catch (NumberFormatException e) {
            out.println("Please input valid integer.");
            return readInteger(prompt);
        }
    }

    /**
     * Generate the order string based on the user input
     *
     * @param prompt        The String will print in the terminal before the user
     *                      inputs data
     * @param legalOrderSet The order set
     * @return the ArrayList of the String describes the order
     */
    public ArrayList<String> generateOrderString(String prompt, HashSet<String> legalOrderSet) throws IOException {
        ArrayList<String> orderStringList = new ArrayList<String>();
        String orderType = readAction(prompt, legalOrderSet);
        orderStringList.add(orderType);
        if (normalOrderSet.contains(orderType)) {
            orderStringList.add(readString("Please input your source territory name:"));
            orderStringList.add(readString("Please input your target territory name:"));
            orderStringList.add(readInteger("Please input the number of nuit:"));
        } else {
            for (int i = 0; i < 3; i++) {
                orderStringList.add("");
            }
        }
        return orderStringList;
    }

    /**
     * Send order to server
     *
     * @param orderJSON The order JSON describes the Order
     */
    public void sendOrderToServer(JSONObject orderJSON) throws IOException {
        socketClient.send(orderJSON);
    }

    /**
     * Send order
     *
     * @param prompt the information will print in the terminal before user input
     * @return the ArrayList of the String describes the order
     */
    public ArrayList<String> sendOrder(String prompt, HashSet<String> legalOrderSet) throws IOException {
        ArrayList<String> orderString = generateOrderString(prompt, legalOrderSet);
        if (normalOrderSet.contains(orderString.get(0))) {
            JSONObject orderJSON = generateOrderJSON(orderKeyValue.get(orderString.get(0)), orderString.get(1),
                    orderString.get(2), Integer.parseInt(orderString.get(3)));
            sendOrderToServer(orderJSON);
        } else {
            JSONObject orderJSON = generateCommitJSON();
            sendOrderToServer(orderJSON);
        }
        if (getPrompt(socketClient.receive()).equals("invalid\n")) {
            out.println("Your last order is invalid, please input your order again");
            orderString = sendOrder(prompt, legalOrderSet);
        }
        return orderString;
    }


    private void FGCConnection() throws IOException {
        // Receive the password.
        //JSONObject object = socketClient.receive();
        String password = currentJSON.getString("password");

        System.out.println(password);
        // Send a JSON object which contains the password.
        socketClient.send(generateConnectJSON(password));
    }

    /**
     * Connect to one of the game
     * FGC changed.
     */
    public void connectGame() throws IOException {
        out.println(getPrompt(this.currentJSON));
        String prompt = "If you already join a game, please input the password.\n if you want to join a new game, please press Enter.";
        String password = readString(prompt);
        sendOrderToServer(generateConnectJSON(password));
        System.out.println(currentJSON);
        setCurrentJSON(socketClient.receive());
        password = currentJSON.getString("password");
        if (getPrompt(this.currentJSON).equals("invalid\n")) {
            connectGame();
        }
        sendOrderToServer(generateConnectJSON(password));
    }

    /**
     * Place the units at beginning of the game
     */
    public void doPlacement() throws IOException {
        out.println(getPrompt(this.currentJSON));
        //FGC added
        this.playerID = getPlayerId(this.currentJSON);
        String prompt = "You can move your units now.\n   (M)ove\n   (D)one";
        HashSet<String> legalOrderSet = new HashSet<String>();
        legalOrderSet.add("M");
        legalOrderSet.add("D");
        ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
        while (!orderString.get(0).equals("D")) {
            orderString.clear();
            orderString = sendOrder(prompt, legalOrderSet);
        }
    }

    /**
     * Play the game after the placement phase
     */
    public void playGame() throws IOException {
        out.println(getPrompt(this.currentJSON));
        if (getPlayerStatus(this.currentJSON).equals("L")) {
            sendOrderToServer(generateCommitJSON());
            while (getPrompt(socketClient.receive()).equals("invalid\n")) {
                sendOrderToServer(generateCommitJSON());
            }
        } else if (getPlayerStatus(this.currentJSON).equals("E")) {
            out.println("Game over.");
        } else {
            String prompt = "You are the Player " + String.valueOf(playerID)
                    + ", What would you like to do?\n   (M)ove\n   (A)ttack\n   (D)one";
            HashSet<String> legalOrderSet = new HashSet<String>();
            legalOrderSet.add("M");
            legalOrderSet.add("A");
            legalOrderSet.add("D");
            ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
            while (!orderString.get(0).equals("D")) {
                orderString = sendOrder(prompt, legalOrderSet);
            }
        }
    }
}
