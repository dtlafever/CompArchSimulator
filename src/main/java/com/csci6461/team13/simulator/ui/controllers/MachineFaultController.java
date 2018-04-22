package com.csci6461.team13.simulator.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * @author zhiyuan
 */
public class MachineFaultController {

    @FXML
    private Button mfOK;

    @FXML
    private Label mfCode;

    @FXML
    private TextArea mfMsg;

    @FXML
    public void initialize(){
        mfOK.setOnAction(event -> {
            ((Node) event.getSource()).getScene().getWindow().hide();
        });
    }

    public void setup(int code, String message){
        mfCode.setText(Integer.toString(code));
        mfMsg.setText(message);
    }
}
