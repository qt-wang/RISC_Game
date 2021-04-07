package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    Label choiceBoxIntro;

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

    boolean createGameLayOut;

    SceneFactory factory;

    public UserScenePanelController(Client client, Stage primaryStage, JSONObject object, SceneFactory factory) {
        this.client = client;
        this.primaryStage = primaryStage;
        this.object = object;
        this.createGameLayOut = false;
        this.factory = factory;
    }

    /**
     * Based on the value of createGameLayout, change the display information for some components.
     */
    private void setupLayOut() {
        choiceBox.setDisable(false);
        button.setDisable(false);
        if (createGameLayOut) {
            choiceBoxIntro.setText("Please select the number of Players involved in game:");
            labelIntro.setText("");
            button.setText("Create");
        } else {
            choiceBoxIntro.setText("Select game:");
            labelIntro.setText("");
            button.setText("Join");
        }
    }


    public void onCreateNewGames(ActionEvent actionEvent) throws IOException {
        this.createGameLayOut = true;
        setupLayOut();
        // Setup the default values for choice box.
        choiceBox.getItems().clear();
        for (int i = 2; i <= 5; i++) {
            choiceBox.getItems().add(i);
        }
        // Setup the default values for choice box.
        choiceBox.setValue(2);
    }

    // Send json to the server, and receive the corresponding json from server.

    /**
     * Action handler for button listOpenGames
     *
     * @param ae The action event.
     * @throws IOException
     */
    public void onClickListOpenGameButton(ActionEvent ae) throws IOException {
        this.createGameLayOut = false;
        client.sendListOpenGameJSON(false);
        JSONObject object = client.socketClient.receive();
        if (choiceBox.getItems() != null) {
            choiceBox.getItems().clear();
        }
        setupLayOut();
        setChoiceBoxDefaultValue(object);
    }

    public void onListExistingGameButton(ActionEvent ae) throws IOException {
        this.createGameLayOut = false;
        client.sendListOpenGameJSON(true);
        JSONObject object = client.socketClient.receive();
        if (choiceBox.getItems() != null) {
            choiceBox.getItems().clear();
        }
        setupLayOut();
        setChoiceBoxDefaultValue(object);
    }

    // Send join game to the server, receive response from server.

    /**
     * Event handler for player who click the join game.
     *
     * @param ae The action event.
     * @throws IOException
     */
    public void onClickJoinGameButton(ActionEvent ae) throws IOException {
        // Based on the layout, do different things.
        if (createGameLayOut) {
            // Create game layout.
            int numberOfPlayers = (int) choiceBox.getValue();
            this.client.sendCreateGameJSON(numberOfPlayers);
            JSONObject object = this.client.getSocketClient().receive();
            System.out.println(object);
            Boolean valid = object.getString("prompt").equals("valid\n");
            Stage dialog;
            if (valid) {
                dialog = App.createDialogStage(primaryStage, "Tip", "New game successfully added!");
            } else {
                dialog = App.createDialogStage(primaryStage, "Warning", object.getString("reason"));
            }
            dialog.show();
        } else {
            int gameId = (int) choiceBox.getValue();
            this.client.sendJoinGameJSON(gameId);
            JSONObject object = this.client.getSocketClient().receive();
            Boolean valid = object.getString("prompt").equals("valid\n");
            if (!valid) {
                // provide a information to show it.
                Stage stage = App.createDialogStage(primaryStage, "Error", object.getString("reason"));
                stage.show();
            } else {
                // This will block, maybe we can create a task to handle this logic.\
                // We create a new login page.
                Scene testScene = this.factory.createTestScene();
                System.out.println(testScene.getWidth());
                System.out.println(testScene.getHeight());
                primaryStage.close();
                primaryStage.setWidth(testScene.getWidth());
                primaryStage.setHeight(testScene.getHeight());
                primaryStage.setScene(testScene);
                primaryStage.show();
            }
        }
    }

    /**
     * Based on the game info included in the object, setup the label introduction.
     *
     * @param object The json object describes the game information.
     */
    private void setLabelText(JSONObject object) {
        int gameId = object.getInt("gameId");
        int currentPlayers = object.getInt("currentPlayer");
        int totalPlayers = object.getInt("totalPlayers");
        String displayInfo = "gameId: " + gameId + "\n" +
                "Number of players (currently): " + currentPlayers + "\n" +
                "Number of players (required): " + totalPlayers + "\n";
        labelIntro.setText(displayInfo);
    }

    /**
     * Set the on action for the choice box.
     */
    private void setOnActionForChoiceBox(UserScenePanelController controller) {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            //TODO: change
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Based on the value of newValue, display the gameinfo for newValue.
                if (!controller.createGameLayOut) {
                    for (JSONObject object : client.getListedGames().values()) {
                        int gameId = object.getInt("gameId");
                        if (gameId == newValue.intValue()) {
                            setLabelText(object);
                            break;
                        }
                    }
                } else {
                    labelIntro.setText("");
                }
            }
        });
    }

    /**
     * Set the default value for the choice box.
     *
     * @param object The json object describes the game information for client
     */
    private void setChoiceBoxDefaultValue(JSONObject object) {
        int numberOfGames = object.getInt("numberOfGames");
        if (numberOfGames != 0) {
            HashMap<Integer, JSONObject> listedGames = new HashMap<>();
            for (int i = 0; i < numberOfGames; i++) {
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
            client.setListedGames(new HashMap<>());
            button.setDisable(true);
            choiceBox.getItems().clear();
            choiceBox.setDisable(true);
            labelIntro.setText("There is currently no open games.");
        }
    }

    @Override
    /**
     * Initialize the panel, setup initial displays.
     */
    public void initialize(URL location, ResourceBundle resources) {
        int numberOfGames = this.object.getInt("numberOfGames");
        // Set the action handler for choice box.
        setOnActionForChoiceBox(this);
        if (numberOfGames == 0) {
            button.setDisable(true);
            labelIntro.setFont(Font.font(20));
            labelIntro.setText("You currently do not involve in any games.");
            choiceBox.setDisable(true);
            this.client.setListedGames(new HashMap<>());
        } else {
            // Value is the currently displayed one.
            button.setDisable(false);
            choiceBox.setDisable(false);
            // Store the information into the client.
            setChoiceBoxDefaultValue(this.object);
        }
    }
}
