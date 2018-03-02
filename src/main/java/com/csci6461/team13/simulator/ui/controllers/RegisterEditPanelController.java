package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.util.Register;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterEditPanelController {

    // property used to temporary bind register textfield in the main panel
    // this is accessable to MainPanelController
    // MainPanelController uses this to update certain register value after
    // this window being closed
    public String newVal = null;

    @FXML
    private TextField reName;

    @FXML
    private TextField reVal;

    @FXML
    private HBox reBits;

    @FXML
    private Button reSave;

    @FXML
    private Button reExit;

    private int bitLength;

    @FXML
    private void initialize() {
        // initialize bitsToValue bidirectional bindings
        UIComponentUtil.bindValueToBits(reVal, reBits, 16);
    }

    /**
     * flush register edit panel
     */
    public void reset(String name, String value) {
        newVal = value;
        reName.setText(name);
        reVal.setText(value);

        List<RadioButton> bits = reBits.getChildren().stream().filter(it -> it instanceof RadioButton).map(it -> (RadioButton)it).collect(Collectors.toList());
        int bitLength = Register.valueOf(name).getBitLength();
        this.bitLength = bitLength;
        for (int i = 0; i < bits.size(); i++) {
            if (i < bits.size() - bitLength) {
                bits.get(i).setDisable(true);
            } else {
                bits.get(i).setDisable(false);
            }
        }
    }

    @FXML
    void saveHandler(MouseEvent event) {
        // update the value for binding
        newVal = reVal.getText();
        // hide the window
        // it will still exist in the memory, but just hiding
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void exitHandler(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
