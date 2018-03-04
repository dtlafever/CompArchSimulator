package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;

public class SIR extends Instruction {

    // Subtract address from register, r (0 - 3)
    // NOTE:
    // if address = 0, dont do anything
    // if c(r) = 0; load r1 with -(address)
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int max = CoreUtil.maxOfBits(Const.CPU_BIT_LENGTH);
        int min = CoreUtil.minOfBits(Const.CPU_BIT_LENGTH);
        if (this.getAddress() != 0) {
            if (registers.getR(this.getR()) == 0) {
                registers.setR1(~this.getAddress());
            } else {
                int result = registers.getR(this.getR()) - this.getAddress();

                //overflow
                if (result > max || result < min) {
                    registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
                } else {
                    // updating the value of register if there is no overflow
                    registers.setR(this.getR(), registers.getR(this.getR()) - this.getAddress());
                }
            }
        }

        return ExecutionResult.CONTINUE;
    }
}