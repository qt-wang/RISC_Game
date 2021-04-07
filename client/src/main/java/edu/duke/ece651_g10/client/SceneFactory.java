package edu.duke.ece651_g10.client;

import edu.duke.ece651_g10.client.controller.LoginButtonController;
import edu.duke.ece651_g10.client.controller.NumButtonController;
import edu.duke.ece651_g10.client.controller.TestSceneController;
import edu.duke.ece651_g10.client.controller.UserScenePanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
        Scene loginScene = new Scene(pane, 900, 400);
        return loginScene;
    }

    public Scene createUserScene(JSONObject object) throws IOException {
        URL fxmlResource = getClass().getResource("/ui/UserPanel.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(UserScenePanelController.class, new UserScenePanelController(this.client, this.primaryStage, object, this));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane pane = loader.load();
        Scene UserScene = new Scene(pane, 900, 400);
        return UserScene;
    }

    // Create a test scene for the user.
    public Scene createTestScene(JSONObject object) throws IOException {
        URL cssResource = getClass().getResource("/ui/buttonStyles.css");
        URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(TestSceneController.class, new TestSceneController(this.client, this.primaryStage, this, object));
        loader.setControllerFactory((c)->{
            return controllers.get(c);
        });
        AnchorPane ap = loader.load();
        Scene scene = new Scene(ap,900,400);
        scene.getStylesheets().add(cssResource.toString());
        return scene;
    }

}
