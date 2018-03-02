package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.Instruction;

import java.util.ArrayList;

public class ROM {

    private ROM() {

    }

    private static ArrayList<Instruction> instructions;

    static {

        instructions = new ArrayList<>();
        instructions.add(Instruction.build("IN 1,0,0,0"));
        instructions.add(Instruction.build("OUT 1,0,0,1"));
    }

    public static ArrayList<Instruction> getInstructions() {
        return instructions;
    }
}
