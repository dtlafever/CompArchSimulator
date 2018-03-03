package com.csci6461.team13.simulator.core.instruction;

import com.csci6461.team13.simulator.core.instruction.io.IN;
import com.csci6461.team13.simulator.core.instruction.io.OUT;
import com.csci6461.team13.simulator.core.instruction.miscellaneous.HLT;
import com.csci6461.team13.simulator.core.instruction.loadstore.*;
import com.csci6461.team13.simulator.core.instruction.transfer.*;
import com.csci6461.team13.simulator.core.instruction.arithmetic.*;
import com.csci6461.team13.simulator.core.instruction.logical.*;

public enum Inst {

    // miscellaneous instructions
    INST_HLT(0, "HLT", HLT.class),

    // load and store instructions
    INST_LDR(1, "LDR", LDR.class),
    INST_STR(2, "STR", STR.class),
    INST_LDA(3, "LDA", LDA.class),
    INST_LDX(41, "LDX", LDX.class),
    INST_STX(42, "STX", STX.class),
    // IO
    INST_IN(61, "IN", IN.class),
    INST_OUT(62, "OUT", OUT.class),
    
    //TRANSFER
    INST_JZ(010, "JZ", JZ.class),
    INST_JNE(011, "JNE", JNE.class),
    INST_JCC(012, "JCC", JCC.class),
    INST_JMA(013, "JMA", JMA.class),
    INST_JSR(014, "JSR", JSR.class),
    INST_RFS(015, "RFS", RFS.class),
    INST_SOB(016, "SOB", SOB.class),
    INST_JGE(017, "JGE", JGE.class),

    //Arithmetic
    INST_AMR(004, "AMR", AMR.class),
    INST_SMR(005, "SMR", SMR.class),
    INST_AIR(006, "AIR", AIR.class),
    INST_SIR(007, "SIR", SIR.class),
    INST_MLT(020, "MLT", MLT.class),
    INST_DVD(021, "DVD", DVD.class),

    //Logical
    INST_TRR(022, "TRR", TRR.class),
    INST_AND(023, "AND", AND.class),
    INST_ORR(024, "ORR", ORR.class),
    INST_NOT(025, "NOT", NOT.class),
    INST_SRC(031, "SRC", SRC.class),
    INST_RRC(032, "RRC", RRC.class),
    ;

    Inst(int opcode, String title, Class<? extends Instruction> instClass) {
        this.opcode = opcode;
        this.title = title;
        this.instClass = instClass;
    }

    public static Inst findByOpcode(int opcode) {
        for (Inst inst : values()) {
            if (inst.opcode == opcode) {
                return inst;
            }
        }
        return null;
    }

    public static Inst findByClass(Class<? extends Instruction> clazz) {
        for (Inst inst : values()) {
            if (clazz == inst.instClass) {
                return inst;
            }
        }
        return null;
    }

    public static Inst findByTitle(String title) {
        for (Inst inst : values()) {
            if (title != null && title.trim().toUpperCase().equals(inst.title)) {
                return inst;
            }
        }
        return null;
    }

    private int opcode;
    private String title;
    private Class<? extends Instruction> instClass;

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<? extends Instruction> getInstClass() {
        return instClass;
    }

    public void setInstClass(Class<? extends Instruction> instClass) {
        this.instClass = instClass;
    }

}
