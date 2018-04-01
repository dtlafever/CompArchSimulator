package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;

public class DVD extends Instruction {

    // Divide register by register
    // NOTE: 
    // rx must be 0 or 2
    // rx contains the high order bits, rx+1 contains the lower order bits
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int max = CoreUtil.maxOfBits(Const.CPU_BIT_LENGTH);
        int min = CoreUtil.minOfBits(Const.CPU_BIT_LENGTH);
        int rx = this.getR();
        int ry = this.getIx();

        if ((rx == 0 || rx == 2) && (ry == 0 || ry == 2)) {
            //check for DIVZERO error
            if (registers.getR(ry) == 0) {
                registers.setCCByBit(Const.ConditionCode.DIVZERO.getValue(), true);
            } else {
                int result = registers.getR(rx) / registers.getR(ry);

                //check if we have overflow
                if (result > max || result < min) {
                    registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
                } else {

                    // we dont have overflow, update registers
                    int remainder = registers.getR(rx) % registers.getR(ry);

                    // saving the quotient in rx
                    registers.setR(rx, result);

                    // saving the remainder in rx+1
                    registers.setR(rx + 1, remainder);
                }
            }
        }

        return ExecutionResult.CONTINUE;
    }
}