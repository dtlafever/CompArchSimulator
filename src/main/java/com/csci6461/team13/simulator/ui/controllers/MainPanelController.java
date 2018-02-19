package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.ROM;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.ui.helpers.MainPanelHelper;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import com.csci6461.team13.simulator.util.Register;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainPanelController {

    private MemControlController memControlController = null;
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

    // tools panel
    @FXML
    private Button m_ie;

    @FXML
    void initialize() {
        this.signals = Simulator.getSignals();

        try {
            FXMLLoadResult result = FXMLUtil.loadAsNode("mem_control.fxml");
            memControlController = (MemControlController) result.getController();
            m_mem.setContent(result.getNode());
            memControlController.setup(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        m_ipl.disableProperty().bind(signals.on);
        m_overview.disableProperty().bind(signals.on.not());
        m_regs.disableProperty().bind(signals.on.not());
        m_mem.disableProperty().bind(signals.on.not());

        m_mode.textProperty().bind(modeText);
        m_mode.disableProperty().bind(signals.loaded.not().or(signals.started));
        // currently no load function
//        m_load.disableProperty().bind(signals.on.not().or(signals.started));
        m_load.setDisable(true);
        m_reset.disableProperty().bind(signals.on.not());

        m_start.disableProperty().bind(signals.loaded.not().or(signals.started));
        m_next.disableProperty().bind(signals.mode.or(signals.started.not()));
        helper = new MainPanelHelper();

        initRegisterBindings();

        // init other bindings
        m_history.textProperty().bind(helper.history);
        m_history.textProperty().addListener((observable -> m_history
                .setScrollTop(Double.MAX_VALUE)));
        m_exec.textProperty().bind(helper.exec);

        refreshRegisters(Simulator.getCpu().getRegisters());
    }

    // Menu Buttons Handlers
    @FXML
    void iplHandler(MouseEvent event) {
        MCU mcu = Simulator.getCpu().getMcu();
        Registers registers = Simulator.getCpu().getRegisters();

        // setup registers
        registers.setR0(1000);
        registers.setR1(2000);
        registers.setR2(3000);
        registers.setR3(4000);

        registers.setX1(1000);
        registers.setX2(2000);
        registers.setX3(3000);

        // setup direct addressing
        mcu.storeWord(9, 9);
        mcu.storeWord(11, 11);
        mcu.storeWord(13, 13);
        mcu.storeWord(15, 15);
        mcu.storeWord(17, 17);

        // setup indirect addressing
        mcu.storeWord(10, 42);
        mcu.storeWord(12, 44);
        mcu.storeWord(14, 46);
        mcu.storeWord(16, 48);
        mcu.storeWord(18, 50);

        mcu.storeWord(42, 142);
        mcu.storeWord(44, 144);
        mcu.storeWord(46, 146);
        mcu.storeWord(48, 148);
        mcu.storeWord(50, 150);

        ArrayList<String> instructions = ROM.getInstructions();
        // address of the program beginning
        int index = Const.INITIAL_PROGRAM_ADDR;
        for (String instStr : instructions) {
            Instruction instruction = Instruction.build(instStr);
            mcu.storeWord(index++, instruction.toInteger());
        }

        registers.setPC(Const.INITIAL_PROGRAM_ADDR);

        refreshSimulator();

        // power on
        signals.on.set(true);
        signals.loaded.set(true);
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
        resetSimulator();
    }

    @FXML
    void nextHandler(MouseEvent event) {
        // execute one instruction under debug mode
        boolean hasNext = helper.execute(Simulator.getCpu());
        if (!hasNext) {
            // if there is no more instructions
            // reset started signal
            signals.started.set(false);
            helper.updateHistory("--END--");
        } else {
            helper.fetch(Simulator.getCpu());
        }
        refreshSimulator();
    }

    @FXML
    void startHandler(MouseEvent event) {
        // setup the program started
        signals.started.set(true);
        helper.updateHistory("--START--");

        // run program according to different modes
        if (signals.mode.get()) {
            // run mode
            boolean hasNext = true;
            while (hasNext) {
                helper.fetch(Simulator.getCpu());
                hasNext = helper.execute(Simulator.getCpu());
                // refresh register values on the stage
                refreshRegisters(Simulator.getCpu().getRegisters());
            }
            helper.updateHistory("--END--");
            // reset started signal
            signals.started.set(false);
        } else {
            // debug mode
            helper.fetch(Simulator.getCpu());
            refreshSimulator();
        }
    }

    // tools panel handlers
    @FXML
    void ieHandler(MouseEvent event) {
        toInstEdit();
    }

    @FXML
    void peHandler(MouseEvent event) {

    }

    // Registers Handlers
    @FXML
    void pcHandler(MouseEvent event) {
        toRegisterEdit(m_pc, Register.PC.name());
    }

    @FXML
    void irHandler(MouseEvent event) {
        toRegisterEdit(m_ir, Register.IR.name());
    }

    @FXML
    void marHandler(MouseEvent event) {
        toRegisterEdit(m_mar, Register.MAR.name());
    }

    @FXML
    void mbrHandler(MouseEvent event) {
        toRegisterEdit(m_mbr, Register.MBR.name());
    }

    @FXML
    void msrHandler(MouseEvent event) {
        toRegisterEdit(m_msr, Register.MSR.name());
    }

    @FXML
    void r0Handler(MouseEvent event) {
        toRegisterEdit(m_r0, Register.R0.name());
    }

    @FXML
    void r1Handler(MouseEvent event) {
        toRegisterEdit(m_r1, Register.R1.name());
    }

    @FXML
    void r2Handler(MouseEvent event) {
        toRegisterEdit(m_r2, Register.R2.name());
    }

    @FXML
    void r3Handler(MouseEvent event) {
        toRegisterEdit(m_r3, Register.R3.name());
    }


    @FXML
    void x1Handler(MouseEvent event) {
        toRegisterEdit(m_x1, Register.X1.name());
    }

    @FXML
    void x2Handler(MouseEvent event) {
        toRegisterEdit(m_x2, Register.X2.name());
    }

    @FXML
    void x3Handler(MouseEvent event) {
        toRegisterEdit(m_x3, Register.X3.name());
    }

    /**
     * registerEditor is a Singleton object
     */
    private void toRegisterEdit(TextField register, String name) {

        FXMLLoadResult editorResult = helper.getRegisterEditor(register,
                name);
        Stage registerEditor = editorResult.getStage();
        RegisterEditPanelController controller = (RegisterEditPanelController) editorResult.getController();
        registerEditor.showAndWait();
        register.setText(controller.newVal);
    }

    private void toInstEdit() {
        FXMLLoadResult editorResult = helper.getInstEditor(Simulator.getPrimaryStage(),
                Modality.NONE);
        Stage registerEditor = editorResult.getStage();
        InstEditController controller = (InstEditController) editorResult.getController();
        controller.reset();
        registerEditor.show();
    }

    /**
     * add listeners to all TextFields
     * when the text value changes, it will save the value to Registers
     * automatically
     * <p>
     * CC and MFR is not changeable for user
     */
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

    /**
     * refresh all register value to latest
     */
    public void refreshSimulator() {
        Registers registers = Simulator.getCpu().getRegisters();
        // refresh registers
        refreshRegisters(registers);
        // refresh mem control
        memControlController.refresh();
    }

    /**
     * reset the whole simulator to original state
     */
    public void resetSimulator() {
        // reset signals
        signals.mode.set(true);
        modeText.set("RUN");
        signals.on.set(false);
        signals.loaded.set(false);
        signals.started.set(false);

        // reset over texts
        helper.exec.set("");
        helper.history.set("");
        helper.historyLen = 0;
        // reset mem control
        memControlController.reset();

        // reset cpu
        Simulator.getCpu().reset();
        // reset registers
        refreshSimulator();
    }

    /**
     * refresh all register value to latest
     */
    public void refreshRegisters(Registers regs) {
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
}
