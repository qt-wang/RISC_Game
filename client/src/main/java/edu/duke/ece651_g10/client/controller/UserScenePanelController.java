package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.Client;
import javafx.stage.Stage;

public class UserScenePanelController {
    Client client;
    Stage primaryStage;

    public UserScenePanelController(Client client, Stage primaryStage) {
        this.client = client;
        this.primaryStage = primaryStage;
    }
}
