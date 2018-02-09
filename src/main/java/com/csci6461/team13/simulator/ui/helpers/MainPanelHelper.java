package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Instruction;
import javafx.beans.property.SimpleStringProperty;

public class MainPanelHelper {

    // execution history property
    // if this content update, the corresponding textfield in main panel would also update
    public SimpleStringProperty history = new SimpleStringProperty();

    /**
     * execute a single instruction
     */
    public boolean execute(CPU cpu, Instruction instruction) {
        // execution
        return true;
    }
}
