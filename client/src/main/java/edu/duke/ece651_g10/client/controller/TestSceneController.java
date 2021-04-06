package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestSceneController implements Initializable {

    Client client;

    Stage primaryStage;

    @FXML
    Button doneButton;


    @FXML
    Button logOutButton;

    SceneFactory factory;

    public TestSceneController(Client client, Stage primaryStage, SceneFactory factory) throws IOException {
        this.client = client;
        this.primaryStage = primaryStage;
        this.factory = factory;
    }

    @FXML
    Label stateLabel;

    public void doneButtonPressed() throws IOException {
        client.getSocketClient().send(client.generateCommitJSON());
        System.out.println("Done button response: " + client.getSocketClient().receive());
    }


    // Do this for the player.
    public void logOutButtonPressed() throws IOException {
        this.client.sendLogOutCommand();
        JSONObject object = this.client.getSocketClient().receive();
        System.out.println("Logout response:" + object);
        if (object.getString("prompt").equals("valid\n")) {
            // Is ok to logout.
            // Relogin into the beginning site.
            client.sendOrderToServer(client.sendPasswordToServer(client.getPassword()));
            //JSONObject result = client.getSocketClient().receive();
            //System.out.println("Send password to server response: " + result);
            JSONObject result = client.getSocketClient().receive();
            System.out.println("Last response, used for generate user pane:" + result);
            Scene loginScene = factory.createUserScene(result);
            primaryStage.setScene(loginScene);
        } else {
            App.createDialogStage(this.primaryStage, "Error", object.getString("reason"));
        }
    }

    private void setUpInitialJSONObject() {
        try {
            JSONObject object = this.client.socketClient.receive();
            System.out.println("Initial setup response:" + object);
            stateLabel.setText("Enough players joined, wait for game to start");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateLabel.setText("Currently waiting for enough players to join.");
        setUpInitialJSONObject();
    }
}
