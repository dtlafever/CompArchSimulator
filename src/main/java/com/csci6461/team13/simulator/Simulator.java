package com.csci6461.team13.simulator;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.util.FxmlUtil;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulator extends Application {

    CPU cpu;

    // primaryStage is the root component of the UI module
    // it will be initialized in the start method
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // cpu settings
        // TODO: 1/27/18 I'm thinking about running the cpu in a separate thread, so that UI and CPU will be communicating across the threads
        cpu = new CPU();

        // main csci6461.team13.ui settings

        // load start scene
        Parent root = FxmlUtil.loadAsNode("start.fxml");
        FxmlUtil.addStylesheets(root, "bootstrap3.css");

        primaryStage.setTitle("CSCI6461 TEAM 13");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        Simulator.primaryStage = primaryStage;
    }

    /**
     * fetch primary stage of the simulator
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
