package edu.duke.ece651_g10.client;

import edu.duke.ece651_g10.client.controller.InGameController;
import edu.duke.ece651_g10.client.controller.NumButtonController;
import edu.duke.ece651_g10.client.model.GameInfo;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.checkerframework.common.reflection.qual.GetClass;

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
        URL fxmlResource = getClass().getResource("/ui/GameMapFor3.fxml");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor4.fxml");
        //URL fxmlResource = getClass().getResource("/ui/GameMapFor5.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        GameInfo gameInfo = new GameInfo();
        controllers.put(InGameController.class, new InGameController(gameInfo,primaryStage));
        loader.setControllerFactory((c)->{
            return controllers.get(c);
        });
        //AnchorPane ap = loader.load();
        GridPane gp = loader.load();
        //AnchorPane ap =  (AnchorPane) FXMLLoader.load(getClass().getResource("/ui/GameMapFor5.fxml"));
        //gp.add(ap,1,0,2,2);
//        gp.setAlignment(Pos.CENTER);
//        gp.setHgap(10);
//        gp.setVgap(10);
//        gp.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(gp);
        //gp.add(ap,1,1,9,9);
        scene.getStylesheets().add(cssResource.toString());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
