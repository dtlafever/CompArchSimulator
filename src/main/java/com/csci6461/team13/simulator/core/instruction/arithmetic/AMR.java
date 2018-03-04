package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;

public class AMR extends Instruction {
    
    // Add Memory to Register r (0 - 3)
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int max = CoreUtil.maxOfBits(Const.CPU_BIT_LENGTH);
        int min = CoreUtil.minOfBits(Const.CPU_BIT_LENGTH);

        //store effective address in memory address register
        registers.setMAR(this.getEffectiveAddress(mcu, registers));

        //store what was fetched from memory into MBR
        registers.setMBR(mcu.getWord(registers.getMAR()));

        int result = registers.getR(this.getR()) + registers.getMBR();

        // Overflow check!
        if (result > max || result < min){
            registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
        }else{
            registers.setR(this.getR(), result);
        }

        return ExecutionResult.CONTINUE;
    }
}
