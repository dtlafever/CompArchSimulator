package csci6461.team13.ui.controllers;

import csci6461.team13.Simulator;
import csci6461.team13.util.FxmlUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StartPanelController {

    @FXML
    Button startBtn;

    /**
     * automatically executed by application after loading
     */
    @FXML
    void initialize(){

    }

    /**
     * act to clicks on startBtn
     *
     * switch scene to main scene
     * */
    @FXML
    void handleStartBtn(MouseEvent event){
        System.out.println("Simulation Started");
        startBtn.setDisable(true);
        try {
            // load the main scene, then current scene will dismiss
            Parent root = FxmlUtil.loadAsNode("main.fxml");
            FxmlUtil.addStylesheets(root, "bootstrap3.css");
            Simulator.getPrimaryStage().setResizable(true);
            Simulator.getPrimaryStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
