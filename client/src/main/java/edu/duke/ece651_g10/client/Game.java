package edu.duke.ece651_g10.client;

import edu.duke.ece651_g10.client.controller.NumButtonController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;

public class Game extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

//        primaryStage.setTitle("RISK Game");
//        primaryStage.getIcons().add(new Image("/ui/icon/Helvetica_R.png"));
//        primaryStage.setMinHeight(900);
//        primaryStage.setMinWidth(1600);
//        primaryStage.setResizable(false);
//        primaryStage.show();

        URL cssResource = getClass().getResource("/ui/buttonStyles.css");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
        URL fxmlResource = getClass().getResource("/ui/GameMapFor2.fxml");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor4.fxml");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor5.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(NumButtonController.class, new NumButtonController());
        loader.setControllerFactory((c)->{
            return controllers.get(c);
        });
        AnchorPane ap = loader.load();
        Scene scene = new Scene(ap,600,400);
        scene.getStylesheets().add(cssResource.toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
