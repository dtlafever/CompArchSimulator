package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.ui.helpers.MemControlHelper;
import com.csci6461.team13.simulator.util.UIComponentUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MemControlController {

    @FXML
    private TextField mc_nval;

    @FXML
    private TextField mc_cval;

    @FXML
    private HBox mc_bits;

    @FXML
    private TextField mc_memaddr;

    @FXML
    private Button mc_store;

    @FXML
    private Button mc_clear;

    @FXML
    private Button mc_settopc;

    private MemControlHelper helper = new MemControlHelper();
    private MainPanelController mainPanelController = null;

    private Signals signals = null;

    @FXML
    private void initialize() {

        this.signals = Simulator.getSignals();

        UIComponentUtil.bindValueToBits(mc_nval, mc_bits, 16);

        mc_memaddr.textProperty().addListener((observable, oldValue, newValue) -> {
            helper.stored.set(false);
            MCU mcu = Simulator.getCpu().getMcu();
            String memAddrStr = mc_memaddr.getText();
            if (!memAddrStr.isEmpty()) {
                // reset current value
                mc_cval.setText(Integer.toString(mcu.getWord(Integer.valueOf(memAddrStr))));
                // new valid value
                helper.memaddr.set(true);
            }else{
                // not new valid value
                helper.memaddr.set(false);
            }
        });

        mc_nval.textProperty().addListener(((observable, oldValue, newValue) -> {
            helper.stored.set(false);
            if (!newValue.isEmpty()) {
                // new valid value signal
                helper.nval.set(true);
            } else {
                // no new valid value
                helper.nval.set(false);
            }
        }));

        mc_store.disableProperty().bind(signals.on.not().or(helper.nval.not().or(helper.memaddr.not())));
        mc_settopc.disableProperty().bind((signals.on.and(helper.stored).not()));
    }

    // place here a hook to access main panel controller
    public void setup(MainPanelController mainPanelController) {
        this.mainPanelController = mainPanelController;
    }

    @FXML
    void clearHandler(MouseEvent event) {
        mc_nval.clear();
        mc_cval.clear();
        mc_memaddr.clear();
        helper.stored.set(false);
    }

    @FXML
    void setToPCHandler(MouseEvent event) {
        // save the address of stored instruction to pc
        Simulator.getCpu().getRegisters().setPC(Integer.valueOf(mc_memaddr.getText()));
        helper.stored.set(false);
        mainPanelController.refreshRegisters(Simulator.getCpu().getRegisters());
    }

    @FXML
    void storeHandler(MouseEvent event) {
        //
        String instStr = mc_nval.getText();
        String memAddrStr = mc_memaddr.getText();
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
        mc_nval.clear();
        helper.stored.set(true);
        // reset new value signal to disable store button
        helper.nval.set(false);
        mc_cval.setText(Integer.toString(Simulator.getCpu().getMcu().getWord(memAddr)));
    }

    public void reset() {
        mc_nval.clear();
        mc_memaddr.clear();
        mc_cval.clear();
    }
    public void refresh() {
        mc_nval.clear();
        mc_memaddr.setText(mc_memaddr.getText());
    }
}
