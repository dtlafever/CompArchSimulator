package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.core.CPU;
import javafx.beans.property.SimpleStringProperty;

public class MainPanelHelper {

    // execution history property
    // if this content update, the corresponding textfield in main panel would also update
    public SimpleStringProperty history = new SimpleStringProperty("");
    public SimpleStringProperty exec = new SimpleStringProperty("");

    /**
     * execute a single instruction
     */
    public boolean execute(CPU cpu) {
        // execution

        // update execution history
        history.set(history.get()+"\n"+"hello");
        return true;
    }

    public boolean fetch(CPU cpu){
        cpu.fetch();
        exec.set(Integer.toBinaryString(cpu.getRegisters().getMBR()));
        return true;
    }

}
