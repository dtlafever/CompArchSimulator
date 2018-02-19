package com.csci6461.team13.simulator.core.instruction.loadstore;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class LDA extends Instruction {

    @Override
    public boolean execute(Registers registers, MCU mcu) {
        int effectiveAddress = this.getEffectiveAddress(mcu, registers);
        registers.setR(this.getR(), effectiveAddress);
        return true;
    }
}
