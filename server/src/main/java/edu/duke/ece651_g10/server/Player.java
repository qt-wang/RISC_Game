package edu.duke.ece651_g10.server;

/**
 * The Player class records the information of a player within the game.
 * Maintained by Guancheng Fu
 */
public class Player {
  private boolean isLost;

  // This should record the socket related to this user.
  private int socketNumber;

  private int playerId;
  // TODO:Don't forget to modify this function
  public int getPlayerID() {
    return playerId;
  }

  public int getSocketNumber() {
    return this.socketNumber;
  }

  public boolean getIsLost() {
    return isLost;
  }
}
