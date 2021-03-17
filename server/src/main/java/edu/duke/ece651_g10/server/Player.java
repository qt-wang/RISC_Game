package edu.duke.ece651_g10.server;

import java.io.BufferedWriter;
import java.net.Socket;

/**
 * The Player class records the information of a player within the game.
 * Maintained by Guancheng Fu
 */
public class Player {

  //The next available playerId.
  static int availableId;

  BufferedWriter bw;

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

  public BufferedWriter getBufferedWriter() {
    return this.bw;
  }
  /**
   * Construct a Player class.
   * @param socket   The socket which can be used to read/write to it.
   */
  Player(Socket socket, BufferedWriter bw) {
    this.playerId = availableId++;
    this.connectedSocket = socket;
    this.bw = bw;
  }



}
