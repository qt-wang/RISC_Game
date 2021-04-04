/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The App class
 */
public class App {
  final Client client;
  final SocketClient socketClient;

  /**
   * The constructor of the App class
   *
   * @param input    The BufferedReader input
   * @param hostname The hostname of the game
   * @param port     The port of the game
   */
  public App(BufferedReader input, String hostname, int port) throws IOException {
    this.socketClient = new SocketClient(hostname, port);
    this.client = new Client(System.out, input, socketClient);
  }

  /**
   * The main function of the App
   *
   * @param args The argument of when user run the client program in the shell
   */
  public static void main(String[] args) throws IOException {
//    boolean endGame = false;
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//    //App app = new App(input, "152.3.69.50", 12345);
//    App app = new App(input, "127.0.0.1", 12345);
//    app.client.doPlacement();
//    while (!endGame) {
//      //JSONObject ans = app.client.jCommunicate.receive();
//      // JSONObject ans = app.client.jCommunicate.receive();
//      //System.out.println(ans.getString("prompt"));
//      endGame = app.client.playGame();
//    }
//    //app.client
    Application.launch(Game.class,args);
    // BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    // App app = new App(input, "127.0.0.1", 12345);
    // while (true) {
    //   app.client.setCurrentJSON(app.socketClient.receive());
    //   app.client.commandMap.get(app.client.getCurrentMessageType()).run();
    // }
  }
}











