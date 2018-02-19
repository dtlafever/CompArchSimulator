package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class InstEditController {

    // signal a new valid instruction
    private SimpleBooleanProperty newValSignal = new SimpleBooleanProperty(false);
    private SimpleStringProperty inst = new SimpleStringProperty("");
    private SimpleStringProperty error = new SimpleStringProperty();

    @FXML
    private TextField ie_inst;

    @FXML
    private Text ie_error;

    @FXML
    private Button ie_instCopy;

    @FXML
    private Button ie_valCopy;

    @FXML
    private TextField ie_val;

    @FXML
    private HBox ie_bits;

    @FXML
    private Button ie_exit;

    @FXML
    void initialize() {

        UIComponentUtil.bindValueToBits(ie_val, ie_bits, 16);

        ie_instCopy.disableProperty().bind(newValSignal.not());
        ie_valCopy.disableProperty().bind(newValSignal.not());
        ie_error.textProperty().bind(error);

        ie_inst.textProperty().addListener((observable, oldValue, newValue) -> {
            Instruction instruction = Instruction.build(newValue);
            if (instruction != null) {
                // a valid instruction
                ie_val.setText(String.valueOf(instruction.toInteger()));
                newValSignal.set(true);
            }
        });

        ie_val.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(ie_val.getText());
                Instruction instruction = Instruction.build(value);
                if (instruction != null) {
                    // valid instruction
                    inst.set(instruction.toString());
                    newValSignal.set(true);
                    error.set("");
                    ie_inst.setStyle("-fx-text-inner-color: black;");
                    ie_val.setStyle("-fx-text-inner-color: black;");
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                ie_inst.setStyle("-fx-text-inner-color: red;");
                ie_val.setStyle("-fx-text-inner-color: red;");
                error.set("Not A Valid Instruction");
                // not a valid instruction
                inst.set("");
                newValSignal.set(false);
            }
        });

        inst.addListener((observable, oldValue, newValue) -> {
            Instruction instruction = Instruction.build(newValue);
            if (instruction == null) {
                // not a valid instruction
                ie_inst.setStyle("-fx-text-inner-color: red;");
                ie_val.setStyle("-fx-text-inner-color: red;");
                error.set("Not A Valid Instruction");
                newValSignal.set(false);
            } else {
                ie_inst.setStyle("-fx-text-inner-color: black;");
                ie_val.setStyle("-fx-text-inner-color: black;");
                ie_val.setText(String.valueOf(instruction.toInteger()));
                error.set("");
                newValSignal.set(true);
            }
        });

        ie_inst.textProperty().bindBidirectional(inst);

        ie_inst.clear();
        ie_val.clear();
    }

    public void reset() {
        error.set("");
//        inst.set("");
        ie_val.setText("0");
        newValSignal.set(false);
    }

    @FXML
    void instCopyHandler(MouseEvent event) {
        setupCopyBtn(ie_inst, event);
    }

    @FXML
    void valCopyHandler(MouseEvent event) {
        setupCopyBtn(ie_val, event);
    }

    private void setupCopyBtn(TextField textField, MouseEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(textField.getText());
        clipboard.setContent(content);
    }

    @FXML
    void exitHandler(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
