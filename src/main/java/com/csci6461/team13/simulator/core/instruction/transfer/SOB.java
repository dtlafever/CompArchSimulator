package com.csci6461.team13.simulator.core.instruction.transfer;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.MachineFaultException;

public class SOB extends Instruction {

    // Subtract one and branch
    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        registers.setR(this.getR(), mcu.getWord(registers.getR(this.getR()) - 1));

        if (mcu.getWord(registers.getR(this.getR())) > 0) {
            registers.setPC(getEffectiveAddress(mcu, registers));
        } //otherwise do nothing

        return ExecutionResult.CONTINUE;
    }
}