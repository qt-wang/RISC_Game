package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.Client;
import edu.duke.ece651_g10.client.model.GameInfo;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class InGameController {
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

    public InGameController(GameInfo gameInfo, Stage primaryStage, Client client) {
        this.gameInfo = gameInfo;
        this.primaryStage = primaryStage;
        this.client = client;
    }

    public void setPrompt(String str) {
        prompt.setText(str);
    }

    //TODO:
//    public void updateGameInfo(JSONObject obj){
//        gameInfo = new GameInfo(obj);
//    }

    public void setPlayerInfo() {
        ObservableList<String> obl = FXCollections.observableArrayList(gameInfo.getPlayerInfo());
        playerInfo.setItems(obl);
        playerInfo.refresh();
    }

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

    @FXML
    public void onEnterStage(MouseEvent ae) {
        setPlayerInfo();
    }

    @FXML
    public void onClickPane(MouseEvent ae) {
        setTerritoryInfo("");
    }

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
                    sendJSON();
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

    public void sendJSON() {
        //send and receive reply
        System.out.println(toSend.toString());
        setPrompt("What would you like to do?");
        toSend = null;
    }

    @FXML
    public void onClickTerritory(ActionEvent ae) {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            setTerritoryInfo(btn.getText());
            if (toSend == null) return;
            if (toSend.getString("orderType").equals("move") || toSend.getString("orderType").equals("attack")) {
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    setPrompt("Please select the destination territory!");
                } else if (!toSend.has("destTerritory")) {
                    toSend.put("destTerritory", btn.getText());
                    //for specific level of unit and unit number
//                    String[] infos = {"Please input the level of the unit you want to send!","Please input the number of unit!"};
//                    String[] fields = {"unitLevel","unitNumber"};
//                    setNumbersPrompt(infos, fields, true);
                    String[] infos = {"Please input the number of unit!"};
                    String[] fields = {"unitNumber"};
                    setNumbersPrompt(infos, fields, true);
                }
            } else if (toSend.getString("orderType").equals("upgradeUnit")) {
                if (!toSend.has("sourceTerritory")) {
                    toSend.put("sourceTerritory", btn.getText());
                    String[] infos = {"Please input the level of the unit you want to upgrade!", "Please input the number of unit!"};
                    String[] fields = {"unitLevel", "unitNumber"};
                    setNumbersPrompt(infos, fields, true);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    public void initOrder(String orderType, String prompt) {
        if (toSend == null) {
            toSend = new JSONObject();
            toSend.put("type", "order").put("orderType", orderType);
            setPrompt(prompt);
        } else {
            invalidPrompt("You are in the process of issuing an order!\nEither continue or cancel the prev one!");
        }
    }

    @FXML
    public void onMove(ActionEvent ae) {
        initOrder("move", "Please select the source territory!");
    }

    @FXML
    public void onAttack(ActionEvent ae) {
        initOrder("attack", "Please select the source territory!");
    }

    @FXML
    public void onUpgradeUnit(ActionEvent ae) {
        initOrder("upgradeUnit", "Please select the territory which the units are on!");
    }

    @FXML
    public void onUpgradeTech(ActionEvent ae) {
        if (toSend == null) {
            initOrder("upgradeTech", "");
            sendJSON();
        } else {
            invalidPrompt("You are in the process of issuing an order!\nCancel the one to commit!");
        }

    }

    @FXML
    public void onCancel(ActionEvent ae) {
        if (toSend != null) {
            toSend = null;
            setPrompt("Order cancelled. What would you like to do?");
        }
    }

    public void initCommit() {
        if (toSend == null) {
            toSend = new JSONObject();
            toSend.put("type", "commit");
            sendJSON();
        } else {
            invalidPrompt("You are in the process of issuing an order!\nCancel the one to commit!");
        }
    }

    @FXML
    public void onCommit(ActionEvent ae) throws IOException {
        initCommit();
        JSONObject object = this.client.getSocketClient().receive();
        System.out.println("Commit response: " + object);
    }
}
