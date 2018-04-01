package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;

;

public class TRR extends Instruction {

    // Jump if not equal to
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        int rx = this.getR();
        int ry = this.getIx();

        if (registers.getR(rx) == registers.getR(ry)) {
            registers.setCCByBit(Const.ConditionCode.EQUALORNOT.getValue(), true);
        } else {
            registers.setCCByBit(Const.ConditionCode.EQUALORNOT.getValue(), false);
        }

        return ExecutionResult.CONTINUE;
    }
}