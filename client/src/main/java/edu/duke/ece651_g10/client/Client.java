package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
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

  public String playerStatus;

  private boolean endGame;
  final int playerID;
  final HashSet<String> normalOrderSet;
  final HashMap<String, String> orderKeyValue;
  public SocketClient socketClient;

  /**
   * The constructor of the Client
   */
  public Client(PrintStream out, BufferedReader input, SocketClient socketClient) throws IOException {
    this.socketClient = socketClient;
    socketClient.send(generateInfoJSON("Testing, server, can you hear me?\n"));
    JSONObject ans = socketClient.receive();
    String prompt = ans.getString("prompt");
    System.out.println(prompt);
    this.playerID = getPlayerId(ans);
    this.playerStatus = "A";
    this.out = out;
    this.inputReader = input;
    endGame = false;

    this.normalOrderSet = new HashSet<String>();
    normalOrderSet.add("M");
    normalOrderSet.add("A");

    this.orderKeyValue = new HashMap<String, String>();
    orderKeyValue.put("M", "move");
    orderKeyValue.put("A", "attack");
    orderKeyValue.put("D", "commit");
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
   * generate a commit JSONObject used to let the server end the receiving order
   * phase
   * 
   * @return a newly constructed JSONObject with only one field "type" to be
   *         "commit"
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
   * get the asking field from the json object
   * 
   * @param obj the json object
   * @return "initial" where the player is in the initialization part of the game
   *         and whose order should be restricted to move and commit "regular"
   *         where the player is in the game and can input any order type and
   *         commit
   */
  public String getAskingType(JSONObject obj) {
    assert (getMessageType(obj) != null && getMessageType(obj).equals("ask"));
    try {
      String ans = obj.getString("asking");
      return ans;
    } catch (NullPointerException e) {
      // e.printStackTrace();
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
    return inputReader.readLine();
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
    if (getPrompt(socketClient.receive()) == "invalid\n") {
      out.println("Your last order is invalid, please input your order again");
      orderString = sendOrder(prompt, legalOrderSet);
    }
    return orderString;
  }

  /**
   * Place the units at beginning of the game
   *
   * @return Always return true
   */
  public boolean doPlacement() throws IOException {
    out.println(getPrompt(socketClient.receive()));
    String prompt = "You can move your units now.\n   (M)ove\n   (D)one";
    out.println(prompt);
    HashSet<String> legalOrderSet = new HashSet<String>();
    legalOrderSet.add("M");
    legalOrderSet.add("D");
    ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
    while (!orderString.get(0).equals("D")) {
      orderString.clear();
      orderString = sendOrder(prompt, legalOrderSet);
    }
    return true;
  }

  /**
   * Play the game after the placement phase
   *
   * @return Return the boolean whether the game is end
   */
  public boolean playGame() throws IOException {
    JSONObject receivedJSON = socketClient.receive();
    out.println(getPrompt(receivedJSON));
    if (getPlayerStatus(receivedJSON) == "L") {
      sendOrderToServer(generateCommitJSON());
      // if (getPrompt(jCommunicate.receive()) == "invalid\n") {
      // sendOrderToServer(generateCommitJSON());
      // }
    } else if (getPlayerStatus(receivedJSON) == "E") {
      endGame = true;
    } else {
      String prompt = "You are the Player " + String.valueOf(playerID)
          + ", What would you like to do?\n   (M)ove\n   (A)ttack\n   (D)one";
      out.println(prompt);
      HashSet<String> legalOrderSet = new HashSet<String>();
      legalOrderSet.add("M");
      legalOrderSet.add("A");
      legalOrderSet.add("D");
      ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
      while (!orderString.get(0).equals("D")) {
        orderString = sendOrder(prompt, legalOrderSet);
      }
    }
    return endGame;
  }
}
