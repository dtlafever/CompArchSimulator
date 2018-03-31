package com.csci6461.team13.simulator;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.io.CardReader;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.core.io.Keyboard;
import com.csci6461.team13.simulator.core.io.Printer;
import com.csci6461.team13.simulator.ui.basic.Signals;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author zhiyuan
 */
public class Simulator extends Application {

    private static CPU cpu;

    /**
     * primaryStage is the root component of the UI module
     * it will be initialized in the start method
     */
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

        // create devices
        String keyboardName = "keyboard";
        Device keyboard = new Keyboard(Const.DEVICE_ID_KEYBOARD, keyboardName);
        String cardReaderName = "card reader";
        Device cardReader = new CardReader(Const.DEVICE_ID_CARD_READER, cardReaderName);
        String printerName = "printer";
        Device printer = new Printer(Const.DEVICE_ID_PRINTER, printerName);
        cpu.getDevices().add(keyboard);
        cpu.getDevices().add(printer);
        cpu.getDevices().add(cardReader);

        // main csci6461.team13.ui settings

        // load start scene
        FXMLLoadResult result = FXMLUtil.loadAsNode("start.fxml");
        FXMLUtil.addStylesheets(result.getNode(), Const
                .UNIVERSAL_STYLESHEET_URL.get());

        Image icon = new Image(Const.ICON_URL);

        primaryStage.setTitle("CSCI6461 TEAM 13");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(result.getNode()));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();

        Simulator.primaryStage = primaryStage;
    }

    /**
     * fetch primary stage of the simulator
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static CPU getCpu() {
        return cpu;
    }

    private static void initCPU() {
        cpu = new CPU();
    }

    public static Signals getSignals() {
        if (signals == null) {
            signals = new Signals();
        }

        return signals;
    }
}
