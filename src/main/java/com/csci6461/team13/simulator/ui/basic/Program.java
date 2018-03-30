package com.csci6461.team13.simulator.ui.basic;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhiyuan
 */
public class Program {

    private static final byte BIT_LENGTH = Const.CPU_BIT_LENGTH;

    private static final String DESCRIPTION_REGEX =
            "\\{DESC:\\[.*\\]END-DESC\\}";
    private static final String INIT_ADDR_REGEX = "\\{INIT_ADDR=\\d+\\}";
    private static final String INIT_DATA_INNER_REGEX = "\\[\\d+\\|\\d+\\]";
    private static final String INIT_DATA_REGEX = String.format
            ("\\{INIT_DATA:(%s)*\\}",
                    INIT_DATA_INNER_REGEX);
    private static final String INST_REGEX = "\\[\\d+\\]";
    private static final String PART_ADDR_REGEX = "\\[ADDR=\\d+\\]";

    private static final String PART_REGEX = String.format
            ("\\{%s:(%s)*\\}",
                    PART_ADDR_REGEX,
                    INST_REGEX);
    private static final String PROGRAM_REGEX = String.format
            ("\\{PROGRAM\\}%s%s(%s){1}(%s)*",
                    DESCRIPTION_REGEX,
                    INIT_ADDR_REGEX,
                    INIT_DATA_REGEX,
                    PART_REGEX);

    /**
     * program start address
     */
    private Integer initAddrIndex;
    /**
     * initial data
     * <p>
     * format: [address, data]
     */
    private Map<Integer, Integer> initialData;
    /**
     * instructions
     * <p>
     * format: [part_init_address_storage_address, instructions]
     */
    private Map<Integer, List<String>> insts;
    /**
     * description of the program
     */
    private String description;

