package com.csci6461.team13.simulator.core.instruction.transfer;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.MachineFaultException;

;

public class JNE extends Instruction {

    // Jump if not equal to
    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int rVal = registers.getR(this.getR());
        if (rVal != 0) {
            registers.setPC(getEffectiveAddress(mcu, registers));
        }

        return ExecutionResult.CONTINUE;
    }
}