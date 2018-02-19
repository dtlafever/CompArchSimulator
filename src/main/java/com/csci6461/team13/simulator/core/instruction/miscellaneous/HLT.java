package com.csci6461.team13.simulator.core.instruction.miscellaneous;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class HLT extends Instruction {

    @Override
    public boolean execute(Registers registers, MCU mcu) {
        return false;
    }
}