    public Program() {
        initialData = new LinkedHashMap<>();
        insts = new LinkedHashMap<>();
        initAddrIndex = null;
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

    public Integer getInitAddrIndex() {
        return initAddrIndex;
    }

    public void setInitAddrIndex(Integer initAddrIndex) {
        this.initAddrIndex = initAddrIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * store a program into mcu
     */
    public static void storeToMemory(Program program, MCU mcu) {
        Map<Integer, Integer> initData = program.getInitialData();
        Map<Integer, List<String>> instLists = program.getInsts();

        int curAddr = Const.PROGRAM_STORAGE_ADDR;
        for (Integer key : instLists.keySet()) {
            List<String> instructions = instLists.get(key);
            // store part start address
            initData.put(key, curAddr);
            for (String instStr : instructions) {
                Instruction instruction = Instruction.build(instStr);
                Objects.requireNonNull(instruction, "Invalid " +
                        "Instruction:" + instStr);
                mcu.storeWord(curAddr, instruction.toWord());
                curAddr++;
            }
        }

        for (Integer key : initData.keySet()) {
            mcu.storeWord(key, initData.get(key));
        }
    }

    @Override
    public String toString() {
        Map<Integer, Integer> initData = this.getInitialData();
        Map<Integer, List<String>> instLists = this.getInsts();
        StringBuilder builder = new StringBuilder();
        builder.append("{PROGRAM}");
        builder.append(String.format("{DESC:[%s]END-DESC}", this.getDescription()));
        builder.append(String.format("{INIT_ADDR=%s}", Const.PROG_INIT_STORAGE_ADDR));
        builder.append("{INIT_DATA:");
        for (Integer key : initData.keySet()) {
            builder.append(String.format("[%d|%d]",
                    key, initData.get(key)));
        }
        builder.append("}");

        Set<Integer> keys = instLists.keySet();
        for (Integer key : keys) {
            List<String> instList = instLists.get(key);
            builder.append("{[ADDR=").append(key).append("]:");
            for (String instStr : instList) {
                Instruction instruction = Instruction.build(instStr);
                if (instruction != null) {
                    Integer word = instruction.toWord();
                    builder.append(String.format("[%d]", word));
                } else {
                    throw new IllegalArgumentException("Invalid Instruction:"
                            + instStr);
                }
            }
            builder.append("}");
        }

        return builder.toString();
    }

    /**
     * build a program from a binary format
     * */
    public static Program fromBinaryString(String binaryString) {
        Program program = null;
        String programString = CoreUtil.fromFixedLenBinStr(binaryString, BIT_LENGTH);
        Pattern pattern = Pattern.compile(PROGRAM_REGEX);
        Matcher matcher = pattern.matcher(programString);
        if (matcher.matches()) {
            program = new Program();
            // extract init addr
            int addrIndex = extractInitAddrStorage(programString);
            program.setInitAddrIndex(addrIndex);
            // extract init data
            Map<Integer, Integer> initData = extractInitData(programString);
            initData.forEach(program::putInitData);
            // extract insts
            Map<Integer, List<String>> instLists = extractInstLists(programString);
            instLists.forEach(program::putInstructionList);
            // extract description
            program.setDescription(extractDescription(programString));
        } else {
            // not a valid program
            throw new IllegalArgumentException("Invalid Program:" + programString);
        }
        return program;
    }

    /**
     * extract description from program string
     * */
    private static String extractDescription(String programString) {
        Pattern pattern = Pattern.compile(DESCRIPTION_REGEX);
        Matcher matcher = pattern.matcher(programString);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String desc = matcher.group().replaceFirst("\\{DESC:\\[", "")
                    .replace("]END-DESC}",
                            "");
            builder.append(desc);
        }

        return builder.toString();
    }

    /**
     * extract init address storage address from program string
     * */
    private static int extractInitAddrStorage(String programString) {
        Pattern pattern = Pattern.compile(INIT_ADDR_REGEX);
        Matcher matcher = pattern.matcher(programString);
        int addrIndex = 0;
        while (matcher.find()) {
            String datString = matcher.group();
            datString = datString.replace("{INIT_ADDR=", "").replace("}", "");
            addrIndex = Integer.valueOf(datString);
        }
        return addrIndex;
    }

    /**
     * extract init data from program string
     * */
    private static Map<Integer, Integer> extractInitData(String programString) {
        Pattern pattern = Pattern.compile(INIT_DATA_INNER_REGEX);
        Matcher matcher = pattern.matcher(programString);
        Map<Integer, Integer> initData = new HashMap<>();
        while (matcher.find()) {
            String dataStr = matcher.group();
            String[] dat = dataStr.replace("[", "").replace("]", "").split("\\|");
            int addr = Integer.valueOf(dat[0]);
            int data = Integer.valueOf(dat[1]);
            initData.put(addr, data);
        }
        return initData;
    }

    /**
     * extract parts map from program string
     *
     * key of the map is the init address storage address of each parts
     * */
    private static Map<Integer, List<String>> extractInstLists(String programString) {
        Map<Integer, List<String>> insts = new HashMap<>();
        Pattern pattern = Pattern.compile(PART_REGEX);
        Matcher matcher = pattern.matcher(programString);
        Pattern partAddrPattern = Pattern.compile(PART_ADDR_REGEX);
        Pattern instPattern = Pattern.compile(INST_REGEX);

        int partAddr;
        while (matcher.find()) {
            List<String> newPart = new ArrayList<>();
            Matcher partAddrMatcher = partAddrPattern.matcher(matcher.group());
            if (!partAddrMatcher.find()) {
                continue;
            } else {
                String partAddrStr = partAddrMatcher.group();
                partAddr = Integer.valueOf(partAddrStr.replace("[ADDR=", "").replace("]", ""));
            }
            Matcher instMatcher = instPattern.matcher(matcher.group());
            while (instMatcher.find()) {
                String dat = instMatcher.group().replace("[", "").replace("]", "");
                int word = Integer.valueOf(dat);
                Instruction instruction = Instruction.build(word);
                newPart.add(instruction.toString());
            }
            insts.put(partAddr, newPart);
        }

        return insts;
    }
    
    public String toFixedLenBinaryString(int length) {
        return CoreUtil.toFixedLenBinStr(this.toString(), length);
    }

    public byte[] getByteFormat() {
        String binaryString = this.toFixedLenBinaryString(BIT_LENGTH);
        byte[] bytes = new byte[binaryString.length()];
        for (int i = 0; i < binaryString.length(); i++) {
            bytes[i] = Integer.valueOf(String.valueOf(binaryString.charAt(i))).byteValue();
        }
        return bytes;
    }
}
