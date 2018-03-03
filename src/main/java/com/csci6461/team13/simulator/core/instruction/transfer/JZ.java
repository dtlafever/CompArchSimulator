package com.csci6461.team13.simulator.core.instruction.transfer;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;;

public class JZ extends Instruction {
    
    // Jump if Zero
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        if (mcu.getWord(registers.getR(this.getR())) == 0){
            registers.setPC(getEffectiveAddress(mcu, registers));
        }

        return ExecutionResult.CONTINUE;
    }
}