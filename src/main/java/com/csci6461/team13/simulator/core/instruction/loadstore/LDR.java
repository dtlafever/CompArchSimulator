package com.csci6461.team13.simulator.core.instruction.loadstore;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.MachineFaultException;

public class LDR extends Instruction {
    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        MCU mcu = cpu.getMcu();
        Registers registers = cpu.getRegisters();
        registers.setMAR(this.getEffectiveAddress(mcu, registers));
        registers.setMBR(mcu.getWord(registers.getMAR()));
        registers.setR(this.getR(), registers.getMBR());
        return ExecutionResult.CONTINUE;
    }
}
