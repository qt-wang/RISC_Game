/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;

/**
 * The App class
 */

public class App extends Application {
    /**
     * The main function of the App
     *
     * @param args The argument of when user run the client program in the shell
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    /**
     * Generate a background task to try to receive a json object from the server.
     * @param client The client which represents the user.
     * @return The task.
     */
    public static Task<JSONObject> generateBackGroundReceiveTask(Client client) {
        return new Task<JSONObject>() {
            @Override
            protected JSONObject call() throws Exception {
                while (true) {
                    JSONObject temp = client.socketClient.tryReceive();
                    if (temp != null) {
                        System.out.println(temp);
                        return temp;
                    }
                    if (isCancelled()) {
                        return null;
                    }
                }
            }
        };
    }

    public static Stage createDialogStage(Stage primaryStage, String title, String content) {
        Stage stage = createChildStage(primaryStage, title);
        VBox dialogBox = new VBox(20);
        Text info = new Text(content);
        info.setFont(Font.font(20));
        dialogBox.getChildren().add(info);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        stage.setScene(dialogScene);
        return stage;
    }

    public static Stage createChildStage(Stage primaryStage, String title) {
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
     * @return  The scene to be used for login users.
     */
    static Scene createSceneForLogInUsers(JSONObject object) {
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("RISC");
        SocketClient socketClient = new SocketClient("127.0.0.1", 12345);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client(System.out, input, socketClient);
        primaryStage.setResizable(false);
        SceneFactory factory = new SceneFactory(client, primaryStage);
        primaryStage.setWidth(900);
        primaryStage.setHeight(400);
        Scene testScene = factory.createLoginScene();
        primaryStage.setScene(testScene);
        primaryStage.show();
    }
}











