package edu.duke.ece651_g10.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.HashMap;

public class TerritoryFor5ButtonStyleController extends TerritoryButtonStyleController{
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
    @FXML
    Button buttonI;
    @FXML
    Button buttonJ;
    @FXML
    Button buttonK;
    @FXML
    Button buttonL;
    @FXML
    Button buttonM;
    @FXML
    Button buttonN;
    @FXML
    Button buttonO;

    public TerritoryFor5ButtonStyleController() {
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
        buttonMap.put("I", buttonI);
        buttonMap.put("J", buttonJ);
        buttonMap.put("K", buttonK);
        buttonMap.put("L", buttonL);
        buttonMap.put("M", buttonM);
        buttonMap.put("N", buttonN);
        buttonMap.put("O", buttonO);
    }
}
