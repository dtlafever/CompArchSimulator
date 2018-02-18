package com.csci6461.team13.simulator.core.instruction;

import com.csci6461.team13.simulator.core.instruction.Miscellaneous.HLT;
import com.csci6461.team13.simulator.core.instruction.loadStore.*;

public enum Inst {

    // miscellaneous instructions
    INST_HLT(0, "HLT", HLT.class),

    // load and store instructions
    INST_LDR(1, "LDR", LDR.class),
    INST_STR(2, "STR", STR.class),
    INST_LDA(3, "LDA", LDA.class),
    INST_LDX(41, "LDX", LDX.class),
    INST_STX(42, "STX", STX.class);

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
