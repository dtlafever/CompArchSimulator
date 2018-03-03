package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class ROM {
    private ROM() {
    }

    private static List<String> insts;

    static {
        insts = new ArrayList<>();
        // non-stop reading all buffered chars
        insts.add("IN 1,0,0,0");
        insts.add("OUT 1,0,0,1");
        insts.add("JMA 0,0,0,10");
    }

    public static List<Instruction> getInstructions() {
        if (insts == null) {
            return new ArrayList<>();
        }
        List<Instruction> instructions = new ArrayList<>();
        for (String inst : insts) {
            instructions.add(Instruction.build(inst));
        }
        return instructions;
    }
}
