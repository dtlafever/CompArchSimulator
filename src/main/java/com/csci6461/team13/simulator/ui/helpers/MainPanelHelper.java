package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.Simulator;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Cache;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.ui.basic.CacheRow;
import com.csci6461.team13.simulator.ui.controllers.InstEditController;
import com.csci6461.team13.simulator.ui.controllers.RegisterEditPanelController;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.FXMLLoadResult;
import com.csci6461.team13.simulator.util.FXMLUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * @author zhiyuan
 * */
public class MainPanelHelper {

    // console output history
    public StringProperty consoleOutput = new SimpleStringProperty("");
    public StringProperty exec = new SimpleStringProperty();
    public int executedInstCount = 0;
    public IntegerProperty nextWord = new SimpleIntegerProperty();
    public IntegerProperty nextAddr = new SimpleIntegerProperty();
    private boolean hasUnfinishedCycle = false;

    private InstEditController instEditController = null;
    private Stage instEditor = null;

    private Stage registerEditor = null;
    private RegisterEditPanelController registerEditPanelController = null;

    private ObservableList<CacheRow> cacheRows = FXCollections.observableArrayList();

    public ObservableList<CacheRow> getCache() {
        return cacheRows;
    }

    /**
     * execute fetch operation in CPU
     * it will fetch a instruction from current PC
     */
    public void fetch(CPU cpu) {
        if(hasUnfinishedCycle){
            // skip fetch operation if the previous cycle is unfinished
            return;
        }
        int word = cpu.fetch();
        int addr = cpu.getRegisters().getMAR();
        Instruction instruction = Instruction.build(word);
        exec.set(String.format("%s(%d) @ [%d]", instruction.toString(), word, addr));
        nextWord.set(word);
        nextAddr.set(addr);
        // unfinished cycle
        hasUnfinishedCycle = true;
    }

    /**
     * execute a single instruction
     * <p>
     * this should be executed after fetch method
     */
    public ExecutionResult execute(CPU cpu) {
        // execution
        ExecutionResult executionResult = cpu.decodeAndExecute();
        executedInstCount++;
        exec.set("");
        // finishing current execution cycle
        hasUnfinishedCycle = false;
        return executionResult;
    }

    public void refreshCache(CPU cpu){
        MCU mcu = cpu.getMcu();
        Cache cache = mcu.getCache();
        List<Cache.CacheLine> cacheLines = cache.getCacheLines();
        cacheRows.clear();
        for (Cache.CacheLine cacheLine : cacheLines) {
            String addr = String.valueOf(cacheLine.getAddr());
            String data = String.valueOf(cacheLine.getData());
            cacheRows.add(new CacheRow(addr, data));
        }
    }

    public FXMLLoadResult getInstEditor(Stage owner, Modality modality) {
        if (instEditor == null) {
            this.instEditor = new Stage();
            try {
                FXMLLoadResult result;
                result = FXMLUtil.loadAsNode("inst_edit.fxml");
                this.instEditController = (InstEditController)
                        result.getController();
                FXMLUtil.addStylesheets(result.getNode(), Const
                        .UNIVERSAL_STYLESHEET_URL.get());
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
                FXMLUtil.addStylesheets(result.getNode(), Const
                        .UNIVERSAL_STYLESHEET_URL.get());
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
        FXMLLoadResult<RegisterEditPanelController> result = new FXMLLoadResult<>();
        result.setStage(this.registerEditor);
        result.setController(this.registerEditPanelController);
        this.registerEditPanelController.reset(name, originalValue);
        return result;
    }
}
