package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;

import javafx.event.ActionEvent;

import java.io.IOException;
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

    @FXML
    Button listOpenGameButton;

    @FXML
    Button createGameButton;

    @FXML
    Button listExistingGameButton;

    public UserScenePanelController(Client client, Stage primaryStage, JSONObject object) {
        this.client = client;
        this.primaryStage = primaryStage;
        this.object = object;
    }

    // Send json to the server, and receive the corresponding json from server.
    public void onClickListOpenGameButton(ActionEvent ae) throws IOException {
        client.sendListOpenGameJSON();
        JSONObject object = client.socketClient.receive();
        System.out.println(object);
        choiceBox.getItems().clear();
        setChoiceBoxDefaultValue(object);
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

    private void setOnActionForChoiceBox() {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Based on the value of newValue, display the gameinfo for newValue.
                for (JSONObject object: client.getListedGames().values()) {
                    int gameId = object.getInt("gameId");
                    if (gameId == newValue.intValue()) {
                        setLabelText(object);
                        break;
                    }
                }
            }
        });
    }

    private void setChoiceBoxDefaultValue(JSONObject object) {
        int numberOfGames = object.getInt("numberOfGames");
        if (numberOfGames != 0) {
            HashMap<Integer, JSONObject> listedGames = new HashMap<>();
            for (int i = 0; i < numberOfGames; i ++) {
                JSONObject temp = object.getJSONObject(Integer.toString(i));
                choiceBox.getItems().add(temp.getInt("gameId"));
                listedGames.put(i, temp);
            }
            client.setListedGames(listedGames);
            // Set the default value for the choice box and label.
            JSONObject defaultJSON = client.getListedGames().get(0);
            choiceBox.setValue(defaultJSON.getInt("gameId"));
            setLabelText(defaultJSON);
        } else {
            button.setDisable(true);
            choiceBox.getItems().clear();
            choiceBox.setDisable(true);
            labelIntro.setText("There is currently no open games.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int numberOfGames = this.object.getInt("numberOfGames");
        // Set the action handler for choice box.
        setOnActionForChoiceBox();
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
            setChoiceBoxDefaultValue(this.object);
        }
    }
}
