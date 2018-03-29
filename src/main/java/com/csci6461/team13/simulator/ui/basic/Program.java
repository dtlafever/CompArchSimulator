package com.csci6461.team13.simulator.ui.basic;

import com.csci6461.team13.simulator.ROM;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.CoreUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhiyuan
 */
public class Program {

    private static String INIT_DATA_REGEX = "\\{INIT_DATA:(\\(\\[\\w+=\\d+\\]\\[\\w+=\\d+\\]\\))*\\}";
    private static String PART_REGEX = "(\\{(\\[PART=\\d+\\]){1}:(\\[INST=\\d+\\])*\\})";
    private static String DESCRIPTION_REGEX = "";
    private static String PROGRAM_REGEX = String.format
            ("^\\{PROGRAM\\}{1}%s{1}%s*", INIT_DATA_REGEX, PART_REGEX);

    /**
     * program start address
     */
    private Integer initAddr;
    /**
     * initial data
     */
    private Map<Integer, Integer> initialData;
    /**
     * instructions
     */
    private Map<Integer, List<String>> insts;
    /**
     * description of the program
     */
    private String description;

    public Program() {
        initialData = new HashMap<>();
        insts = new HashMap<>();
        initAddr = null;
        description = "";
    }

    public Map<Integer, Integer> getInitialData() {
        return initialData;
    }

    public Map<Integer, List<String>> getInsts() {
        return insts;
    }

    public void putInstructionList(Integer key, List<String> list) {
        insts.put(key, list);
    }

    public void putInitData(Integer addr, Integer data) {
        initialData.put(addr, data);
    }

    public Integer getInitAddr() {
        return initAddr;
    }

    public void setInitAddr(Integer initAddr) {
        this.initAddr = initAddr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * store a program into mcu
     *
     * @return the program init address
     */
    public static int storeToMemory(Program program, MCU mcu) {
        Map<Integer, Integer> initData = program.getInitialData();
        Map<Integer, List<String>> instLists = program.getInsts();

        for (Integer key : initData.keySet()) {
            mcu.storeWord(key, initData.get(key));
        }

        for (Integer key : instLists.keySet()) {
            List<String> instructions = instLists.get(key);
            int index = key;
            for (String instStr : instructions) {
                Instruction instruction = Instruction.build(instStr);
                Objects.requireNonNull(instruction, "Invalid " +
                        "Instruction:" + instStr);
                mcu.storeWord(index, instruction.toWord());
                index++;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        Map<Integer, Integer> initData = this.getInitialData();
        Map<Integer, List<String>> instLists = this.getInsts();
        StringBuilder builder = new StringBuilder();
        builder.append("{PROGRAM}");
        builder.append("{INIT_DATA:");
        for (Integer key : initData.keySet()) {
            builder.append(String.format("([ADDR=%d][DATA=%d])",
                    key, initData.get(key)));
        }
        builder.append("}");

        Set<Integer> keys = instLists.keySet();
        int i = 0;
        for (Integer key : keys) {
            List<String> instList = instLists.get(key);
            builder.append(String.format("{[PART=%d]:", i));
            for (String instStr : instList) {
                Instruction instruction = Instruction.build(instStr);
                if (instruction != null) {
                    Integer word = instruction.toWord();
                    builder.append(String.format("[INST=%d]", word));
                } else {
                    throw new IllegalArgumentException("Invalid Instruction:"
                            + instStr);
                }
            }
            builder.append("}");
            i++;
        }

        return builder.toString();
    }

    public static Program fromBinaryString(String binaryString) {
        Program program = null;
        String programString = CoreUtil.fromFixedLenBinStr(binaryString, 16);
        Pattern pattern = Pattern.compile(PROGRAM_REGEX);
        Matcher matcher = pattern.matcher(programString);
        if (matcher.matches()) {
            program = new Program();
            // extract init address
            // extract init data
            // extract insts
            // extract description
        } else {
            // not a valid program
            throw new IllegalArgumentException("Invalid Program:" + programString);
        }
        return program;
    }

    public String toBinaryString() {
        return CoreUtil.toFixedLenBinStr(this.toString(), 16);
    }

    public static void main(String[] args) {
        Program program = ROM.getPrograms().get(0);
        System.out.println(program.toString());
        String string = program.toBinaryString();
        System.out.println(string);
        Program program1 = Program.fromBinaryString(string);
        System.out.println(program1);
    }
}
