package com.csci6461.team13.simulator.core.instruction;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.csci6461.team13.simulator.util.CoreUtil.int2FixedLenStr;

public abstract class Instruction {

    /**
     * 6 bits
     */
    private int opcode;
    /**
     * 2 bits
     */
    private int r;
    /**
     * 2 bits
     */
    private int ix;
    /**
     * 1 bits
     */
    private int i;
    /**
     * 5 bits
     */
    private int address;

    protected Instruction() {
        this.opcode = 0;
        this.r = 0;
        this.ix = 0;
        this.i = 0;
        this.address = 0;
    }

    /**
     * build an instruction from symbolic form
     * example: LDR 3,1,1,31
     */
    public static Instruction build(String line) {
        Pattern pattern = Pattern.compile("^(\\w{3})\\s(\\d)," +
                "(\\d),([01]),(\\d{1,2})$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String opcodeStr = matcher.group(1);
            Inst inst = Inst.findByTitle(opcodeStr);
            if (inst != null) {
                int opcode = inst.getOpcode();
                int r = Integer.parseInt(matcher.group(2));
                int ix = Integer.parseInt(matcher.group(3));
                int i = Integer.parseInt(matcher.group(4));
                int address = Integer.parseInt(matcher.group(5));
                return build(opcode, r, ix, i, address);
            }
        }
        return null;
    }

    /**
     * build an instruction from a integer word
     */
    public static Instruction build(int word) {
        String strWord = int2FixedLenStr(word, 16);
        int opcode = Integer.parseInt(strWord.substring(0, 6), 2);
        int r = Integer.parseInt(strWord.substring(6, 8), 2);
        int ix = Integer.parseInt(strWord.substring(8, 10), 2);
        int i = Integer.parseInt(strWord.substring(10, 11), 2);
        int address = Integer.parseInt(strWord.substring(11, 16), 2);

        return build(opcode, r, ix, i, address);
    }

    /**
     * build an instruction from parts
     */
    public static Instruction build(int opcode, int r, int ix, int i, int address) {
        Instruction instruction = null;
        Inst inst = Inst.findByOpcode(opcode);
        if (inst != null) {
            try {
                instruction = inst.getInstClass().getDeclaredConstructor().newInstance();
                instruction.setOpcode(opcode);
                instruction.setI(i);
                instruction.setIx(ix);
                instruction.setR(r);
                instruction.setAddress(address);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return instruction;
    }

    public Integer toWord() {
        String result = int2FixedLenStr(opcode, 6) + int2FixedLenStr(r, 2)
                + int2FixedLenStr(ix, 2) + int2FixedLenStr(i, 1)
                + int2FixedLenStr(address, 5);
        return Integer.parseInt(result, 2);
    }

    public int getOpcode() {
        return this.opcode;
    }

    public void setOpcode(int value) {
        this.opcode = value;
    }

    public int getR() {
        return this.r;
    }

    public void setR(int value) {
        this.r = value;
    }

    public int getIx() {
        return this.ix;
    }

    public void setIx(int value) {
        this.ix = value;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int value) {
        this.i = value;
    }

    public int getAddress() {
        return this.address;
    }

    public void setAddress(int value) {
        this.address = value;
    }

    /**
     * get the effective address
     */
    public int getEffectiveAddress(MCU mcu, Registers registers) {
        if (this.i == 0) {
            // NO indirect addressing
            if (this.ix == 0) {
                return this.address;
            } else {
                return this.address + registers.getX(this.ix);
            }
        } else if (this.i == 1) {
            // indirect addressing, but NO indexing
            if (this.ix == 0) {
                return mcu.getWord(this.address);
            } else {
                return mcu.getWord(this.address + registers.getX(this.ix));
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        Inst inst = Inst.findByOpcode(this.opcode);
        if (inst == null) {
            return null;
        } else {
            String title = inst.getTitle();
            return String.format("%s %d,%d,%d,%d", title, r, ix, i, address);
        }
    }

    /**
     * the primary execution method
     * <p>
     * return true if there is a next instructions
     */
    public abstract boolean execute(Registers registers, MCU mcu);
}