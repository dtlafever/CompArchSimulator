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
import java.util.Objects;

public class MainPanelController {

    private MemControlController memControlController = null;
    private MainPanelHelper helper = null;

    /**
     * signals - states of different parts of simulator
     * */
    private SimpleStringProperty modeText = new SimpleStringProperty("RUN");

    private Signals signals;

    @FXML
    private TitledPane mOverview;

    @FXML
    private TitledPane mMem;

    @FXML
    private VBox mRegs;

    @FXML
    private Button mIpl;

    @FXML
    private Button mMode;

    @FXML
    private Button mReset;

    @FXML
    private Button mLoad;

    @FXML
    private Button mExit;

    @FXML
    private TextArea mHistory;

    @FXML
    private TextField mExec;

    @FXML
    private Button mStart;

    @FXML
    private Button mNext;

    @FXML
    private TextField mPc;

    @FXML
    private TextField mIr;

    @FXML
    private TextField mMar;

    @FXML
    private TextField mMbr;

    @FXML
    private TextField mMsr;
    @FXML
    private Label mCc;

    @FXML
    private Label mMfr;

    @FXML
    private TextField mR0;

    @FXML
    private TextField mR1;

    @FXML
    private TextField mR2;

    @FXML
    private TextField mR3;

    @FXML
    private TextField mX1;

    @FXML
    private TextField mX2;

    @FXML
    private TextField mX3;

    // tools panel
    @FXML
    private Button mIe;

    @FXML
    void initialize() {
        this.signals = Simulator.getSignals();

        try {
            FXMLLoadResult result = FXMLUtil.loadAsNode("mem_control.fxml");
            memControlController = (MemControlController) result.getController();
            mMem.setContent(result.getNode());
            memControlController.setup(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mIpl.disableProperty().bind(signals.on);
        mOverview.disableProperty().bind(signals.on.not());
        mRegs.disableProperty().bind(signals.on.not());
        mMem.disableProperty().bind(signals.on.not());

        mMode.textProperty().bind(modeText);
        mMode.disableProperty().bind(signals.loaded.not().or(signals.started));
        // currently no load function
//        mLoad.disableProperty().bind(signals.on.not().or(signals.started));
        mLoad.setDisable(true);
        mReset.disableProperty().bind(signals.on.not());

        mStart.disableProperty().bind(signals.loaded.not().or(signals.started));
        mNext.disableProperty().bind(signals.mode.or(signals.started.not()));
        helper = new MainPanelHelper();

        initRegisterBindings();

        // init other bindings
        mHistory.textProperty().bind(helper.history);
        mHistory.textProperty().addListener((observable -> mHistory
                .setScrollTop(Double.MAX_VALUE)));
        mExec.textProperty().bind(helper.exec);

        refreshRegisters(Simulator.getCpu().getRegisters());
    }

    // Menu Buttons Handlers
    @FXML
    void iplHandler(MouseEvent event) {
        MCU mcu = Simulator.getCpu().getMcu();
        Registers registers = Simulator.getCpu().getRegisters();

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
        int index = Const.ROM_ADDR;
        for (String instStr : instructions) {
            Instruction instruction = Instruction.build(instStr);
            Objects.requireNonNull(instruction, "Invalid ROM");
            mcu.storeWord(index++, instruction.toWord());
        }

        registers.setPC(Const.ROM_ADDR);

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
    void exitHandler(MouseEvent event){
        Simulator.getPrimaryStage().close();
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
        toRegisterEdit(mPc, Register.PC.name());
    }

    @FXML
    void irHandler(MouseEvent event) {
        toRegisterEdit(mIr, Register.IR.name());
    }

    @FXML
    void marHandler(MouseEvent event) {
        toRegisterEdit(mMar, Register.MAR.name());
    }

    @FXML
    void mbrHandler(MouseEvent event) {
        toRegisterEdit(mMbr, Register.MBR.name());
    }

    @FXML
    void msrHandler(MouseEvent event) {
        toRegisterEdit(mMsr, Register.MSR.name());
    }

    @FXML
    void r0Handler(MouseEvent event) {
        toRegisterEdit(mR0, Register.R0.name());
    }

    @FXML
    void r1Handler(MouseEvent event) {
        toRegisterEdit(mR1, Register.R1.name());
    }

    @FXML
    void r2Handler(MouseEvent event) {
        toRegisterEdit(mR2, Register.R2.name());
    }

    @FXML
    void r3Handler(MouseEvent event) {
        toRegisterEdit(mR3, Register.R3.name());
    }


    @FXML
    void x1Handler(MouseEvent event) {
        toRegisterEdit(mX1, Register.X1.name());
    }

    @FXML
    void x2Handler(MouseEvent event) {
        toRegisterEdit(mX2, Register.X2.name());
    }

    @FXML
    void x3Handler(MouseEvent event) {
        toRegisterEdit(mX3, Register.X3.name());
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

        mPc.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setPC(Integer.valueOf(newValue)));
        mIr.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setIR(Integer.valueOf(newValue)));
        mMar.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMAR(Integer.valueOf(newValue)));
        mMbr.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMBR(Integer.valueOf(newValue)));
        mMsr.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setMSR(Integer.valueOf(newValue)));
//        mCc.textProperty().addListener((observable, oldValue, newValue) ->
//                regs.setCC(Integer.valueOf(newValue)));
//        mMfr.textProperty().addListener((observable, oldValue, newValue) ->
//                regs.setMFR(Integer.valueOf(newValue)));
        mR0.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR0(Integer.valueOf(newValue)));
        mR1.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR1(Integer.valueOf(newValue)));
        mR2.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR2(Integer.valueOf(newValue)));
        mR3.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setR3(Integer.valueOf(newValue)));
        mX1.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setX1(Integer.valueOf(newValue)));
        mX2.textProperty().addListener((observable, oldValue, newValue) ->
                regs.setX2(Integer.valueOf(newValue)));
        mX3.textProperty().addListener((observable, oldValue, newValue) ->
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
        mPc.setText(Integer.toString(regs.getPC()));
        mIr.setText(Integer.toString(regs.getIR()));
        mMar.setText(Integer.toString(regs.getMAR()));
        mMbr.setText(Integer.toString(regs.getMBR()));
        mMsr.setText(Integer.toString(regs.getMSR()));
        mCc.setText(Integer.toString(regs.getCC()));
        mMfr.setText(Integer.toString(regs.getMFR()));
        mR0.setText(Integer.toString(regs.getR0()));
        mR1.setText(Integer.toString(regs.getR1()));
        mR2.setText(Integer.toString(regs.getR2()));
        mR3.setText(Integer.toString(regs.getR3()));
        mX1.setText(Integer.toString(regs.getX1()));
        mX2.setText(Integer.toString(regs.getX2()));
        mX3.setText(Integer.toString(regs.getX3()));
    }
}
