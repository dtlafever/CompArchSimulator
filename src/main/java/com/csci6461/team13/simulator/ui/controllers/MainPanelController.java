package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPanelController {

    private static Stage registerEditor = null;
    private static RegisterEditPanelController registerEditPanelController = null;

    @FXML
    void initialize() {
//        refresh();
    }

    @FXML
    private Button m_start;

    @FXML
    private Button m_step;

    @FXML
    private Button m_ipl;

    @FXML
    private Button m_reset;

    @FXML
    private Button m_load;

    @FXML
    private TextArea m_prog;

    @FXML
    private TextField m_exec;

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
    private Button m_save;

    // Registers TextFields Handlers
    @FXML
    void pcHandler(MouseEvent event) {
        createRegisterEditWindow(m_pc, "PC");
    }

    @FXML
    void irHandler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_ir, "IR");
    }

    @FXML
    void marHandler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_mar, "MAR");
    }

    @FXML
    void mbrHandler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_mbr, "MBR");
    }

    @FXML
    void msrHandler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_msr, "MSR");
    }

    @FXML
    void r0Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_r0, "R0");
    }

    @FXML
    void r1Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_r1, "R1");
    }

    @FXML
    void r2Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_r2, "R2");
    }

    @FXML
    void r3Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getIR();
        createRegisterEditWindow(m_r3, "R3");
    }


    @FXML
    void x1Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getX1();
        createRegisterEditWindow(m_x1, "X1");
    }

    @FXML
    void x2Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getX2();
        createRegisterEditWindow(m_x2, "X2");
    }

    @FXML
    void x3Handler(MouseEvent event) {
        int value = Simulator.getCpu().getRegisters().getX3();
        createRegisterEditWindow(m_x3, "X3");
    }

    // Main Buttons Handlers
    @FXML
    void saveHandler(MouseEvent event) {

    }

    /**
     * registerEditor is a Singleton object
     */
    private void createRegisterEditWindow(TextField register, String name) {

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
