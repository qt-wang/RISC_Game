package edu.duke.ece651_g10.client.controller;

import edu.duke.ece651_g10.client.model.GameInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.HashMap;

public class TerritoryFor2ButtonStyleController extends TerritoryButtonStyleController{
    @FXML
    Button buttonA;
    @FXML
    Button buttonB;
    @FXML
    Button buttonC;
    @FXML
    Button buttonD;
    @FXML
    Button buttonE;
    @FXML
    Button buttonF;
    @FXML
    Button buttonG;
    @FXML
    Button buttonH;

    public TerritoryFor2ButtonStyleController() {
        buttonMap = new HashMap<>();
    }

    @Override
    public void addButtons(){
        buttonMap.put("A", buttonA);
        buttonMap.put("B", buttonB);
        buttonMap.put("C", buttonC);
        buttonMap.put("D", buttonD);
        buttonMap.put("E", buttonE);
        buttonMap.put("F", buttonF);
        buttonMap.put("G", buttonG);
        buttonMap.put("H", buttonH);
    }

}
