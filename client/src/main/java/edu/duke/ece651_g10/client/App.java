/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The App class
 */
//public class App {
//  final Client client;
//  final SocketClient socketClient;
//
//  /**
//   * The constructor of the App class
//   *
//   * @param input    The BufferedReader input
//   * @param hostname The hostname of the game
//   * @param port     The port of the game
//   */
//  public App(BufferedReader input, String hostname, int port) throws IOException {
//    this.socketClient = new SocketClient(hostname, port);
//    this.client = new Client(System.out, input, socketClient);
//  }
//
//  /**
//   * The main function of the App
//   *
//   * @param args The argument of when user run the client program in the shell
//   */
//  public static void main(String[] args) throws IOException {
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//    App app = new App(input, "127.0.0.1", 12345);
//    while (true) {
//      app.client.setCurrentJSON(app.socketClient.receive());
//      app.client.commandMap.get(app.client.getCurrentMessageType()).run();
//    }
//  }
//}
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    URL cssResource = getClass().getResource("/ui/calcbuttons.css");
    URL xmlResource = getClass().getResource("/ui/calc-split.xml");
//    //RPNStack model = new RPNStack();
//    //FXMLLoader loader = new FXMLLoader(xmlResource);
//    HashMap<Class<?>,Object> controllers = new HashMap<>();
//    //controllers.put(NumButtonController.class, new NumButtonController(model));
//    //controllers.put(CalculatorController.class, new CalculatorController());
//    loader.setControllerFactory((c)->{
//      return controllers.get(c);
//    });
//    GridPane gp = loader.load();
//    Scene scene = new Scene(gp,640,480);
//    scene.getStylesheets().add(cssResource.toString());
//    @SuppressWarnings("unchecked")
//    ListView<Double> operands = (ListView<Double>)scene.lookup("#rpnstack");
//    operands.setItems(model.getList());
    //stage.setScene();
    stage.show();
  }
}












