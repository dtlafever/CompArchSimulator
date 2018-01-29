package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.util.FxmlUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPanelController {

    @FXML
    Button startBtn;

    /**
     * automatically executed by application after loading
     */
    @FXML
    void initialize() {

    }

    /**
     * act to clicks on startBtn
     * <p>
     * switch scene to main scene
     */
    @FXML
    void handleStartBtn(MouseEvent event) {
        System.out.println("Simulation Started");

        Stage primaryStage = Simulator.getPrimaryStage();
        startBtn.setDisable(true);
        try {
            // load the main scene, then current scene will dismiss
            Parent root = FxmlUtil.loadAsNode("main.fxml");
            FxmlUtil.addStylesheets(root, "bootstrap3.css");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
