package com.csci6461.team13.simulator.core.instruction.arithmetic;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;;

public class MLT extends Instruction {
    
    // Mltiply register by register
    // NOTE:
    // rx must be 0 or 2, contains high order bits
    // rx+1 contains low order bits
    // ry must be 0 or 2
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
		int max = CoreUtil.maxOfBits(Const.CPU_BIT_LENGTH);
		int min = CoreUtil.minOfBits(Const.CPU_BIT_LENGTH);
        int rx = this.getR();
        int ry = this.getIx();

        if ((rx == 0 || rx == 2) && (ry == 0 || ry == 2)) {
			int result = registers.getR(rx) * registers.getR(ry);

			// overflow
			if (result > max || result < min) {
				registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
			} else {
				// rx contains the high order bits of the result
				registers.setR(rx, getHighOrderBits(result));

				// rx+1 contains the low order bits of the result
				registers.setR(rx + 1, getLowOrderBits(result));
			}
		}

        return ExecutionResult.CONTINUE;
    }

    // getting the low 16 bits of an integer
	public static int getLowOrderBits(int x) {
		return (x & 0xFFFF);
	}

	// getting the high 16 bits of an integer
	public static int getHighOrderBits(int x) {
		return x >> 16;
	}
}