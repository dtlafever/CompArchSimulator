package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.util.Register;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class RegisterEditPanelController {

    // property used to temporary bind register textfield in the main panel
    // this is accessable to MainPanelController
    // MainPanelController uses this to update certain register value after
    // this window being closed
    public String newVal = null;

    @FXML
    private TextField re_name;

    @FXML
    private TextField re_val;

    @FXML
    private HBox re_bits;

    @FXML
    private Button re_save;

    @FXML
    private Button re_exit;

    private int bitLength;

    @FXML
    private void initialize() {
        // initialize bitsToValue bidirectional bindings
        UIComponentUtil.bindValueToBits(re_val, re_bits, 16);
    }

    /**
     * reset register edit panel
     */
    public void reset(String name, String value) {
        newVal = value;
        re_name.setText(name);
        re_val.setText(value);

        ObservableList<Node> bits = re_bits.getChildren();
        int bitLength = Register.valueOf(name).getBitLength();
        this.bitLength = bitLength;
        for (int i = 0; i < bits.size(); i++) {
            if (i < bits.size() - bitLength) {
                ((RadioButton) bits.get(i)).setDisable(true);
            } else {
                ((RadioButton) bits.get(i)).setDisable(false);
            }

        }
    }

    @FXML
    void saveHandler(MouseEvent event) {
        // update the value for binding
        newVal = re_val.getText();
        // hide the window
        // it will still exist in the memory, but just hiding
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void exitHandler(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
