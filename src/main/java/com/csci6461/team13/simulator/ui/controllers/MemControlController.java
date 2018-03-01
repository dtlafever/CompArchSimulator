package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.ui.helpers.MemControlHelper;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MemControlController {

    @FXML
    private TextField mcNval;

    @FXML
    private TextField mcCval;

    @FXML
    private HBox mcBits;

    @FXML
    private TextField mcMemAddr;

    @FXML
    private Button mcStore;

    @FXML
    private Button mcClear;

    @FXML
    private Button mcSettopc;

    @FXML
    private HBox mMemGrid;

    @FXML
    private HBox mCacheGrid;

    private MemControlHelper helper = new MemControlHelper();
    private MainPanelController mainPanelController = null;

    @FXML
    private void initialize() {

        Signals signals = Simulator.getSignals();

        UIComponentUtil.bindValueToBits(mcNval, mcBits, 16);

        mcMemAddr.textProperty().addListener((observable, oldValue, newValue) -> {
            helper.stored.set(false);
            MCU mcu = Simulator.getCpu().getMcu();
            String memAddrStr = mcMemAddr.getText();
            if (!memAddrStr.isEmpty()) {
                // reset current value
                mcCval.setText(Integer.toString(mcu.getWord(Integer.valueOf(memAddrStr))));
                // new valid value
                helper.memaddr.set(true);
            } else {
                // not new valid value
                helper.memaddr.set(false);
            }
        });

        mcNval.textProperty().addListener(((observable, oldValue, newValue) -> {
            helper.stored.set(false);
            if (!newValue.isEmpty()) {
                // new valid value signal
                helper.nval.set(true);
            } else {
                // no new valid value
                helper.nval.set(false);
            }
        }));

        mcStore.disableProperty().bind(signals.on.not().or(helper.nval.not().or(helper.memaddr.not())));
        mcSettopc.disableProperty().bind((signals.on.and(helper.stored).not()));
    }

    // place here a hook to access main panel controller
    public void setup(MainPanelController mainPanelController) {
        this.mainPanelController = mainPanelController;
    }

    @FXML
    void clearHandler(MouseEvent event) {
        mcNval.clear();
        mcCval.clear();
        mcMemAddr.clear();
        helper.stored.set(false);
    }

    @FXML
    void setToPCHandler(MouseEvent event) {
        // save the address of stored instruction to pc
        Simulator.getCpu().getRegisters().setPC(Integer.valueOf(mcMemAddr.getText()));
        helper.stored.set(false);
        mainPanelController.refreshRegisters(Simulator.getCpu().getRegisters());
    }

    @FXML
    void storeHandler(MouseEvent event) {
        String instStr = mcNval.getText();
        String memAddrStr = mcMemAddr.getText();
        int memAddr = 0;
        int inst;

        if (!instStr.isEmpty()) {
            inst = Integer.valueOf(instStr);
        } else {
            return;
        }

        // store the new instruction to memory
        if (!memAddrStr.isEmpty()) {
            memAddr = Integer.valueOf(memAddrStr);
        }

        Simulator.getCpu().getMcu().storeWord(memAddr, inst);
        mcNval.clear();
        helper.stored.set(true);
        // reset new value signal to disable store button
        helper.nval.set(false);
        mcCval.setText(Integer.toString(Simulator.getCpu().getMcu().getWord(memAddr)));
    }

    // clear all values
    public void reset() {
        mcNval.clear();
        mcMemAddr.clear();
        mcCval.clear();
    }

    public void refresh() {
        mcNval.clear();
        mcMemAddr.setText(mcMemAddr.getText());
    }



}
