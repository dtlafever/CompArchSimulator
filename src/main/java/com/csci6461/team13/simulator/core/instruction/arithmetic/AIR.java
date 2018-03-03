package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;

public class AIR extends Instruction {
    
    // Add address to Register r (0 - 3)
    // NOTE: 
    // if address is 0, nothing happens
    // if c(r) = 0, load r with address
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        if (this.getAddress() != 0){
            if (registers.getR(this.getR()) == 0){
                registers.setR(this.getR(), this.getAddress());
            }else{
                int result = registers.getR(this.getR()) + this.getAddress();

                // Overflow check!
                if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE){
                    registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
                }else{
                    registers.setR(this.getR(), result);
                }
            }
        }
        return ExecutionResult.CONTINUE;
    }
}