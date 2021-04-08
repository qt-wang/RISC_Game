package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
            button.setText("Create");
        } else {
            choiceBoxIntro.setText("Select game:");
            button.setText("Join");
        }
    }


    public void onCreateNewGames(ActionEvent actionEvent) throws IOException {
        this.createGameLayOut = true;
        labelIntro.setText("");
        setupLayOut();
        // Setup the default values for choice box.
        choiceBox.setDisable(true);
        choiceBox.getItems().clear();
        for (int i = 2; i <= 5; i++) {
            choiceBox.getItems().add(i);
        }
        // Setup the default values for choice box.
        choiceBox.setValue(2);
        choiceBox.setDisable(false);
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
        choiceBox.setDisable(true);
        if (choiceBox.getItems() != null) {
            choiceBox.getItems().clear();
        }
        labelIntro.setText("");
        setChoiceBoxDefaultValue(object);
        setupLayOut();
    }

    public void onListExistingGameButton(ActionEvent ae) throws IOException {
        this.createGameLayOut = false;
        client.sendListOpenGameJSON(true);
        JSONObject object = client.socketClient.receive();
        choiceBox.setDisable(true);
        if (choiceBox.getItems() != null) {
            choiceBox.getItems().clear();
        }
        labelIntro.setText("");
        setChoiceBoxDefaultValue(object);
        setupLayOut();
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
//            System.out.println(numberOfPlayers);
            this.client.sendCreateGameJSON(numberOfPlayers);
            JSONObject object = this.client.getSocketClient().receive();
            //System.out.println(object);
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
            //System.out.println("game id in join button: " + gameId);
            this.client.sendJoinGameJSON(gameId);
            JSONObject object = this.client.getSocketClient().receive();
            Boolean valid = object.getString("prompt").equals("valid\n");
            if (!valid) {
                // provide a information to show it.
                Stage stage = App.createDialogStage(primaryStage, "Error", object.getString("reason"));
                stage.show();
            } else {
                /**
                 * Do the following:
                 * Generate a new stage, that stage will show the wait message.
                 * It should have a cancel button.
                 * This button should send another message to server indicate that the client logout at the wait state, so that it will not block?
                 * After receive the result from the background task, create a new scene use it.
                 */
                // Create the background task, which should receive a json object from the server.
                Task<JSONObject> backTask = App.generateBackGroundReceiveTask(client);

                //Create a wait box.
                Stage stage = App.createChildStage(primaryStage, "Waiting...");
                VBox dialogBox = new VBox(20);
                Text info = new Text("Please wait for enough players to join.");
                info.setFont(Font.font(18));
                dialogBox.getChildren().add(info);
                Button button = new Button("Cancel");
                dialogBox.getChildren().add(button);
                Scene dialogScene = new Scene(dialogBox, 300, 200);
                stage.setScene(dialogScene);


                backTask.valueProperty().addListener(new ChangeListener<JSONObject>() {
                    @Override
                    public void changed(ObservableValue<? extends JSONObject> observable, JSONObject oldValue, JSONObject newValue) {
                        // When we get the result back from it, we can do some tasks.
                        // After we get the thing back.
                        // We should first close the dialog.
                        //primaryStage.show();
                        if (newValue != null) {
                            stage.close();
                            Scene testScene = null;
                            try {
                                //TODO: Update other maps based on player numbers.
                                //testScene = factory.createTestScene(newValue);
                                testScene = factory.createMap(object);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            //primaryStage.close();
                            primaryStage.setWidth(testScene.getWidth());
                            primaryStage.setHeight(testScene.getHeight());
                            primaryStage.setScene(testScene);
                        }
                    }
                });

                //Try receive the json object from the server.
                Thread t = new Thread(backTask);
                t.start();
                stage.show();
                button.setOnAction(new EventHandler<ActionEvent>() {
                    //TODO: Change this behaviour.
                    @Override
                    /**
                     * This function should send a message to the server, to log the user out.
                     * Also, it should close the wait box, and cancel the background task.
                     */
                    public void handle(ActionEvent event) {
                        try {
                            client.sendCancelLogInCommand(gameId);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        stage.close();
                        backTask.cancel();
                    }
                });
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
                //System.out.println((int) newValue.intValue());
                if (!controller.choiceBox.isDisabled()) {
                    if (!controller.createGameLayOut) {
                        int target = (int) choiceBox.getItems().get((int) newValue.intValue());
                        for (JSONObject object : client.getListedGames().values()) {
                            int gameId = object.getInt("gameId");
                            //System.out.println(newValue.intValue());
                            if (gameId == target) {
                                setLabelText(object);
                                break;
                            }
                        }
                    } else {
                        labelIntro.setText("");
                    }
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
