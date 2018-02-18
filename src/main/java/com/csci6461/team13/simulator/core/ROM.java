package com.csci6461.team13.simulator.core;

import java.util.ArrayList;

public class ROM {

    private ROM() {

    }

    private static ArrayList<String> instructions;

    static {

        instructions = new ArrayList<>();

        instructions.add("LDR 0,0,0,9");
        instructions.add("LDR 0,0,1,10");
        instructions.add("STR 1,0,0,11");
        instructions.add("STR 1,0,1,12");
        instructions.add("LDA 2,0,0,13");
        instructions.add("LDA 2,0,1,14");
        instructions.add("LDX 0,1,0,15");
        instructions.add("LDX 0,1,1,16");
        instructions.add("STX 0,2,0,17");
        instructions.add("STX 0,2,1,18");
        // halt
        instructions.add("HLT 0,0,0,0");
    }

    public static ArrayList<String> getInstructions() {
        return instructions;
    }
}
