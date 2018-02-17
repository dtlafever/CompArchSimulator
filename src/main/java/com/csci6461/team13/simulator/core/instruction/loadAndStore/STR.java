package com.csci6461.team13.simulator.core.instruction.loadAndStore;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class STR extends Instruction {
    @Override
    public boolean execute(Registers registers, MCU mcu) {
        registers.setMAR(this.getEffectiveAddress(mcu, registers));
        registers.setMBR(registers.getR(this.getR()));
        mcu.storeWord(registers.getMAR(), registers.getMBR());
        return true;
    }
}
