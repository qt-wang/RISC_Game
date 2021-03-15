package edu.duke.ece651_g10.client;

import java.io.*;
import java.net.Socket;

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
  public Client(int playerID, PrintStream out, BufferedReader input, String hostname, int port) {
    this.playerID = playerID;
    this.out = out;
    this.inputReader = input;
    couldCommand = true;
    isDisconnected = false;
    this.serverHostname = hostname;
    this.serverPort = port;
    initSocket(hostname, port);
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
}
