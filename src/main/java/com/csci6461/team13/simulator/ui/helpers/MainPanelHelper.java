package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import javafx.beans.property.SimpleStringProperty;

public class MainPanelHelper {

    // execution history property
    // if this content update, the corresponding textfield in main panel would also update
    public SimpleStringProperty history = new SimpleStringProperty("");
    public SimpleStringProperty exec = new SimpleStringProperty("");
    public int historyLength = 0;

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
        history.set(historyLength + ": \t" + instStr + "\n" + history.get());
        return hasNext;
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
