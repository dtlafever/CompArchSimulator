package com.csci6461.team13.simulator.core.instruction;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Instruction {

    private int opcode; // 6 bits
    private int r;      // 2 bits
    private int ix;     // 2 bits
    private int i;      // 1 bit
    private int address;// 5 bits

    protected Instruction() {
        this.opcode = 0;
        this.r = 0;
        this.ix = 0;
        this.i = 0;
        this.address = 0;
    }

    public static Instruction build(int word) {
        String strWord = Integer.toBinaryString(word);
        while (strWord.length() < 16) {
            strWord = "0" + strWord;
        }

        int opcode = Integer.parseInt(strWord.substring(0, 6), 2);
        int r = Integer.parseInt(strWord.substring(6, 8), 2);
        int ix = Integer.parseInt(strWord.substring(8, 10), 2);
        int i = Integer.parseInt(strWord.substring(10, 11), 2);
        int address = Integer.parseInt(strWord.substring(11, 16), 2);
        Instruction instruction = null;
        Class<? extends  Instruction> clazz = Inst.findClassByOpcode(opcode);
        if(clazz != null){
            try {
                instruction = clazz.getDeclaredConstructor().newInstance();
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

    //get the effective address 
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

    public abstract boolean execute(Registers registers, MCU mcu);
}