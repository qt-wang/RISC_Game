package edu.duke.ece651_g10.client;

import edu.duke.ece651_g10.client.controller.LoginButtonController;
import edu.duke.ece651_g10.client.controller.UserScenePanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class SceneFactory {

    Client client;
    Stage primaryStage;

    public SceneFactory(Client client, Stage primaryStage) {
        this.client = client;
        this.primaryStage = primaryStage;
    }

    /**
     * Create the scene to be used when game starts.
     *
     * @return A scene which contains the buttons for users to log in.
     * @throws IOException
     */
    public Scene createLoginScene() throws IOException {
        //URL cssResource = getClass().getResource("/ui/buttonStyles.css");
        URL fxmlResource = getClass().getResource("/ui/GameStart.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(LoginButtonController.class, new LoginButtonController(this.client, this.primaryStage, this));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane pane = loader.load();
        Scene loginScene = new Scene(pane, 1600, 900);
        return loginScene;
    }

    public Scene createUserScene(JSONObject object) throws IOException {
        URL fxmlResource = getClass().getResource("/ui/UserPanel.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(UserScenePanelController.class, new UserScenePanelController(this.client, this.primaryStage, object));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane pane = loader.load();
        Scene UserScene = new Scene(pane, 1600, 900);
        return UserScene;
    }

}
