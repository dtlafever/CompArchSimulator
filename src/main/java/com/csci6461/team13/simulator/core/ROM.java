package com.csci6461.team13.simulator.core;

import java.util.ArrayList;

public class ROM {

    private ROM(){

    }

    private static ArrayList<Integer> instructions;

    static {

        instructions = new ArrayList<>();

        //[1033]LDR 0,0,0,9
        instructions.add(1033);
        //[1066]LDR 0,0,1,10(42)
        instructions.add(1066);
        //[2059]STR 1,0,0,11
        instructions.add(2059);
        //[2092]STR 1,0,1,12(44)
        instructions.add(2092);
        //[3085]LDA 2,0,0,13
        instructions.add(3085);
        //[3118]LDA 2,0,1,14(46)
        instructions.add(3118);
        //[41999]LDX 1,0,15
        instructions.add(41999);
        //[42032]LDX 1,1,16(48)
        instructions.add(42032);
        //[43025]STX 2,0,17
        instructions.add(43025);
        //[43058]STX 2,1,18(50)
        instructions.add(43058);
        // halt
        instructions.add(0);
    }


    public static ArrayList<Integer> getInstructions() {
        return instructions;
    }
}
