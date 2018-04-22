package com.csci6461.team13.simulator.core.instruction.transfer;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.MachineFaultException;

;

public class JSR extends Instruction {

    // Jump and save return address
    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        registers.setR3(registers.getPC());
        registers.setPC(getEffectiveAddress(mcu, registers));

        return ExecutionResult.CONTINUE;
    }
}