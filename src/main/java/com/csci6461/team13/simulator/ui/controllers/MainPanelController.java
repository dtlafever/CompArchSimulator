package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.Instruction;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.ui.helpers.MainPanelHelper;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainPanelController {

    private Stage registerEditor = null;
    private RegisterEditPanelController registerEditPanelController = null;
    private MainPanelHelper helper = null;

    // signals - states of different parts of simulator
    private SimpleStringProperty modeText = new SimpleStringProperty("RUN");

    private Signals signals;

    @FXML
    private TitledPane m_overview;

    @FXML
    private TitledPane m_mem;

    @FXML
    private VBox m_regs;

    @FXML
    private Button m_ipl;

    @FXML
    private Button m_mode;

    @FXML
    private Button m_reset;

    @FXML
    private Button m_load;

    @FXML
    private TextArea m_history;

    @FXML
    private TextField m_exec;

    @FXML
    private Button m_start;

    @FXML
    private Button m_next;

    @FXML
    private TextField m_pc;

    @FXML
    private TextField m_ir;

    @FXML
    private TextField m_mar;

    @FXML
    private TextField m_mbr;

    @FXML
    private TextField m_msr;
    @FXML
    private Label m_cc;

    @FXML
    private Label m_mfr;

    @FXML
    private TextField m_r0;

    @FXML
    private TextField m_r1;

    @FXML
    private TextField m_r2;

    @FXML
    private TextField m_r3;

    @FXML
    private TextField m_x1;

    @FXML
    private TextField m_x2;

    @FXML
    private TextField m_x3;

    @FXML
    void initialize() {
        this.signals = Simulator.getSignals();

        try {
            FXMLLoadResult result = FXMLUtil.loadAsNode("mem_control.fxml");
            m_mem.setContent(result.getNode());
            ((MemControlController)result.getController()).setup(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        m_ipl.disableProperty().bind(signals.on);
        m_overview.disableProperty().bind(signals.on.not());
        m_regs.disableProperty().bind(signals.on.not());
        m_mem.disableProperty().bind(signals.on.not());

        m_mode.textProperty().bind(modeText);
        m_mode.disableProperty().bind(signals.loaded.not().or(signals.started));
        m_load.disableProperty().bind(signals.on.not().or(signals.started));
        m_reset.disableProperty().bind(signals.on.not().or(signals.started));

        m_start.disableProperty().bind(signals.loaded.not().or(signals.started));
        m_next.disableProperty().bind(signals.mode.or(signals.started.not()));
        helper = new MainPanelHelper();

        initRegisterBindings();

        // init other bindings
        m_history.textProperty().bind(helper.history);
        m_exec.textProperty().bind(helper.exec);
    }

    // Menu Buttons Handlers
    @FXML
    void iplHandler(MouseEvent event) {
        // todo run ipl code

        // power on
        signals.on.set(true);
    }

    @FXML
    void loadHandler(MouseEvent event) {
        // todo load program

        // program loaded
        signals.loaded.set(true);
    }

    @FXML
    void modeHandler(MouseEvent event) {
        // toggle
        signals.mode.set(!signals.mode.get());
        modeText.set(signals.mode.get() ? "RUN" : "DEBUG");
    }

    @FXML
    void resetHandler(MouseEvent event) {
        signals.mode.set(true);
        modeText.set("RUN");
        signals.on.set(false);
        signals.loaded.set(false);
        signals.started.set(false);

        Simulator.initCPU();
        refreshRegisters(Simulator.getCpu().getRegisters());
    }

    @FXML
    void nextHandler(MouseEvent event) {
        // execute one instruction under debug mode
        boolean hasNext = helper.execute(Simulator.getCpu());
        if(!hasNext){
            // if there is not more instructions
            // reset started signal
            signals.started.set(false);
        }else{
            // TODO fetch next, set to exec
        }
    }

    @FXML
    void startHandler(MouseEvent event) {
        // setup the program started
        signals.started.set(true);

        // run program according to different modes
        if (signals.mode.get()) {
            // run mode
            boolean hasNext = true;
            while (hasNext){
                hasNext = helper.execute(Simulator.getCpu());
                // refresh register values on the stage
                refreshRegisters(Simulator.getCpu().getRegisters());
            }
            // reset started signal
            signals.started.set(false);
        } else {
            // debug mode
            // TODO fetch next, set to exec
        }
    }

    // Registers Handlers
    @FXML
    void pcHandler(MouseEvent event) {
        toEdit(m_pc, "PC");
    }

    @FXML
    void irHandler(MouseEvent event) {
        toEdit(m_ir, "IR");
    }

    @FXML
    void marHandler(MouseEvent event) {
        toEdit(m_mar, "MAR");
    }

    @FXML
    void mbrHandler(MouseEvent event) {
        toEdit(m_mbr, "MBR");
    }

    @FXML
    void msrHandler(MouseEvent event) {
        toEdit(m_msr, "MSR");
    }

    @FXML
    void r0Handler(MouseEvent event) {
        toEdit(m_r0, "R0");
    }

    @FXML
    void r1Handler(MouseEvent event) {
        toEdit(m_r1, "R1");
    }

    @FXML
    void r2Handler(MouseEvent event) {
        toEdit(m_r2, "R2");
    }

    @FXML
    void r3Handler(MouseEvent event) {
        toEdit(m_r3, "R3");
    }


    @FXML
    void x1Handler(MouseEvent event) {
        toEdit(m_x1, "X1");
    }

    @FXML
    void x2Handler(MouseEvent event) {
        toEdit(m_x2, "X2");
    }

    @FXML
    void x3Handler(MouseEvent event) {
        toEdit(m_x3, "X3");
    }

    /**
     * registerEditor is a Singleton object
     */
    private void toEdit(TextField register, String name) {

        if (registerEditor == null) {
            registerEditor = new Stage();
            try {
                FXMLLoadResult result;
                result = FXMLUtil.loadAsNode("register_edit.fxml");
                registerEditPanelController = (RegisterEditPanelController) result.getController();
                FXMLUtil.addStylesheets(result.getNode(), "bootstrap3.css");
                registerEditor.setScene(new Scene(result.getNode()));
                registerEditor.setResizable(false);
                registerEditor.setTitle("Register Editor");
                registerEditor.initModality(Modality.WINDOW_MODAL);
                registerEditor.initOwner(Simulator.getPrimaryStage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String originalValue = register.getText();
        registerEditPanelController.reset(name, originalValue);
        registerEditor.showAndWait();
        register.setText(registerEditPanelController.newVal);
    }

    private void initRegisterBindings() {

        Registers regs = Simulator.getCpu().getRegisters();

        m_pc.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setPC(Integer.valueOf(newValue)));
        m_ir.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setIR(Integer.valueOf(newValue)));
        m_mar.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMAR(Integer.valueOf(newValue)));
        m_mbr.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMBR(Integer.valueOf(newValue)));
        m_msr.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMSR(Integer.valueOf(newValue)));
//        m_cc.textProperty().addListener((observable, oldValue, newValue) ->
//                regs.setCC(Integer.valueOf(newValue)));
//        m_mfr.textProperty().addListener((observable, oldValue, newValue) ->
//                regs.setMFR(Integer.valueOf(newValue)));
        m_r0.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR0(Integer.valueOf(newValue)));
        m_r1.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR1(Integer.valueOf(newValue)));
        m_r2.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR2(Integer.valueOf(newValue)));
        m_r3.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR3(Integer.valueOf(newValue)));
        m_x1.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setX1(Integer.valueOf(newValue)));
        m_x2.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setX2(Integer.valueOf(newValue)));
        m_x3.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setX3(Integer.valueOf(newValue)));
    }

    public void refreshRegisters(Registers regs){
        m_pc.setText(Integer.toString(regs.getPC()));
        m_ir.setText(Integer.toString(regs.getIR()));
        m_mar.setText(Integer.toString(regs.getMAR()));
        m_mbr.setText(Integer.toString(regs.getMBR()));
        m_msr.setText(Integer.toString(regs.getMSR()));
        m_cc.setText(Integer.toString(regs.getCC()));
        m_mfr.setText(Integer.toString(regs.getMFR()));
        m_r0.setText(Integer.toString(regs.getR0()));
        m_r1.setText(Integer.toString(regs.getR1()));
        m_r2.setText(Integer.toString(regs.getR2()));
        m_r3.setText(Integer.toString(regs.getR3()));
        m_x1.setText(Integer.toString(regs.getX1()));
        m_x2.setText(Integer.toString(regs.getX2()));
        m_x3.setText(Integer.toString(regs.getX3()));
    }

    public void refreshEntirePanel(){

    }
}
