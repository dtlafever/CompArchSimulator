package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.ui.helpers.MainPanelHelper;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPanelController {

    private Stage registerEditor = null;
    private RegisterEditPanelController registerEditPanelController = null;

    // signals - states of different parts of simulator
    private SimpleStringProperty modeText = new SimpleStringProperty("RUN");
    private SimpleBooleanProperty mode = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty on = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty loaded = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty started = new SimpleBooleanProperty(false);

    @FXML
    void initialize() {
        m_ipl.disableProperty().bind(on);
        m_overview.disableProperty().bind(on.not());
        m_regs.disableProperty().bind(on.not());
        m_mem.disableProperty().bind(on.not());

        m_mode.textProperty().bind(modeText);
        m_mode.disableProperty().bind(loaded.not().or(started));
        m_load.disableProperty().bind(on.not().or(started));
        m_reset.disableProperty().bind(on.not().or(started));

        m_start.disableProperty().bind(loaded.not().or(started));
        m_next.disableProperty().bind(mode.or(started.not()));
    }

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
    private TextArea m_prog;

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

    // Menu Buttons Handlers
    @FXML
    void iplHandler(MouseEvent event) {
        // todo run ipl code

        // power on
        on.set(true);
    }

    @FXML
    void loadHandler(MouseEvent event) {
        // todo load program

        // program loaded
        loaded.set(true);
    }

    @FXML
    void modeHandler(MouseEvent event) {
        // toggle
        mode.set(!mode.get());
        modeText.set(mode.get() ? "RUN" : "DEBUG");
    }

    @FXML
    void resetHandler(MouseEvent event) {
        mode.set(true);
        on.set(false);
        loaded.set(false);
    }

    @FXML
    void nextHandler(MouseEvent event) {
        // execute one instruction under debug mode

        // reset started signal
        started.set(started.not().get());
    }

    @FXML
    void startHandler(MouseEvent event) {
        // set the program started
        started.set(true);

        // run program according to different modes
        if (mode.get()) {
            // run mode
            MainPanelHelper.executeAll(null);
            // reset started signal
            started.set(started.not().get());
        } else {
            // debug mode
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
        register.textProperty().bind(registerEditPanelController.valueProperty);
        registerEditPanelController.reset(name, originalValue);
        registerEditor.showAndWait();
        register.textProperty().unbind();
    }
}
