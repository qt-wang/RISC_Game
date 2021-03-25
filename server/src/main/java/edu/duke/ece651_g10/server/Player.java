package edu.duke.ece651_g10.server;

import edu.duke.ece651_g10.shared.JSONCommunicator;

import java.net.Socket;

/**
 * The Player class records the information of a player within the game.
 * Maintained by Guancheng Fu
 */
public class Player {

  //The next available playerId.
  static int availableId;

//  BufferedWriter bw;

  private JSONCommunicator jCommunicate;

  static {
    availableId = 1;
  }

  private boolean isLost;

  // This should record the socket related to this user.
  private int socketNumber;

  private Socket connectedSocket;

  private int playerId;
  // TODO:Don't forget to modify this function
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

  public JSONCommunicator getJCommunicator(){
    return this.jCommunicate;
  }

//  public BufferedWriter getBufferedWriter() {
//    return this.bw;
//  }
  /**
   * Construct a Player class.
   * @param socket   The socket which can be used to read/write to it.
   */
  Player(Socket socket, JSONCommunicator jc) {
    this.playerId = availableId ++;
    this.connectedSocket = socket;
    this.jCommunicate = jc;
  }



}
