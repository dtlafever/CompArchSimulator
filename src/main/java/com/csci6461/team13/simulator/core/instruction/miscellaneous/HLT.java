package com.csci6461.team13.simulator.core.instruction.miscellaneous;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class HLT extends Instruction {

    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        registers.setPC(registers.getPC()-1);
        return ExecutionResult.HALT;
    }
}
