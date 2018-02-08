package com.csci6461.team13.simulator.ui.helpers;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.util.Program;
import java.util.ArrayList;

public class MainPanelHelper {

    /**
     * execute a single instruction
     */
    public boolean execute(CPU cpu, Object instruction) {
        // execution
        return true;
    }

    /**
     * execute multiple instructions
     */
    public boolean executeAll(CPU cpu, ArrayList<Object> instructions) {
        for (Object instruction : instructions) {
            execute(cpu, instruction);
        }
        return true;
    }

    public Program loadProgram() {
        return null;
    }
}
