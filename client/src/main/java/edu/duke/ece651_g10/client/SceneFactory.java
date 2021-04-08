package edu.duke.ece651_g10.client;

import edu.duke.ece651_g10.client.controller.*;
import edu.duke.ece651_g10.client.model.GameInfo;
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


//    public Scene createTwoPeopleMap(JSONObject object) throws IOException {
//        URL cssResource = getClass().getResource("/ui/buttonStyles.css");
//        //URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
//        URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
//        //URL fxmlResource = getClass().getResource("/ui/GameMapFor4.fxml");
//        //URL fxmlResource = getClass().getResource("/ui/GameMapFor5.fxml");
//        FXMLLoader loader = new FXMLLoader(fxmlResource);
//        HashMap<Class<?>,Object> controllers = new HashMap<>();
//        GameInfo gameInfo = new GameInfo(object);
//        controllers.put(InGameController.class, new InGameController(gameInfo, primaryStage, client, this));
//        loader.setControllerFactory((c)->{
//            return controllers.get(c);
//        });
//        GridPane gp = loader.load();
//        Scene scene = new Scene(gp);
//        scene.getStylesheets().add(cssResource.toString());
//        return scene;
//    }

    private Scene loadScene(String url, JSONObject object) throws IOException {
        URL cssResource = getClass().getResource("/ui/buttonStyles.css");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
        URL fxmlResource = getClass().getResource(url);
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor4.fxml");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor5.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        GameInfo gameInfo = new GameInfo(object);
        controllers.put(InGameController.class, new InGameController(gameInfo, primaryStage, client, this));
        loader.setControllerFactory((c)->{
            return controllers.get(c);
        });
        GridPane gp = loader.load();
        Scene scene = new Scene(gp);
        scene.getStylesheets().add(cssResource.toString());
        return scene;
    }

    public Scene createMap(JSONObject object) throws IOException {
        int players = object.getInt("playerNumber");
        String url;
        switch (players) {
            case 2:
                url = "/ui/GameMapFor2.fxml";
                break;
            case 3:
                url = "/ui/GameMapFor3.fxml";
                break;
            case 4:
                url = "/ui/GameMapFor4.fxml";
                break;
            case 5:
                url = "/ui/GameMapFor5.fxml";
                break;
            default:
                System.out.println("Invalid, bad!");
                return null;
        }
        return loadScene(url, object);
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
