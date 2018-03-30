package com.csci6461.team13.simulator.util;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiyuan
 * */
public class UIComponentUtil {

    private UIComponentUtil() {

    }

    /**
     * bidirectionally bind text value to 16 bits
     * they will change correspondingly
     */
    public static void bindValueToBits(TextField textField, HBox hBox, int
            bitLength) {

        List<RadioButton> bits = hBox.getChildren().stream().filter(it
                -> it instanceof RadioButton).map(it -> (RadioButton) it).collect
                (Collectors.toList());
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
                boolean[] booleans = CoreUtil.intToBooleans(value);
                for (int i = 0; i < booleans.length; i++) {
                    bits.get(i).setSelected(booleans[i]);
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
                    booleans[i] = bits.get(i).isSelected();
                }
                int value = CoreUtil.booleansToInt(booleans);
                textField.setText(Integer.toString(value));
            });
        }
    }
}
