package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;

public class LoginButtonController {
    Client client;
    Stage primaryStage;
    SceneFactory factory;
    public LoginButtonController(Client client, Stage primaryStage, SceneFactory factory) {
        this.client = client;
        this.primaryStage = primaryStage;
        this.factory = factory;
    }

    public void onCreate(ActionEvent e) {
        System.out.println("Create button pressed");
        try {
            JSONObject object = client.generateConnectJSON("");
            client.sendOrderToServer(object);
            JSONObject test = client.socketClient.receive();

            // TODO: check validness.
            String password = test.getString("password");
            System.out.println(password);

            // Display a window to tell the user its password.
            String content = "Your password is:" + password;
            final Stage dialog = App.createDialogStage(primaryStage, "Password", content);
            dialog.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void onLogin(ActionEvent e) {
        System.out.println("Login button pressed.");
        final Stage dialog = App.createChildStage(primaryStage, "Login");
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
        // Handle the action for it.

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
                        client.setPassword(password);
                        dialog.close();
                        primaryStage.setScene(factory.createUserScene(response));
                        // Set the new scene for the primaryStage.
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(loginButton, 0, 2);
        dialog.show();
    }
}
