/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 * The App class
 */
public class App extends Application {
//    final Client client;
//    final SocketClient socketClient;
//
//    /**
//     * The constructor of the App class
//     *
//     * @param input    The BufferedReader input
//     * @param hostname The hostname of the game
//     * @param port     The port of the game
//     */
//    public App(BufferedReader input, String hostname, int port) throws IOException {
//        this.socketClient = new SocketClient(hostname, port);
//        this.client = new Client(System.out, input, socketClient);
//    }

    /**
     * The main function of the App
     *
     * @param args The argument of when user run the client program in the shell
     */
    public static void main(String[] args) throws IOException {
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//    App app = new App(input, "127.0.0.1", 12345);
//    while (true) {
//      app.client.setCurrentJSON(app.socketClient.receive());
//      app.client.commandMap.get(app.client.getCurrentMessageType()).run();
//    }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        SocketClient socketClient = new SocketClient("127.0.0.1", 12345);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client(System.out, input, socketClient);



        Group group = new Group();

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(70, 70);
        Button createButton = new Button("Create new account");

        loginButton.setLayoutX(550);

        loginButton.setLayoutY(350);

        createButton.setLayoutX(750);
        createButton.setLayoutY(350);

        group.getChildren().addAll(loginButton, createButton);

        //group.setOpacity(0.5);

        Scene scene = new Scene(group);
        primaryStage.setTitle("RISC");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);


        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Pop up a window to let the user enter the password.
                System.out.println("button pressed");
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Let the client send a message to server.
                try {
                    JSONObject object = client.generateConnectJSON("");
                    client.sendOrderToServer(object);
                    JSONObject test = client.socketClient.receive();
                    System.out.println(test);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.show();
    }
}











