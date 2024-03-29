package com.csci6461.team13.simulator.core.instruction.transfer;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;

;

public class RFS extends Instruction {

    // Return from subroutine with a return code
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        registers.setR0(this.getAddress());
        registers.setPC(registers.getR3());
        return ExecutionResult.CONTINUE;
    }
}