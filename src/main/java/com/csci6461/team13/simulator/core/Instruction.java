package com.csci6461.team13.simulator.core;

//import java.math.BigInteger;

public class Instruction {

    int opcode; // 6 bits
    int r;      // 2 bits
    int ix;     // 2 bits
    int i;      // 1 bit
    int address;// 5 bits

    public Instruction() {
        this.opcode = 0;
        this.r = 0;
        this.ix = 0;
        this.i = 0;
        this.address = 0;
    }

    // given a integer of an instruction, decompose it
    public Instruction(int word) {
        String strWord = Integer.toBinaryString(word);
        while (strWord.length() < 16) {
            strWord = "0" + strWord;
        }

        this.opcode = Integer.parseInt(strWord.substring(0, 6), 2);
        this.r = Integer.parseInt(strWord.substring(6, 8), 2);
        this.ix = Integer.parseInt(strWord.substring(8, 10), 2);
        this.i = Integer.parseInt(strWord.substring(10, 11), 2);
        this.address = Integer.parseInt(strWord.substring(11, 16), 2);
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

    // take a binary string and convert to integer
    // public int binaryToDecimal(String binary) {
    // 	return new BigInteger(binary, 2).intValue();
    // }

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
}