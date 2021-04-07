package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    public TestSceneController(Client client, Stage primaryStage, SceneFactory factory, JSONObject object) {
        this.client = client;
        this.primaryStage = primaryStage;
        this.factory = factory;
    }

    @FXML
    Label stateLabel;

    public void doneButtonPressed() throws IOException {
        client.getSocketClient().send(client.generateCommitJSON());
        // Check valid.
        JSONObject object = this.client.getSocketClient().receive();
        System.out.println("Commit response: " + object);

        // Now, get the second object.
        boolean valid = object.getString("prompt").equals("valid\n");
        if (!valid) {
            Stage stage = App.createDialogStage(primaryStage, "Error", object.getString("reason"));
            stage.show();
        } else {
            // Generate a back ground task to try to receive the next turn information.
            Task<JSONObject> task = App.generateBackGroundReceiveTask(client);
            task.valueProperty().addListener(new ChangeListener<JSONObject>() {
                @Override
                public void changed(ObservableValue<? extends JSONObject> observable, JSONObject oldValue, JSONObject newValue) {
                    if (newValue != null) {
                        //TODO: update the model information, and update the scene.
                        System.out.println(newValue);
                    }
                }
            });
            Thread t = new Thread(task);
            t.start();
            logOutButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Cancel the task.
                    task.cancel();
                    try {
                        client.sendLogOutCommand();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    JSONObject object = null;
                    try {
                        object = client.getSocketClient().receive();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    if (object.getString("prompt").equals("valid\n")) {
                        // Is ok to logout.
                        // Relogin into the beginning site.
                        try {
                            client.sendOrderToServer(client.sendPasswordToServer(client.getPassword()));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        //JSONObject result = client.getSocketClient().receive();
                        //System.out.println("Send password to server response: " + result);
                        JSONObject result = null;
                        try {
                            result = client.getSocketClient().receive();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        //System.out.println("Last response, used for generate user pane:" + result);
                        Scene loginScene = null;
                        try {
                            loginScene = factory.createUserScene(result);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        primaryStage.setScene(loginScene);
                    } else {
                        App.createDialogStage(primaryStage, "Error", object.getString("reason"));
                        // Recreate the previous state.
                        Thread newThread = new Thread(task);
                        newThread.start();
                    }
                }
            });
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateLabel.setText("Currently waiting for enough players to join.");
    }
}
