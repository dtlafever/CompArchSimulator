package com.csci6461.team13.simulator.core.instruction.logical;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.CoreUtil;;

public class SRC extends Instruction {
    
    // Shift Register by Count
    // NOTE: we have to do some finagling for this instruction for AL and LR
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        String strWord = CoreUtil.int2FixedLenBinStr(this.getIx(), 2);
        int AL = Integer.parseInt(strWord.substring(0, 1), 2);
        int LR = Integer.parseInt(strWord.substring(1, 2), 2);
        int BD = registers.getR(this.getR());
        int CT = this.getAddress();

        if (AL == 0) {
            if (LR == 0) {
                BD = BD >> CT;
            }
            if (LR == 1) {
                BD = BD << CT;
            }
        }
        if (AL == 1) {
            if (LR == 0) {
                if (BD >= 0)
                    BD = (BD >>> CT);
                else {
                    String x = Integer.toBinaryString(BD >>> CT);
                    x = x.replace("1111111111111111", "");
                    BD = Integer.parseInt(x, 2);
                }
            }
            if (LR == 1) {
                BD = BD << CT;
            }
        }

        registers.setR(this.getR(), BD);

        return ExecutionResult.CONTINUE;
    }
}