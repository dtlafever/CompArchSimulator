package com.csci6461.team13.simulator.core.instruction.loadstore;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class STX extends Instruction {

    @Override
    public boolean execute(Registers registers, MCU mcu) {
        registers.setMAR(this.getEffectiveAddress(mcu, registers));
        registers.setMBR(registers.getX(this.getIx()));
        mcu.storeWord(registers.getMAR(), registers.getMBR());
        return true;
    }
}
