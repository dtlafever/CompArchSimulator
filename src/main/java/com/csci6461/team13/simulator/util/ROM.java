package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.ui.basic.Program;

import java.util.ArrayList;
import java.util.List;

public class ROM {

    // use space to separate numbers
    private static final int PROGRAM_1_SEPARATOR = 32;
    private static final int PROGRAM_1_MAX = 20;
    // module start addresses


    // char preset
    private static final int CHAR_0 = 48;
    private static final int CHAR_EQUAL = 61;
    public static Program one = new Program();

    private ROM() {
    }

    static {

        one.setDescription("Read 21 numbers from keyboard, compare the last " +
                "one with previous 20 numbers, print the number closest the " +
                "value of the last number. Input numbers are separated with " +
                "one ' '(space)");

        // max count
        one.putInitData(30, PROGRAM_1_MAX);
        // ' '
        one.putInitData(17, PROGRAM_1_SEPARATOR);
        one.putInitData(7, CHAR_0);
        one.putInitData(8, CHAR_EQUAL);
        // number storage start
        one.putInitData(26, 500);
        // 11 number storage index
        // 12 input count
        // 13 return address for reader
        // 14 a single number
        // 15 current closest
        // 16 difference

        List<String> init = new ArrayList<>();
        List<String> loop = new ArrayList<>();
        List<String> reader = new ArrayList<>();
        List<String> assembler = new ArrayList<>();
        List<String> comparator = new ArrayList<>();
        List<String> replace = new ArrayList<>();
        List<String> printer = new ArrayList<>();

        one.putInstructionList(9, init);
        one.putInstructionList(18, loop);
        one.putInstructionList(19, reader);
        one.putInstructionList(27, assembler);
        one.putInstructionList(25, comparator);
        one.putInstructionList(29, replace);
        one.putInstructionList(28, printer);

        // set return address of reader to loop start
        init.add("LDR 0,0,0,18");
        init.add("STR 0,0,0,13");
        // add one more to max
        init.add("LDR 0,0,0,30");
        init.add("AIR 0,0,0,1");
        init.add("STR 0,0,0,30");
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
        // put last one into 14
        loop.add("LDR 0,0,1,11");
        loop.add("STR 0,0,0,14");
        // decrease number storage index by one
        loop.add("LDR 0,0,0,11");
        loop.add("SIR 0,0,0,1");
        loop.add("STR 0,0,0,11");
        // jump to replace
        loop.add("JMA 0,0,1,29");

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
        // return to stored address
        reader.add("JMA 0,0,1,13");

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
        // multiply by 10
        assembler.add("LDA 2,0,0,10");
        // rx = 0, ry = 2
        assembler.add("MLT 0,2,0,10");
        assembler.add("AMR 1,0,0,10");
        // subtract 48
        assembler.add("SMR 1,0,0,7");

        // store the assembled word
        assembler.add("STR 1,0,1,11");
        // back to reader
        assembler.add("JMA 0,0,1,19");

        //comparator
        // load current input number index
        comparator.add("LDR 0,0,0,11");
        // check if number storage index == Max
        comparator.add("SMR 0,0,0,26");
        // if zero, jump to printer
        comparator.add("JZ 0,0,1,28");
        // decrease number storage index by one
        comparator.add("LDR 0,0,0,11");
        comparator.add("SIR 0,0,0,1");
        comparator.add("STR 0,0,0,11");
        // get next number
        comparator.add("LDR 0,0,1,11");
        // get abs val of next
        comparator.add("SMR 0,0,0,14");
        comparator.add("ABS 0,0,0,0");
        // subtract the number to compare with
        comparator.add("SMR 0,0,0,16");
        // if greater or equal, do not replace
        comparator.add("JGE 0,0,1,25");
        // back to comparator start
        comparator.add("JMA 0,0,1,29");

        // replace
        // get next number
        replace.add("LDR 0,0,1,11");
        // store into 15
        replace.add("STR 0,0,0,15");
        // get the difference
        replace.add("SMR 0,0,0,14");
        replace.add("ABS 0,0,0,0");
        // store the difference
        replace.add("STR 0,0,0,16");
        // go to comparator
        replace.add("JMA 0,0,1,25");

        // printer
        printer.add("LDR 0,0,0,8");
        printer.add("OUT 0,0,0,1");
        printer.add("LDR 0,0,0,15");
        // print the final number as integer
        printer.add("OUT 0,0,1,1");
        printer.add("HLT 0,0,0,0");

    }
}
