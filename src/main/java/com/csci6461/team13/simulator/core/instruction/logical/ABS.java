package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;

/**
 * @author zhiyuan
 */
public class ABS extends Instruction {
    // get absolute value of c(r)
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int rx = registers.getR(this.getR());
        registers.setR(this.getR(), Math.abs(rx));
        return ExecutionResult.CONTINUE;
    }
}
