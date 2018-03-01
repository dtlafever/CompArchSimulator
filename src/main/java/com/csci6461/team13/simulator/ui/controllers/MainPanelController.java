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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.IntConsumer;

public class MainPanelController {

    private MemControlController memControlController = null;
    private MainPanelHelper helper = null;

    private static String REGISTER_PROPERTY_NAME = "regName";

    /**
     * signals - states of different parts of simulator
     */
    private SimpleStringProperty modeText = new SimpleStringProperty();

    private Signals signals;

    @FXML
    private HBox mOverview;

    @FXML
    private HBox mMem;

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

        // initialize register properties
        putRegProperty(mPc, Register.PC);
        putRegProperty(mIr, Register.IR);
        putRegProperty(mMar, Register.MAR);
        putRegProperty(mMbr, Register.MBR);
        putRegProperty(mMsr, Register.MSR);
        putRegProperty(mR0, Register.R0);
        putRegProperty(mR1, Register.R1);
        putRegProperty(mR2, Register.R2);
        putRegProperty(mR3, Register.R3);
        putRegProperty(mX1, Register.X1);
        putRegProperty(mX2, Register.X2);
        putRegProperty(mX3, Register.X3);

        try {
            FXMLLoadResult result = FXMLUtil.loadAsNode("mem_control.fxml");
            memControlController = (MemControlController) result.getController();
            mMem.getChildren().add(result.getNode());
            memControlController.setup(this);
            mMem.autosize();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mIpl.disableProperty().bind(signals.on);
        mOverview.disableProperty().bind(signals.on.not());
//        mRegs.disableProperty().bind(signals.on.not());
//        mMem.disableProperty().bind(signals.on.not());

        StringBinding modeText = Bindings.createStringBinding(() -> signals
                .mode.get() ? "RUN" : "DEBUG", signals.mode);
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
        mHistory.textProperty().bindBidirectional(helper.consoleOutput);
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
    }

    @FXML
    void resetHandler(MouseEvent event) {
        resetSimulator();
    }

    @FXML
    void exitHandler(MouseEvent event) {
        Simulator.getPrimaryStage().close();
    }

    @FXML
    void nextHandler(MouseEvent event) {
        // execute one instruction under debug mode
        boolean hasNext = helper.execute(Simulator.getCpu());
        updateHistory(helper.nextWord.get(), helper.nextAddr.get());
        if (!hasNext) {
            // if there is no more instructions
            // reset started signal
            signals.started.set(false);
            updateHistory("--END--");
        } else {
            helper.fetch(Simulator.getCpu());
        }
        refreshSimulator();
    }

    @FXML
    void startHandler(MouseEvent event) {
        // setup the program started
        signals.started.set(true);
        updateHistory("--START--");

        // run program according to different modes
        if (signals.mode.get()) {
            // run mode
            boolean hasNext = true;
            while (hasNext) {
                helper.fetch(Simulator.getCpu());
                hasNext = helper.execute(Simulator.getCpu());
                updateHistory(helper.nextWord.get(), helper.nextAddr.get());
                // refresh register values on the stage
                refreshRegisters(Simulator.getCpu().getRegisters());
            }
            updateHistory("--END--");
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
    void registerHandler(MouseEvent event) {
        TextField register = (TextField) event.getSource();
        toRegisterEdit(register, (String) register.getProperties().get
                (REGISTER_PROPERTY_NAME));
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
        addRegListener(mPc, regs::setPC);
        addRegListener(mIr, regs::setIR);
        addRegListener(mMar, regs::setMAR);
        addRegListener(mMbr, regs::setMBR);
        addRegListener(mMsr, regs::setMSR);
        addRegListener(mR0, regs::setR0);
        addRegListener(mR1, regs::setR1);
        addRegListener(mR2, regs::setR2);
        addRegListener(mR3, regs::setR3);
        addRegListener(mX1, regs::setX1);
        addRegListener(mX2, regs::setX2);
        addRegListener(mX3, regs::setX3);
    }

    /**
     * refresh all register value to latest
     */
    private void refreshSimulator() {
        Registers registers = Simulator.getCpu().getRegisters();
        // refresh registers
        refreshRegisters(registers);
        // refresh mem control
        memControlController.refresh();
    }

    /**
     * reset the whole simulator to original state
     */
    private void resetSimulator() {
        // reset signals
        signals.mode.set(true);
        modeText.set("RUN");
        signals.on.set(false);
        signals.loaded.set(false);
        signals.started.set(false);

        // reset over texts
        helper.exec.set("");
        helper.consoleOutput.set("");
        helper.executedInstCount = 0;
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
        refreshText(mPc, regs.getPC());
        refreshText(mIr, regs.getIR());
        refreshText(mMar, regs.getMAR());
        refreshText(mMbr, regs.getMBR());
        refreshText(mMsr, regs.getMSR());
        refreshText(mCc, regs.getCC());
        refreshText(mMfr, regs.getMFR());
        refreshText(mR0, regs.getR0());
        refreshText(mR1, regs.getR1());
        refreshText(mR2, regs.getR2());
        refreshText(mR3, regs.getR3());
        refreshText(mX1, regs.getX1());
        refreshText(mX2, regs.getX2());
        refreshText(mX3, regs.getX3());
    }

    private void updateHistory(int word, int addr) {
        String instStr = Instruction.build(word).toString();
        String line = String.format("Executed: %s(%d)\t@ [%d]\nMSG: \n",
                instStr, word,addr);
        // update execution consoleOutput
        mHistory.appendText(line);
    }

    private void updateHistory(String line) {
        // update execution consoleOutput
        mHistory.appendText(String.format("[%s]\n", line));
    }

    // utility methods
    private static void refreshText(Label label, int regVal) {
        label.setText(Integer.toString(regVal));
    }

    private static void refreshText(TextField textField, int regVal) {
        textField.setText(Integer.toString(regVal));
    }

    private static void addRegListener(TextField textField, IntConsumer consumer) {
        textField.textProperty().addListener((observable, oldValue, newValue)
                -> consumer.accept(Integer.valueOf(newValue)));
    }

    private static void putRegProperty(TextField textField, Register register) {
        textField.getProperties().put(REGISTER_PROPERTY_NAME, register.name());
    }

}
