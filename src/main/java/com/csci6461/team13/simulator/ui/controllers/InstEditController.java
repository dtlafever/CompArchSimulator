package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private BooleanProperty newValSignal = new SimpleBooleanProperty(false);
    private StringProperty inst = new SimpleStringProperty("");
    private StringProperty error = new SimpleStringProperty();

    @FXML
    private TextField ieInst;

    @FXML
    private Text ieError;

    @FXML
    private Button ieInstCopy;

    @FXML
    private Button ieValCopy;

    @FXML
    private TextField ieVal;

    @FXML
    private HBox ieBits;

    @FXML
    private Button ieExit;

    @FXML
    void initialize() {

        UIComponentUtil.bindValueToBits(ieVal, ieBits, 16);

        ieInstCopy.disableProperty().bind(newValSignal.not());
        ieValCopy.disableProperty().bind(newValSignal.not());
        ieError.textProperty().bind(error);

        ieInst.textProperty().addListener((observable, oldValue, newValue) -> {
            Instruction instruction = Instruction.build(newValue);
            if (instruction != null) {
                // a valid instruction
                ieVal.setText(String.valueOf(instruction.toWord()));
                newValSignal.set(true);
            }
        });

        ieVal.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(ieVal.getText());
                Instruction instruction = Instruction.build(value);
                if (instruction != null) {
                    // valid instruction
                    inst.set(instruction.toString());
                    newValSignal.set(true);
                    error.set("");
                    ieInst.setStyle("-fx-text-inner-color: black;");
                    ieVal.setStyle("-fx-text-inner-color: black;");
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                ieInst.setStyle("-fx-text-inner-color: red;");
                ieVal.setStyle("-fx-text-inner-color: red;");
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
                ieInst.setStyle("-fx-text-inner-color: red;");
                ieVal.setStyle("-fx-text-inner-color: red;");
                error.set("Not A Valid Instruction");
                newValSignal.set(false);
            } else {
                ieInst.setStyle("-fx-text-inner-color: black;");
                ieVal.setStyle("-fx-text-inner-color: black;");
                ieVal.setText(String.valueOf(instruction.toWord()));
                error.set("");
                newValSignal.set(true);
            }
        });

        ieInst.textProperty().bindBidirectional(inst);

        ieInst.clear();
        ieVal.clear();
    }

    /**
     * flush the whole panel
     * */
    public void reset() {
        ieVal.setText("0");
    }

    @FXML
    void instCopyHandler(MouseEvent event) {
        setupCopyBtn(ieInst, event);
    }

    @FXML
    void valCopyHandler(MouseEvent event) {
        setupCopyBtn(ieVal, event);
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
