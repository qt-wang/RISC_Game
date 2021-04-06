package edu.duke.ece651_g10.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NumButtonController {
    int a;
    public NumButtonController(){
        a = 0;
    }
    public void onLeft(ActionEvent ae){
        System.out.println("left");
    }
    public void onRight(ActionEvent ae){
        System.out.println("right");
    }
}
