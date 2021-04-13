package edu.duke.ece651_g10.server;

import edu.duke.ece651_g10.shared.JSONCommunicator;

import java.net.Socket;

/**
 * The Player class records the information of a player within the game.
 * Maintained by Guancheng Fu
 */
public class Player {

  // The next available playerId.
  static int availableId;

  // BufferedWriter bw;

  private JSONCommunicator jCommunicate;

  static {
    availableId = 1;
  }

  private boolean isLost;

  // This should record the socket related to this user.
  private int socketNumber;

  private Socket connectedSocket;

  private int playerId;

  public int getPlayerID() {
    return playerId;
  }

  public Socket getSocket() {
    return this.connectedSocket;
  }

  public int getSocketNumber() {
    return this.socketNumber;
  }

  public boolean getIsLost() {
    return isLost;
  }

  public void setIsLost() {
    this.isLost = true;
  }

  public JSONCommunicator getJCommunicator() {
    return this.jCommunicate;
  }

  /**
   * Version 2 fields. foodResourceTotal: The total food resource owned by the
   * player. technologyResourceTotal: The total technology resource owned by the
   * player. technologyLevel: The technology level of the current player.
   * upgradeInThisTurn: Represents whether a player can upgrade its technology
   * level in this turn.
   */
  private int foodResourceTotal;

  private int technologyResourceTotal;

  private int technologyLevel;

  private boolean canUpgradeInThisTurn;

  // This variable represents whether the player is in current game.
  // If the player log out, then the inGame variable is set to false.
  private volatile boolean inGame;

  WaitGroup waitGroup;

  public void setWaitGroup(WaitGroup waitGroup) {
    this.waitGroup = waitGroup;
  }

  //Added by yt136
  /**
   * Get whether the player can upgrade in this turn
   *
   * @return The boolean whether the player can upgrade in this trun 
   */
  public boolean getCanUpgradeInThisTurn() {
    return this.canUpgradeInThisTurn;
  }

  public void setCanUpgradeInThisTurn(boolean canUpgradeInThisTurn) {
    this.canUpgradeInThisTurn = canUpgradeInThisTurn;
  }

  /**
   * Get the food resource total.
   *
   * @return The player's total food resource.
   */
  public int getFoodResourceTotal() {
    return this.foodResourceTotal;
  }

  /**
   * Set the player's food resource.
   *
   * @param foodResource The player's new food resource.
   */
  public void setFoodResourceTotal(int foodResource) {
    this.foodResourceTotal = foodResource;
  }

  /**
   * Get the player's current total technology units.
   *
   * @return the player's current technology units.
   */
  public int getTechnologyResourceTotal() {
    return this.technologyResourceTotal;
  }

  /**
   * Set the player's current total technology resource.
   *
   * @param resourceTotal The player's total resource.
   */
  public void setTechnologyResourceTotal(int resourceTotal) {
    this.technologyResourceTotal = resourceTotal;
  }

  /**
   * Get the player's current technology level.
   *
   * @return The player's current technology level.
   */
  public int getTechnologyLevel() {
    return this.technologyLevel;
  }

  /**
   * Increment the player's technology level by 1.
   */
  public void incrementTechnologyLevel() {
    this.technologyLevel += 1;
  }

  /**
   * Mark the player as join the game.
   */
  public void joinGame() {
    this.inGame = true;
  }

  /**
   * Check whether the player is in this game.
   *
   * @return True if the player is in this game. False if the player leaves the
   *         game.
   */
  public boolean getInGameStatus() {
    return inGame;
  }

  /**
   * Mark the player as leave the game.
   */
  public void leaveGame() {
    this.inGame = false;
  }

  // public BufferedWriter getBufferedWriter() {
  // return this.bw;
  // }

  /**
   * Construct a Player class.
   *
   * @param socket The socket which can be used to read/write to it.
   */
  Player(Socket socket, JSONCommunicator jc) {
    synchronized (Player.class) {
      this.playerId = availableId++;
    }
    this.connectedSocket = socket;
    this.jCommunicate = jc;
    this.foodResourceTotal = 0;
    this.technologyResourceTotal = 0;
    this.technologyLevel = 1;
    this.canUpgradeInThisTurn = true;
    this.inGame = false;
  }

}
