package com.csci6461.team13.simulator.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UIComponentUtil {

    private UIComponentUtil() {

    }

    /**
     * bidirectionally bind text value to 16 bits
     * they will change correspondingly
     */
    public static void bindValueToBits(TextField textField, HBox hBox, int
            bitLength) {

        ObservableList<Node> bits = hBox.getChildren();
        // convert value to boolean disableProperties
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value;
                if (newValue.isEmpty()) {
                    value = 0;
                } else {
                    value = Integer.parseInt(newValue);
                }
                textField.setText(Integer.toString(value));
                boolean[] booleans = CoreUtil.intToBooleans
                        (value);
                for (int i = 0; i < booleans.length; i++) {
                    ((RadioButton) bits.get(i)).setSelected(booleans[i]);
                }
            } catch (Exception ex) {
                if (oldValue.isEmpty()) {
                    textField.clear();
                }
                textField.setText(oldValue);
            }
        });

        // retrieve value from booleans
        for (Node btn : bits) {
            ((RadioButton) btn).selectedProperty().addListener((observable, oldValue, newValue) -> {
                boolean[] booleans = new boolean[bits.size()];
                for (int i = 0; i < booleans.length; i++) {
                    booleans[i] = ((RadioButton) bits.get(i)).isSelected();
                }
                int value = CoreUtil.booleansToInt(booleans);
                textField.setText(Integer.toString(value));
            });
        }
    }
}
