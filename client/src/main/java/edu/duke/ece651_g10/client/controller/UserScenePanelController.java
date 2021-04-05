package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UserScenePanelController implements Initializable {
    Client client;
    Stage primaryStage;
    JSONObject object;

    @FXML
    Label labelIntro;

    @FXML
    ChoiceBox choiceBox;

    @FXML
    Button button;

    public UserScenePanelController(Client client, Stage primaryStage, JSONObject object) {
        this.client = client;
        this.primaryStage = primaryStage;
        this.object = object;
    }

    private void setLabelText(JSONObject object) {
        int gameId = object.getInt("gameId");
        int currentPlayers = object.getInt("currentPlayer");
        int totalPlayers = object.getInt("totalPlayers");
        String displayInfo = "gameId: " + gameId + "\n" +
                "Number of players (currently): " + currentPlayers + "\n" +
                "Number of players (required): "  + totalPlayers + "\n";
        labelIntro.setText(displayInfo);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int numberOfGames = this.object.getInt("numberOfGames");
        if (numberOfGames == 0) {
            button.setDisable(true);
            labelIntro.setFont(Font.font(20));
            labelIntro.setText("You currently do not involve in any games.");
            choiceBox.setDisable(true);
        } else {
            // Value is the currently displayed one.
            button.setDisable(false);
            choiceBox.setDisable(false);
            // Store the information into the client.
            HashMap<Integer, JSONObject> listedGames = new HashMap<>();
            for (int i = 0; i < numberOfGames; i ++) {
                JSONObject temp = this.object.getJSONObject(Integer.toString(i));
                choiceBox.getItems().add(temp.getInt("gameId"));
                listedGames.put(i, temp);
            }
            client.setListedGames(listedGames);
            // Set the default value for the choice box and label.
            JSONObject defaultJSON = client.getListedGames().get(0);
            choiceBox.setValue(Integer.toString(defaultJSON.getInt("gameId")));
            setLabelText(defaultJSON);
        }
    }
}
