package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;

public class AND extends Instruction {
    
    // Logical AND of 2 registers
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int rx = this.getR();
        int ry = this.getIx();

        int x  = registers.getR(rx);
        int y  = registers.getR(ry);

        x = x & y;

        registers.setR(rx, x);

        return ExecutionResult.CONTINUE;
    }
}