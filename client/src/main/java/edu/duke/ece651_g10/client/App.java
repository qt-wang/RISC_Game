/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The App class
 */
public class App {
  final Client client;

  /**
   * The constructor of the App class
   *
   * @param input    The BufferedReader input
   * @param hostname The hostname of the game
   * @param port     The port of the game
   */
  public App(BufferedReader input, String hostname, int port) throws IOException {
    this.client = new Client(System.out, input, hostname, port);
  }

  public static void main(String[] args) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    //App app = new App(input, "152.3.69.50", 12345);
    App app = new App(input, "127.0.0.1", 12345);
    app.client.doPlacement();
    while (true) {
      //JSONObject ans = app.client.jCommunicate.receive();
      // JSONObject ans = app.client.jCommunicate.receive();
      //System.out.println(ans.getString("prompt"));
      app.client.playGame();
    }
    //app.client
  }
}
