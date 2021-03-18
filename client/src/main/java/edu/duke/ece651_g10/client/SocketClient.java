package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONObject;

import edu.duke.ece651_g10.shared.JSONCommunicator;

/**
 * The class for client side socket
 */
public class SocketClient {
  final String serverHostname;
  final int serverPort;
  private Socket socket;
  private BufferedReader br;
  private BufferedWriter bw;
  public JSONCommunicator jCommunicate;

  /**
   * The constructor of the ClientSocket
   */
  public SocketClient(String hostname, int port) throws IOException {
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
      this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
      this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      this.jCommunicate = new JSONCommunicator(br, bw);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
