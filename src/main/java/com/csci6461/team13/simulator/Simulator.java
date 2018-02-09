package com.csci6461.team13.simulator;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulator extends Application {

    private static CPU cpu;

    // primaryStage is the root component of the UI module
    // it will be initialized in the start method
    private static Stage primaryStage;
    private static Signals signals = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // cpu settings
        initCPU();

        // init signals
        getSignals();

        // main csci6461.team13.ui settings

        // load start scene
        FXMLLoadResult result = FXMLUtil.loadAsNode("start.fxml");
        FXMLUtil.addStylesheets(result.getNode(), "bootstrap3.css");

        primaryStage.setTitle("CSCI6461 TEAM 13");
        primaryStage.setScene(new Scene(result.getNode()));
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

    public static CPU getCpu(){
        return cpu;
    }

    private static void initCPU(){
        cpu = new CPU();
    }

    public static Signals getSignals(){
        if(signals == null){
            signals = new Signals();
        }

        return signals;
    }
}
