package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.ui.controllers.InstEditController;
import com.csci6461.team13.simulator.ui.controllers.RegisterEditPanelController;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPanelHelper {

    // execution history property
    // if this content update, the corresponding textfield in main panel would also update
    public SimpleStringProperty history = new SimpleStringProperty("");
    public SimpleStringProperty exec = new SimpleStringProperty("");
    public int historyLength = 0;

    private InstEditController instEditController = null;
    private Stage instEditor = null;

    private Stage registerEditor = null;
    private RegisterEditPanelController registerEditPanelController = null;

    /**
     * execute a single instruction
     * <p>
     * this should be executed after fetch method
     */
    public boolean execute(CPU cpu) {
        int inst = Integer.valueOf(exec.get());
        String instStr = Instruction.build(inst).toString();
        // execution
        boolean hasNext = cpu.decodeAndExecute(inst);
        historyLength++;
        // update execution history
        history.set(historyLength + ": \t[" + instStr + "]\n" + history.get());
        return hasNext;
    }

    public FXMLLoadResult getInstEditor(Stage owner, Modality modality) {
        if (instEditor == null) {
            this.instEditor = new Stage();
            try {
                FXMLLoadResult result;
                result = FXMLUtil.loadAsNode("inst_edit.fxml");
                this.instEditController = (InstEditController)
                        result.getController();
                FXMLUtil.addStylesheets(result.getNode(), "bootstrap3.css");
                this.instEditor.setScene(new Scene(result.getNode()));
                this.instEditor.setResizable(false);
                this.instEditor.setTitle("Instruction Editor");
                this.instEditor.initModality(modality);
                this.instEditor.initOwner(owner);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FXMLLoadResult<InstEditController> result = new FXMLLoadResult<>();
        result.setStage(this.instEditor);
        result.setController(this.instEditController);
        this.instEditController.reset();
        return result;
    }

    public FXMLLoadResult getRegisterEditor(TextField register, String name) {
        if (this.registerEditor == null) {
            this.registerEditor = new Stage();
            try {
                FXMLLoadResult result;
                result = FXMLUtil.loadAsNode("register_edit.fxml");
                this.registerEditPanelController = (RegisterEditPanelController)
                        result.getController();
                FXMLUtil.addStylesheets(result.getNode(), "bootstrap3.css");
                this.registerEditor.setScene(new Scene(result.getNode()));
                this.registerEditor.setResizable(false);
                this.registerEditor.setTitle("Register Editor");
                this.registerEditor.initModality(Modality.WINDOW_MODAL);
                this.registerEditor.initOwner(Simulator.getPrimaryStage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String originalValue = register.getText();
        FXMLLoadResult<RegisterEditPanelController> result = new
                FXMLLoadResult<>();
        result.setStage(this.registerEditor);
        result.setController(this.registerEditPanelController);
        this.registerEditPanelController.reset(name, originalValue);
        return result;
    }

    /**
     * execute fetch operation in CPU
     * it will fetch a instruction from current PC
     */
    public void fetch(CPU cpu) {
        int word = cpu.fetch();
        exec.set(String.valueOf(word));
    }
}
