package com.csci6461.team13.simulator.core;

import java.math.BigInteger;

public class Instruction{

    int opcode; // 6 bits
    int r;      // 2 bits
	int ix;     // 2 bits
	int i;      // 1 bit
    int address;// 5 bits

    public Instruction(){
        this.opcode = 0;
        this.r = 0;
        this.ix = 0;
        this.i = 0;
        this.address = 0;
    }

    // given a string of an instruction, decompose it
    public Instruction(String instruction){
        this.opcode = binaryToDecimal(instruction.substring(0, 6));
        this.r      = binaryToDecimal(instruction.substring(6, 8));
        this.ix     = binaryToDecimal(instruction.substring(8, 10));
        this.i      = binaryToDecimal(instruction.substring(10, 11));
        this.address = binaryToDecimal(instruction.substring(11, 16));
    }

    public int getOpcode(){
        return this.opcode;
    }

    public void setOpcode(int value){
        this.opcode = value;
    }

    public int getR(){
        return this.r;
    }

    public void setR(int value){
        this.r = value;
    }

    public int getIx(){
        return this.ix;
    }

    public void setIx(int value){
        this.ix = value;
    }

    public int getI(){
        return this.i;
    }

    public void setI(int value){
        this.i = value;
    }

    public int getAddress(){
        return this.address;
    }

    public void setAddress(int value){
        this.address = value;
    }

    // take a binary string and convert to integer
	public int binaryToDecimal(String binary) {
		return new BigInteger(binary, 2).intValue();
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
}