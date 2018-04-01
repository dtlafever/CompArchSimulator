package com.csci6461.team13.simulator.ui.controllers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author zhiyuan
 */
public class StartPanelController {

    private BooleanProperty isDracula = new SimpleBooleanProperty(false);
    private BooleanProperty isBootstrap = new SimpleBooleanProperty(true);

    @FXML
    private Button sStart;

    @FXML
    private RadioButton stBootstrap;

    @FXML
    private RadioButton stDracula;

    /**
     * automatically executed by application after loading
     */
    @FXML
    void initialize() {

        stDracula.getProperties().put("theme", Const.DRACULA_THEME_URL);
        stBootstrap.getProperties().put("theme", Const.BOOTSTRAP3_THEME_URL);

        stBootstrap.selectedProperty().bindBidirectional(isBootstrap);
        stDracula.selectedProperty().bindBidirectional(isDracula);

        stBootstrap.selectedProperty().addListener((observable, oldValue,
                                                    newValue) ->
                isDracula.set(!newValue));

        stDracula.selectedProperty().addListener((observable, oldValue,
                                                  newValue) ->
                isBootstrap.set(!newValue)
        );

        Const.UNIVERSAL_STYLESHEET_URL = Bindings.createStringBinding(() ->
                isBootstrap.get() ? Const.BOOTSTRAP3_THEME_URL : Const
                        .DRACULA_THEME_URL, isBootstrap);
        isBootstrap.addListener(observable -> FXMLUtil.replaceTheme(Simulator.getPrimaryStage().getScene().getRoot(), Const.UNIVERSAL_STYLESHEET_URL.get()));
    }

    /**
     * act to clicks on sStart
     * <p>
     * switch scene to main scene
     */
    @FXML
    void handleStartBtn(MouseEvent event) {
        System.out.println("Simulation Started");

        Stage primaryStage = Simulator.getPrimaryStage();
        sStart.setDisable(true);
        try {
            // load the main scene, then current scene will dismiss
            FXMLLoadResult result = FXMLUtil.loadAsNode("main.fxml");
            FXMLUtil.addStylesheets(result.getNode(), Const
                    .UNIVERSAL_STYLESHEET_URL.get());
            primaryStage.setScene(new Scene(result.getNode()));
            primaryStage.setResizable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
