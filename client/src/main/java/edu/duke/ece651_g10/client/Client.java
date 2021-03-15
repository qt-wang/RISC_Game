package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;

/**
 * The client of the game
 */
public class Client {
  final int playerID;
  final PrintStream out;
  final BufferedReader inputReader;

  final String serverHostname;
  final int serverPort;
  private Socket socket;
  private InputStream is;
  private OutputStream os;
  private ObjectOutputStream objectOut;
  private BufferedReader br;

  boolean couldCommand;
  boolean isDisconnected;

  /**
   * The constructor of the Client
   */
  public Client(PrintStream out, BufferedReader input, String hostname, int port) throws IOException {
    this.serverHostname = hostname;
    this.serverPort = port;
    initSocket(hostname, port);

    // Don't know how to use Mockito to set playerID at the beginning, just set 0
    // for now.
    // this.playerID = Integer.parseInt(readLinesFromServer(this.br));
    this.playerID = 0;
    this.out = out;
    this.inputReader = input;
    couldCommand = true;
    isDisconnected = false;
  }

  /**
   * Initiate the socket and connect to the server
   */
  private void initSocket(String hostname, int port) {
    try {
      this.socket = new Socket(hostname, port);
      this.is = this.socket.getInputStream();
      this.os = this.socket.getOutputStream();

      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.os));
      this.br = new BufferedReader(new InputStreamReader(this.is));
      this.objectOut = new ObjectOutputStream(this.os);

      bw.write("Testing, server, can you hear me?\n");
      bw.flush();
      String ans = readLinesFromServer(this.br);
      System.out.println("Server：" + ans.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Read a bunch of lines and combine them into one String
   */
  public String readLinesFromServer(BufferedReader br) throws IOException {
    StringBuilder ans = new StringBuilder();
    String msg = br.readLine();
    while (msg != null) {
      ans.append(msg);
      ans.append("\n");
      msg = br.readLine();
    }
    return ans.toString();
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
  public int readInteger(String prompt) throws IOException {
    try {
      int number = Integer.parseInt(readString(prompt));
      return number;
    } catch (NumberFormatException e) {
      out.println("Please input valid integer.");
      return readInteger(prompt);
    }
  }
}












