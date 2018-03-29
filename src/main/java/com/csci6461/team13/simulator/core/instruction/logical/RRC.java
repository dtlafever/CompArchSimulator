package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.CoreUtil;

public class RRC extends Instruction {
    
    // Rotate Register by Count
    // NOTE: we have to do some finagling for this instruction for AL and LR
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        String strWord = CoreUtil.int2FixedLenBinStr(this.getIx(), 2);
        int AL = Integer.parseInt(strWord.substring(0, 1), 2);
        int LR = Integer.parseInt(strWord.substring(1, 2), 2);
        int BD = registers.getR(this.getR());
        int CT = this.getAddress();

        String x = null; // first part of the content
		String y = null; // second part of the content
		String z = null; // string form of content of the register

		z = Integer.toBinaryString(BD);
		if (BD >= 0)
			;
		z = z.replace("0000000000000000", "");
		if (BD < 0)
			z = z.replaceAll("1111111111111111", "");

		if (LR == 1) {
			x = z.substring(CT, z.length());
			y = z.substring(0, CT);
			z = x + y;
		}
		if (LR == 0) {
			x = z.substring(0, z.length() - CT);
			y = z.substring(z.length() - CT, z.length());
			z = y + x;
		}

		BD = Integer.parseInt(z, 2);
		registers.setR(this.getR(), BD);

		return ExecutionResult.CONTINUE;
    }
}