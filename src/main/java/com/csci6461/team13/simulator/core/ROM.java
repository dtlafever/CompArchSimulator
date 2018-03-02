package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class ROM {

    private ROM() {

    }

    private static ArrayList<Instruction> instructions;

    static {

        instructions = new ArrayList<>();
        List<String> insts = new ArrayList<>();
        for (String inst : insts) {
            instructions.add(Instruction.build(inst));
        }
        insts.add("IN 1,0,0,0");
        insts.add("OUT 1,0,0,1");

        // loop until 20
    }

    public static ArrayList<Instruction> getInstructions() {
        return instructions;
    }
}
