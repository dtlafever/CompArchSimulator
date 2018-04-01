package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;

public class NOT extends Instruction {

    // Logical not for register to register
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int value = registers.getR(this.getR());
        registers.setR(this.getR(), ~value);

        return ExecutionResult.CONTINUE;
    }
}