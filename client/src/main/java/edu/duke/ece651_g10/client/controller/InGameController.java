package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.App;
import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.SceneFactory;
import edu.duke.ece651_g10.client.model.ColorStrategy;
import edu.duke.ece651_g10.client.model.GameInfo;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class InGameController{

    GameInfo gameInfo;

    Stage primaryStage;

    Client client;

    @FXML
    ListView<String> playerInfo;

    @FXML
    ListView<String> territoryInfo;

    @FXML
    Text prompt;

    JSONObject toSend;

    @FXML
    Button logOutButton;

    SceneFactory factory;

    TerritoryButtonStyleController buttonStyleController;

    @FXML
    Text ownColorDesc;

    @FXML
    Rectangle ownColor;

    /**
     * initiate the game controller with given parameters
     * @param gameInfo the model contains game info
     * @param primaryStage the stage where the game runs
     * @param client the client object used to send and receive json
     * @param factory the factory used to create scene
     */
    public InGameController(GameInfo gameInfo, Stage primaryStage, Client client, SceneFactory factory, TerritoryButtonStyleController buttonStyleController) {
        this.gameInfo = gameInfo;
        this.primaryStage = primaryStage;
        this.client = client;
        this.factory = factory;
        this.buttonStyleController = buttonStyleController;
    }

    /**
     * set the prompt area with given str
     * @param str the given str
     */
    public void setPrompt(String str) {
        prompt.setText(str);
    }

    public void setPlayerColor(){
        ColorStrategy cs = new ColorStrategy();
        ownColorDesc.setFill(Color.WHITE);
        ownColor.setFill(Color.web(cs.getPureColor(gameInfo.getMyOwnColor())));
    }
    /**
     * update player info listview according to the model gameinfo
     * and refresh to show the new info
     */
    public void setPlayerInfo() {
        ObservableList<String> obl = FXCollections.observableArrayList(gameInfo.getPlayerInfo());
        playerInfo.setItems(obl);
        playerInfo.refresh();
    }

    public void updatePlayerAndTerritory(){
        setPlayerInfo();
        setTerritoryColor();
    }

    public void setTerritoryColor(){
        HashMap<String, String> terrColors = gameInfo.territoryColor;
        for(String terrName: terrColors.keySet()){
            buttonStyleController.setButtonStyle(terrName,terrColors.get(terrName));
        }
    }

    /**
     * update the territory listview to show the info of the given territory
     * and refresh to show
     * @param territoryName the name of the territory
     */
    public void setTerritoryInfo(String territoryName) {
        ObservableList<String> obl;
        List<String> territoryInfos = gameInfo.getTerritoryInfo(territoryName);
        if (territoryInfos == null) {
            obl = FXCollections.observableArrayList();

        } else {
            obl = FXCollections.observableArrayList(territoryInfos);
        }
        territoryInfo.setItems(obl);
        territoryInfo.refresh();
    }

    /**
     * when first start the game, the player's info cannot be initiated properly
     * so we leave it until the user's mouse enter the stage
     * @param ae
     */
    @FXML
    public void onEnterStage(MouseEvent ae) {
        updatePlayerAndTerritory();
        setPlayerColor();
    }

    /**
     * click on the anchorpane to clear the territory's listview
     * @param ae
     */
    @FXML
    public void onClickPane(MouseEvent ae) {
        setTerritoryInfo("");
    }

    /**
     * pop up a window with a message and the user can only press "OK"
     * @param msg the message to show
     */
    public void invalidPrompt(String msg) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        Button ok = new Button("OK");
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(msg));
        dialogVbox.getChildren().add(ok);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * set fields in toSend with numeric input, all other characters cannot be input
     * @param infos the prompts for different fields
     * @param fields the fields to be put into toSend jsonobject
     * @param finalStep true if it's the final step of the order and the next step is to send
     *                  false if there are other information needed and not ready to send after this function finishes
     */
    public void setNumbersPrompt(String[] infos, String[] fields, boolean finalStep) {
        assert (infos.length == fields.length);
        int num = infos.length;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        TextField[] inputs = new TextField[num];
        for (int i = 0; i < num; i++) {
            final int index = i;
            inputs[index] = new TextField();
            inputs[index].textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        inputs[index].setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }

        Button ok = new Button("OK");
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < num; i++) {
                    String content = inputs[i].getText();
                    if (!content.equals("")) {
                        toSend.put(fields[i], Integer.parseInt(content));
                    }
                    if (!toSend.has(fields[i])) {
                        toSend = null;
                        setPrompt("Fail to set " + fields[i] + ". Please re-do the whole order!");
                        break;
                    }
                }
                if (finalStep && toSend != null) {
                    try {
                        sendJSON();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                //dialog.setOnCloseRequest(otherEvent->{});
                dialog.close();
            }
        });
        VBox dialogVbox = new VBox(20);
        for (int i = 0; i < num; i++) {
            dialogVbox.getChildren().add(new Label(infos[i]));
            dialogVbox.getChildren().add(inputs[i]);
        }
        dialogVbox.getChildren().add(ok);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.setOnCloseRequest(event -> {
            toSend = null;
            setPrompt("Window closed accidentally. Please re-do the whole order, click OK to confirm.");
        });
        dialog.show();
    }
    /**
     * send the toSend json object to the server and receive a response
     * update info according to it
     * @throws IOException
     */
    public void sendJSON() throws IOException {
        //send and receive reply
        System.out.println(toSend.toString());
        client.getSocketClient().send(toSend);
        JSONObject object = client.getSocketClient().receive();
        if (object.getString("prompt").equals("valid\n")) {
            gameInfo = new GameInfo(object);
            updatePlayerAndTerritory();
            setPrompt("What would you like to do?");
        } else {
            invalidPrompt(object.getString("reason"));
            //setPrompt(object.getString("reason"));
        }
        toSend = null;
    }

    /**
     * handle the user's clicking on territory event
     * @param ae click on the territory
     */
    @FXML
    public void onClickTerritory(ActionEvent ae) {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            setTerritoryInfo(btn.getText());
            if (toSend == null) return;
            String orderType = toSend.getString("orderType");
            if (orderType.equals("move") || orderType.equals("attack") || orderType.equals("moveSpyOrder")) {
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    setPrompt("Please select the destination territory!");
                } else if (!toSend.has("destTerritory")) {
                    toSend.put("destTerritory", btn.getText());
                    //for specific level of unit and unit number
                    if(orderType.equals("moveSpyOrder")){
                        String[] infos = {"Please input the number of unit!"};
                        String[] fields = {"unitNumber"};
                        setNumbersPrompt(infos, fields, true);
                    }else{
                        String[] infos = {"Please input the level of the unit you want to send!", "Please input the number of unit!"};
                        String[] fields = {"unitLevel", "unitNumber"};
                        setNumbersPrompt(infos, fields, true);
                    }
                }
            } else if (orderType.equals("upgradeUnit")) {
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    String[] infos = {"Please input the level of the unit you want to upgrade!", "Please input the number of unit!"};
                    String[] fields = {"unitLevel", "unitNumber"};
                    setNumbersPrompt(infos, fields, true);
                }
            } else if(orderType.equals("bombOrder")||orderType.equals("cloakOrder")){
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    try {
                        sendJSON();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if(orderType.equals("virusOrder")){
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    String[] infos = {"Please input the level of the virus you want to use!"};
                    String[] fields = {"unitLevel",};
                    setNumbersPrompt(infos, fields, true);
                }
            } else if(orderType.equals("upgradeSpyOrder")){
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    String[] infos = {"Please input the number of units you want to upgrade to spy!"};
                    String[] fields = {"unitNumber"};
                    setNumbersPrompt(infos, fields, true);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    /**
     * init the toSend to make it not null and put some basic fields in
     * if toSend is not null, pop up a error msg and ask the user to cancel first
     * @param orderType the type of the order: "move","attack","upgradeUnit","upgradeTech"
     * @param prompt the prompt to show in the prompt area
     */
    public void initOrder(String orderType, String prompt) {
        if (toSend == null) {
            toSend = new JSONObject();
            toSend.put("type", "order").put("orderType", orderType);
            setPrompt(prompt);
        } else {
            invalidPrompt("You are in the process of issuing an order!\nEither continue or cancel the prev one!");
        }
    }

    /**
     * for move button
     * @param ae mouse click the button
     */
    @FXML
    public void onMove(ActionEvent ae) {
        initOrder("move", "Please select the source territory!");
    }

    public boolean initInGameOrder(String orderType, String prompt){
        if (gameInfo.getSub().equals("Placement")) {
            invalidPrompt("You can only do move order in the first round!\n");
            return false;
        } else {
            initOrder(orderType, prompt);
            return true;
        }
    }
    /**
     * for attack button
     * @param ae mouse click the button
     */
    @FXML
    public void onAttack(ActionEvent ae) {
        initInGameOrder("attack","Please select the source territory!");
    }

    /**
     * for upgrade unit button
     * @param ae mouse click the button
     */
    @FXML
    public void onUpgradeUnit(ActionEvent ae) {
        initInGameOrder("upgradeUnit", "Please select the territory which the units are on!");
    }

    /**
     * for upgradeTech button
     * @param ae mouse click the button
     * @throws IOException
     */
    @FXML
    public void onUpgradeTech(ActionEvent ae) throws IOException {
        if (gameInfo.getSub().equals("Placement")) {
            invalidPrompt("You can only do move order\nEither continue or cancel the prev one!");
        } else if (!gameInfo.getCanUpgrade()) {
            invalidPrompt("You cannot upgrade multiple\n times in one turn!");
        }
        else {
            initOrder("upgradeTech", "");
            sendJSON();
        }
    }

    @FXML
    public void onBomb(ActionEvent ae){
        initInGameOrder("bombOrder", "Please select the territory to bomb!");
    }

    @FXML
    public void onCloak(ActionEvent ae){
        initInGameOrder("cloakOrder","Please select the territory to cloak!");
    }

    @FXML
    public void onUpgradeSpy(ActionEvent ae){
        initInGameOrder("upgradeSpyOrder","Please select the territory which the units are on!");
    }

    @FXML
    public void onResearchCloak(ActionEvent ae) throws IOException {
        if(initInGameOrder("researchClockOrder","")){
            sendJSON();
        }
    }

    @FXML
    public void onMoveSpy(ActionEvent ae){
        initInGameOrder("moveSpyOrder","Please select the territory where the spies are!");
    }

    @FXML
    public void onVirus(ActionEvent ae){
        initInGameOrder("virusOrder","Please select a territory to attack its owner!");
    }

    @FXML
    public void onVaccine(ActionEvent ae){
        if(initInGameOrder("vaccineOrder","")){
            String[] infos = {"Please input the level of vaccine you want to use!"};
            String[] fields = {"unitLevel"};
            setNumbersPrompt(infos,fields,true);
        }
    }

    @FXML
    public void onUpgradeVirus(ActionEvent ae) throws IOException {
        if(initInGameOrder("upgradeVirusMaxLevelOrder","")){
            sendJSON();
        }
    }

    @FXML
    public void onUpgradeVaccine(ActionEvent ae) throws IOException {
        if(initInGameOrder("upgradeVaccineMaxLevelOrder","")){
            sendJSON();
        }
    }

    /**
     * for cancel button
     * @param ae mouse click the button
     */
    @FXML
    public void onCancel(ActionEvent ae) {
        if (toSend != null) {
            toSend = null;
            setPrompt("Order cancelled. What would you like to do?");
        }
    }

//    public void initCommit() throws IOException {
//        if (toSend == null) {
//            toSend = new JSONObject();
//            toSend.put("type", "commit");
//            sendJSON();
//        } else {
//            invalidPrompt("You are in the process of issuing an order!\nCancel the one to commit!");
//        }
//    }

    @FXML
    public void onCommit(ActionEvent ae) throws IOException {
        if (toSend != null) {
            invalidPrompt("You are in the process of issuing an order!\nCancel the one to commit!");
            return;
        }
        client.sendOrderToServer(client.generateCommitJSON());
        JSONObject object = this.client.getSocketClient().receive();
        //System.out.println("Commit response: " + object);
        boolean valid = object.getString("prompt").equals("valid\n");
        if (!valid) {
            Stage stage = App.createDialogStage(primaryStage, "Error", object.getString("reason"));
            stage.show();
        } else {
            Task<JSONObject> task = App.generateBackGroundReceiveTask(client);
            task.valueProperty().addListener(new ChangeListener<JSONObject>() {
                @Override
                public void changed(ObservableValue<? extends JSONObject> observable, JSONObject oldValue, JSONObject newValue) {
                    if (newValue != null) {
                        // Received the next turn json object.
                        //Handle game ends.
                        String playerStatus = newValue.getString("playerStatus");
                        if (playerStatus.equals("E")) {
                            // Create a message box.
                            //Create a wait box.
                            Stage stage = App.createChildStage(primaryStage, "Game ends");
                            //stage.initStyle(StageStyle.UNDECORATED);
                            VBox dialogBox = new VBox(20);
                            Text info = new Text(newValue.getString("reason"));
                            info.setFont(Font.font(18));
                            dialogBox.getChildren().add(info);
                            Button button = new Button("Ok");
                            // We need to make server listen to it again.
                            dialogBox.getChildren().add(button);
                            Scene dialogScene = new Scene(dialogBox, 300, 200);
                            stage.setScene(dialogScene);
                            stage.show();
                            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    try {
                                        client.sendOrderToServer(client.sendPasswordToServer(client.getPassword()));
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
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
                                }
                            });

                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        stage.close();
                                        client.sendOrderToServer(client.sendPasswordToServer(client.getPassword()));
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
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
                                }
                            });
                        } else if (playerStatus.equals("L")) {
                            gameInfo = new GameInfo(newValue);
                            updatePlayerAndTerritory();
                            try {
                                onCommit(null);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            gameInfo = new GameInfo(newValue);
                            updatePlayerAndTerritory();
                        }
                    }


                }
            });
            Thread t = new Thread(task);
            t.start();
            logOutButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
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
                        // Re login into the beginning site.
                        try {
                            client.sendOrderToServer(client.sendPasswordToServer(client.getPassword()));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
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
}
