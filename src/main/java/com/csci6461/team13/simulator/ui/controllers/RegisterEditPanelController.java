package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.ui.helpers.RegisterEditHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class RegisterEditPanelController {

    // property used to temporary bind register textfield
    StringProperty valueProperty = new SimpleStringProperty();

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

    @FXML
    private void initialize() {

        // add onChange listener to value TextField
        re_val.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String valueToParse = newValue.isEmpty() ? "0" : newValue;
                int value = Integer.parseInt(valueToParse);
                refreshBits(value);
            } catch (Exception ex) {
                // if the new value is invalid
                // restore the old value
                // if old value is empty
                // set to 0
                if (oldValue.isEmpty()) {
                    re_val.setText("0");
                } else {
                    re_val.setText(oldValue);
                }
            }
        });

        // add onChange listener to all RadioButtons
        ObservableList<Node> bits = re_bits.getChildren();
        for (Node btn : bits) {
            ((RadioButton) btn).selectedProperty().addListener((observable, oldValue, newValue) -> {
                boolean[] booleans = new boolean[bits.size()];
                for (int i = 0; i < booleans.length; i++) {
                    booleans[i] = ((RadioButton) bits.get(i)).isSelected();
                }
                refreshValue(booleans);
            });
        }
    }

    /**
     * reset register edit panel
     */
    public void reset(String name, String value) {
        valueProperty.set(value);
        re_name.setText(name);
        re_val.setText(value);
    }

    @FXML
    void saveHandler(MouseEvent event) {
        valueProperty.set(re_val.getText());
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void exitHandler(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void refreshBits(int value) {
        ObservableList<Node> bits = re_bits.getChildren();
        boolean[] booleans = RegisterEditHelper.registerValueToBooleans
                (value);
        for (int i = 0; i < booleans.length; i++) {
            ((RadioButton) bits.get(i)).setSelected(booleans[i]);
        }
    }

    private void refreshValue(boolean[] booleans) {
        int value = RegisterEditHelper.registerBooleansToValue(booleans);
        re_val.setText(Integer.toString(value));
    }
}
