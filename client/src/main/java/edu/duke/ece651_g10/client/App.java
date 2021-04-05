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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

    static Stage createChildStage(Stage primaryStage, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initOwner(primaryStage);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    /**
     * Create a new scene for login users.
     *
     * @param object The json object returned from the server.
     * @return
     */
    static Scene createSceneForLogInUsers(JSONObject object) {
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        SocketClient socketClient = new SocketClient("127.0.0.1", 12345);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client(System.out, input, socketClient);


        primaryStage.setResizable(false);

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
                final Stage dialog = createChildStage(primaryStage, "Login");
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Scene scene = new Scene(grid, 300, 275);
                dialog.setScene(scene);
                Label password = new Label("Password: ");
                // column 0, 1
                grid.add(password, 0, 1);
                PasswordField passwordField = new PasswordField();
                grid.add(passwordField, 1, 1);
                Button loginButton = new Button();
                loginButton.setText("Login");
                Text warning = new Text();
                warning.setFont(Font.font(14));
                grid.add(warning, 1, 2);

                //Handle the logic for loginButton.
                loginButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            String password = passwordField.getText();
                            passwordField.clear();
                            // Send the corresponding information to server.
                            JSONObject passwordJSON = client.sendPasswordToServer(password);
                            client.sendOrderToServer(passwordJSON);
                            JSONObject response = client.socketClient.receive();

                            String valid = response.getString("prompt");
                            if (valid.equals("invalid\n")) {
                                // Provide the reason.
                                warning.setText(response.getString("reason"));
                            } else {
                                // Change scene, and login the user.
                                System.out.println("Login successfully");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                grid.add(loginButton, 0, 2);
                dialog.show();
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

                    // TODO: check validness.
                    String password = test.getString("password");
                    System.out.println(password);

                    // Display a window to tell the user its password.
                    final Stage dialog = createChildStage(primaryStage, "password");
                    VBox dialogBox = new VBox(20);
                    Text info = new Text("Your password is:" + password);
                    info.setFont(Font.font(20));
                    dialogBox.getChildren().add(info);
                    Scene dialogScene = new Scene(dialogBox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.show();
    }
}











