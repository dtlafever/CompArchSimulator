package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.util.CoreUtil;

public class ALU {

    private Registers registers;
    private MCU mcu;

    ALU(Registers registers, MCU mcu){
        this.registers = registers;
        this.mcu = mcu;
    }

    //--------------------------
    //     ASSEMBLY COMMANDS
    //--------------------------

    // --- LOAD/STORE ----------

    public void LDR(Instruction instruction) {
        registers.setMAR(instruction.getEffectiveAddress(mcu, registers));
        registers.setMBR(mcu.getWord(registers.getMAR()));
        registers.setR(instruction.getR(), registers.getMBR());
    }

    public void STR(Instruction instruction) {
        registers.setMAR(instruction.getEffectiveAddress(mcu, registers));
        registers.setMBR(registers.getR(instruction.getR()));
        mcu.storeWord(registers.getMAR(), registers.getMBR());
    }

    public void LDA(Instruction instruction) {
        int effectiveAddress = instruction.getEffectiveAddress(mcu, registers);
        registers.setR(CoreUtil.binaryToDecimal(instruction.getR()), effectiveAddress);
    }

    public void LDX(Instruction instruction) {
        registers.setMAR(instruction.getEffectiveAddress(mcu, registers));
        registers.setMBR(mcu.getWord(registers.getMAR()));
        registers.setX(CoreUtil.binaryToDecimal(instruction.getIx()), registers.getMBR());
    }

    public void STX(Instruction instruction) {
        registers.setMAR(instruction.getEffectiveAddress(mcu, registers));
        registers.setMBR(registers.getX(instruction.getIx()));
        mcu.storeWord(registers.getMAR(), registers.getMBR());
    }
}