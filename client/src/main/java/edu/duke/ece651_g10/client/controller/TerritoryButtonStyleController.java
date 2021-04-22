package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.model.ColorStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.HashMap;

public class TerritoryButtonStyleController {

    HashMap<String, Button> buttonMap;

    InGameController inGameController;

    ColorStrategy cs = new ColorStrategy();

    public TerritoryButtonStyleController(){
        buttonMap = new HashMap<>();
    }

    public void setInGameController(InGameController inGameController){
        this.inGameController = inGameController;
    }

    public boolean setButtonStyle(String buttonName,String color){
        Button toSet = buttonMap.getOrDefault(buttonName,null);
        if(toSet==null) {
            return false;
        }
        toSet.setStyle(cs.getRegularStyle(color));
        toSet.setOnMouseEntered(e->toSet.setStyle(cs.getHoverStyle(color)));
        toSet.setOnMouseExited(e->toSet.setStyle(cs.getRegularStyle(color)));
        toSet.setOnMousePressed(e->toSet.setStyle(cs.getPressedStyle(color)));
        toSet.setOnMouseReleased(e->toSet.setStyle(cs.getRegularStyle(color)));
        return true;
    }

    public void addButtons(){}

    @FXML
    public void onClickTerritory(ActionEvent ae) {
        inGameController.onClickTerritory(ae);
    }
}
