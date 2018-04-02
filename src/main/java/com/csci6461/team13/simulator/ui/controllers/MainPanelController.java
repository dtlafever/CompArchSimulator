package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.CardReader;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.core.io.Keyboard;
import com.csci6461.team13.simulator.core.io.Printer;
import com.csci6461.team13.simulator.ui.basic.CacheRow;
import com.csci6461.team13.simulator.ui.basic.Program;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.ui.helpers.MainPanelHelper;
import com.csci6461.team13.simulator.util.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class MainPanelController {

    private static String REGISTER_PROPERTY_NAME = "regName";
    private MemControlController memControlController = null;
    private MainPanelHelper helper = null;
    private static final FontAwesomeIconView runGraphic;
    private static final FontAwesomeIconView debugGraphic;

    static {

        runGraphic = new FontAwesomeIconView
                (FontAwesomeIcon.FAST_FORWARD, "18.0");
        debugGraphic = new FontAwesomeIconView
                (FontAwesomeIcon.STEP_FORWARD, "18.0");

        runGraphic.setFill(Paint.valueOf("WHITE"));
        debugGraphic.setFill(Paint.valueOf("WHITE"));
    }

    /**
     * signals - states of different parts of simulator
     */
    private SimpleStringProperty modeText = new SimpleStringProperty();

    private Signals signals;

    @FXML
    private HBox mOverview;

    @FXML
    private TextArea mKBBuffer;

    @FXML
    private TextField mKeyboard;

    @FXML
    private Button mKBSubmit;

    @FXML
    private TextArea mConsolePrinter;

    @FXML
    private TableView<CacheRow> mCache;

    @FXML
    private TableColumn<CacheRow, String> mCacheAddr;

    @FXML
    private TableColumn<CacheRow, String> mCacheData;

    @FXML
    private HBox mMem;

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
    private Button mExport;
    @FXML
    private Button mCardReader;

    // utility methods
    private static void refreshText(Label label, IntSupplier function) {
        label.setText(Integer.toString(function.getAsInt()));
    }

    private static void refreshText(TextField textField, IntSupplier function) {
        textField.setText(Integer.toString(function.getAsInt()));
    }

    private static void addRegListener(TextField textField, IntConsumer consumer) {
        textField.textProperty().addListener((observable, oldValue, newValue)
                -> consumer.accept(Integer.valueOf(newValue)));
    }

    private static void putRegProperty(TextField textField, Register register) {
        textField.getProperties().put(REGISTER_PROPERTY_NAME, register.name());
    }

    @FXML
    void initialize() {
        helper = new MainPanelHelper();
        this.signals = Simulator.getSignals();

        // init menu button bindings
        mIpl.disableProperty().bind(signals.on);
        mOverview.disableProperty().bind(signals.on.not());

        StringBinding modeText = Bindings.createStringBinding(() ->
                signals.mode.get() ? "RUN" : "DEBUG", signals.mode);
        Binding<FontAwesomeIconView> modeGraphicBinding = Bindings
                .createObjectBinding(this::getModeIcon, signals.mode);
        mMode.textProperty().bind(modeText);
        mMode.graphicProperty().bind(modeGraphicBinding);
        mMode.disableProperty().bind(signals.loaded.not());

        mLoad.disableProperty().bind(signals.loaded.or(signals.on.not()));
        mReset.disableProperty().bind(signals.on.not());

        mStart.disableProperty().bind(signals.loaded.not().or(signals.started
                .and(signals.mode.not())));
        mNext.disableProperty().bind(signals.mode.or(signals.started.not()));

        // init io bindings
        List<Device> devices = Simulator.getCpu().getDevices();
        Keyboard keyboard = (Keyboard) CoreUtil.findDevice(devices, Const
                .DEVICE_ID_KEYBOARD);
        Printer printer = (Printer) CoreUtil.findDevice(devices, Const
                .DEVICE_ID_PRINTER);
        if (keyboard != null) {
            mKBBuffer.textProperty().bindBidirectional(keyboard.bufferProperty());
            mKBSubmit.disableProperty().bind(keyboard.waitingForInput.not());
            mKBSubmit.setOnAction(event -> {
                // when keyboard value changes, append new value to keyboard buffer
                mKBBuffer.appendText(mKeyboard.getText());
                mKeyboard.clear();
            });
        }
        if (printer != null) {
            mConsolePrinter.textProperty().bindBidirectional(printer.textProperty());
        }

        // init other bindings
        mHistory.textProperty().bindBidirectional(helper.consoleOutput);
        mExec.textProperty().bind(helper.exec);

        initCacheTable();
        addMemControlPanel();
        initRegiseterProperties();
        initRegisterListeners();
        refreshRegisters(Simulator.getCpu().getRegisters());
    }

    private FontAwesomeIconView getModeIcon() {
        return signals.mode.get() ? runGraphic: debugGraphic;
    }

    private void initCacheTable() {
        mCacheAddr.setCellValueFactory(cellData -> cellData.getValue().addrProperty());
        mCacheData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        mCache.setItems(helper.getCache());
    }

    // Menu Buttons Handlers
    @FXML
    void iplHandler(MouseEvent event) {
        // power on
        signals.on.set(true);
        updateHistory("Computer Initialized");
        refreshSimulator();
    }

    @FXML
    void loadHandler(MouseEvent event) {
        // enable keyboard for program 1
        Registers registers = Simulator.getCpu().getRegisters();
        MCU mcu = Simulator.getCpu().getMcu();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Simulator.getPrimaryStage());
        if (file != null) {
            Program program = null;
            try {
                program = ProgramUtil.readBinaryProgram(file);
                if (program == null) {
                    // invalid program
                    throw new IllegalArgumentException();
                }
                Program.storeToMemory(program, mcu);
                updateHistory("New Program Loaded");
                updateHistory("Description: " + program.getDescription());
                // program loaded
                signals.loaded.set(true);
                registers.setPC(mcu.getFromCache(program.getInitAddrIndex()));
            } catch (IOException | IllegalArgumentException e) {
                updateHistory("Invalid Program");
            }

            refreshSimulator();
        }
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
        System.exit(0);
    }

    @FXML
    void nextHandler(MouseEvent event) {
        // execute one instruction under debug mode
        ExecutionResult executionResult = helper.execute(Simulator.getCpu());
        updateHistory(helper.nextWord.get(), helper.nextAddr.get(),
                executionResult.getMessage());
        if (executionResult.equals(ExecutionResult.HALT)) {
            // if there is no more instructions
            // flush started signal
            signals.started.set(false);
        } else {
            helper.fetch(Simulator.getCpu());
        }
        refreshSimulator();
    }

    @FXML
    void startHandler(MouseEvent event) {
        // setup the program started
        signals.started.set(true);

        Task task = new Task() {
            @Override
            protected Object call() {
                // run program according to different modes
                if (!signals.mode.get()) {
                    // debug mode
                    helper.fetch(Simulator.getCpu());
                } else {
                    // run mode
                    Platform.runLater(() -> {
                        ExecutionResult executionResult = ExecutionResult.CONTINUE;
                        while (executionResult.equals(ExecutionResult.CONTINUE)) {
                            helper.fetch(Simulator.getCpu());
                            executionResult = helper.execute(Simulator.getCpu());
                            updateHistory(helper.nextWord.get(), helper.nextAddr.get(),
                                    executionResult.getMessage());
                            if (executionResult.equals(ExecutionResult.RETRY)) {
                                mStart.setText("Retry");
                            } else {
                                mStart.setText("Start");
                            }
                            refreshSimulator();
                        }
                        // flush started signal
                        signals.started.set(false);
                    });
                }
                return true;
            }
        };

        Executors.newSingleThreadExecutor().execute(task);
    }

    // tools panel handlers
    @FXML
    void ieHandler(MouseEvent event) {
        toInstEdit();
    }

    @FXML
    void exportHandler(MouseEvent event) {
        ProgramUtil.exportToDesktop();
    }

    @FXML
    void cardReaderHandler(MouseEvent event) {
        List<Device> devices = Simulator.getCpu().getDevices();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Simulator.getPrimaryStage());
        if (file != null) {
            try {
                Device device = CoreUtil.findDevice(devices, Const
                        .DEVICE_ID_CARD_READER);
                if (device instanceof CardReader) {
                    ((CardReader) device).write(file);
                }
            } catch (IOException | IllegalArgumentException e) {
                updateHistory("Invalid File");
            }

            refreshSimulator();
        }
    }

    @FXML
    void registerHandler(MouseEvent event) {
        TextField register = (TextField) event.getSource();
        toRegisterEdit(register, (String) register.getProperties().get
                (REGISTER_PROPERTY_NAME));
    }

    private void addMemControlPanel() {
        try {
            FXMLLoadResult result = FXMLUtil.loadAsNode("mem_control.fxml");
            memControlController = (MemControlController) result.getController();
            mMem.getChildren().add(result.getNode());
            memControlController.setup(this);
            mMem.autosize();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void initRegisterListeners() {
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

    private void initRegiseterProperties() {
        // init register properties
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
    }

    /**
     * refresh all register value to latest
     */
    private void refreshSimulator() {
        CPU cpu = Simulator.getCpu();
        Registers registers = cpu.getRegisters();
        // refresh registers
        refreshRegisters(registers);
        // refresh cache table
        helper.refreshCache(cpu);
        // refresh mem control
        memControlController.refresh();
    }

    /**
     * flush the whole simulator to original state
     */
    private void resetSimulator() {
        // flush signals
        signals.mode.set(true);
        modeText.set("RUN");
        signals.on.set(false);
        signals.loaded.set(false);
        signals.started.set(false);

        // flush texts
        helper.exec.set("");
        helper.consoleOutput.set("");
        helper.executedInstCount = 0;
        mKeyboard.clear();
        mKBBuffer.clear();
        mConsolePrinter.clear();
        // flush mem control
        memControlController.reset();
        mStart.setText("Start");

        // flush cpu
        Simulator.getCpu().reset();
        // flush registers
        refreshSimulator();
    }

    /**
     * refresh all register value to latest
     */
    void refreshRegisters(Registers regs) {
        refreshText(mPc, regs::getPC);
        refreshText(mIr, regs::getIR);
        refreshText(mMar, regs::getMAR);
        refreshText(mMbr, regs::getMBR);
        refreshText(mMsr, regs::getMSR);
        refreshText(mCc, regs::getCC);
        refreshText(mMfr, regs::getMFR);
        refreshText(mR0, regs::getR0);
        refreshText(mR1, regs::getR1);
        refreshText(mR2, regs::getR2);
        refreshText(mR3, regs::getR3);
        refreshText(mX1, regs::getX1);
        refreshText(mX2, regs::getX2);
        refreshText(mX3, regs::getX3);
    }

    private void updateHistory(int word, int addr, String msg) {
        String instStr = Instruction.build(word).toString();
        String line = String.format("Executed: %s(%d)\t@ [%d]\nMSG: %s\n",
                instStr, word, addr, msg);
        // update execution consoleOutput
        mHistory.appendText(line);
    }

    private void updateHistory(String line) {
        // update execution consoleOutput
        mHistory.appendText(String.format("[%s]\n", line));
    }

}
