package com.csci6461.team13.simulator.core.instruction;

import com.csci6461.team13.simulator.core.instruction.Miscellaneous.HLT;
import com.csci6461.team13.simulator.core.instruction.loadAndStore.*;

public enum Inst {

    // miscellaneous instructions
    INST_HALT(0, HLT.class),

    // load and store instructions
    INST_LDR(1, LDR.class),
    INST_STR(2, STR.class),
    INST_LDA(3, LDA.class),
    INST_LDX(41, LDX.class),
    INST_STX(42, STX.class);

    Inst(int opcode, Class<? extends Instruction> instClass) {
        this.opcode = opcode;
        this.instClass = instClass;
    }

    public static Class<? extends Instruction> findClassByOpcode(int opcode) {
        for (Inst inst : values()) {
            if (inst.opcode == opcode) {
                return inst.instClass;
            }
        }
        return null;
    }

    public static Integer findOpcodeByClass(Class<? extends Instruction> clazz) {
        for (Inst inst : values()) {
            if(clazz == inst.instClass){
                return inst.opcode;
            }
        }
        return null;
    }

    private int opcode;
    private Class<? extends Instruction> instClass;

}
