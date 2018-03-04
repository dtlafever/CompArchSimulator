package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.ui.basic.Program;
import com.csci6461.team13.simulator.util.Const;

import java.util.ArrayList;
import java.util.List;

public class ROM {


    // use space to separate numbers
    private static final int PROGRAM_1_SEPARATOR = 32;
    private static final int PROGRAM_1_MAX = 20;
    private static final int PROGRAM_1_INIT = 40;
    private static final int PROGRAM_1_LOOP = 60;
    private static final int PROGRAM_1_READER = 70;
    private static final int PROGRAM_1_ASSEMBLER = 90;
    private static final int PROGRAM_1_COMPARE = 110;
    private static final int PROGRAM_1_STORAGE = 150;

    private ROM() {
    }

    private static List<Program> programs;

    static {

        programs = new ArrayList<>();

        Program one = new Program();

        // max count
        one.putInitData(30, PROGRAM_1_MAX);
        // ' '
        one.putInitData(17, PROGRAM_1_SEPARATOR);
        // loop reader start
        one.putInitData(18, PROGRAM_1_LOOP);
        // reader start
        one.putInitData(19, PROGRAM_1_READER);
        // word assembler start
        one.putInitData(27, PROGRAM_1_ASSEMBLER);
        // comparer start
        one.putInitData(25, PROGRAM_1_COMPARE);
        // number storage start
        one.putInitData(26, PROGRAM_1_STORAGE);
        // number count

        List<String> init = new ArrayList<>();
        List<String> loop = new ArrayList<>();
        List<String> reader = new ArrayList<>();
        List<String> assembler = new ArrayList<>();

        one.putInstructionList(PROGRAM_1_INIT, init);
        one.putInstructionList(PROGRAM_1_LOOP, loop);
        one.putInstructionList(PROGRAM_1_READER, reader);
        one.putInstructionList(PROGRAM_1_ASSEMBLER, assembler);

        // jump to reader
        init.add("JMA 0,0,1,19");

        // loop
        // load current input number index
        loop.add("LDR 0,0,0,12");
        // check if input number index == Max
        loop.add("SMR 0,0,0,30");
        // not zero, jump to reader
        loop.add("JNE 0,0,1,19");
        // count reached max
        loop.add("LDA 0,0,0,12");
        loop.add("OUT 0,0,0,1");

        // reader, read a complete number
        reader.add("IN 0,0,0,0");
        reader.add("OUT 0,0,0,1");
        // store char to EA
        reader.add("STR 0,0,0,10");
        // subtract separator, a constant
        reader.add("SMR 0,0,0,17");
        // if it's a valid char, to assembler
        // else, return to loop start
        reader.add("JNE 0,0,1,27");
        // decrease input number index by 1
        // then return to loop start
        // load input count
        reader.add("LDR 0,0,0,12");
        // increase count by 1
        reader.add("AIR 0,0,0,1");
        // store updated input count
        reader.add("STR 0,0,0,12");
        reader.add("JMA 0,0,1,18");

        // word assembler
        // get input number index
        assembler.add("LDR 0,0,0,12");
        // get storage index = input count + start
        assembler.add("AMR 0,0,0,26");
        // store number storage index
        assembler.add("STR 0,0,0,11");
        // get word
        assembler.add("LDR 0,0,1,11");
        // assemble, the char to add is stored in 10
        assembler.add("AMR 0,0,0,10");
        // store the assembled word
        assembler.add("STR 0,0,1,11");
        // back to reader
        assembler.add("JMA 0,0,1,19");

        programs.add(one);
    }

    public static List<Program> getPrograms() {
        if (programs == null) {
            return new ArrayList<>();
        }
        return programs;
    }
}
