package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;

public class AIR extends Instruction {

    // Add address to Register r (0 - 3)
    // NOTE: 
    // if address is 0, nothing happens
    // if c(r) = 0, load r with address
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int max = CoreUtil.maxOfBits(Const.CPU_BIT_LENGTH);
        int min = CoreUtil.minOfBits(Const.CPU_BIT_LENGTH);
        if (this.getAddress() != 0) {
            if (registers.getR(this.getR()) == 0) {
                registers.setR(this.getR(), this.getAddress());
            } else {
                int result = registers.getR(this.getR()) + this.getAddress();

                // Overflow check!
                if (result > max || result < min) {
                    registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
                } else {
                    registers.setR(this.getR(), result);
                }
            }
        }
        return ExecutionResult.CONTINUE;
    }
}