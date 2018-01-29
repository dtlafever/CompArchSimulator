package com.csci6461.team13.simulator.core;

import java.util.UUID;

public class CPU {

    // identity of the initialized cpu
    private String CPU_ID = UUID.randomUUID().toString();

    private Registers registers;
    private MCU mcu;

    // initialize a new cpu
    public CPU() {
        // initialize the registers and memory control unit
        registers = new Registers();
        mcu = new MCU();
        System.out.println("CPU " + CPU_ID + " started");
    }

    //----------------------
    //      GETTERS/SETTERS
    //----------------------

    // TODO: 1/27/18 [Zhiyuan Ling] I'm not sure about these setters, we'll get rid of them if you think they are certainly useless

    public String getCPU_ID() {
        return CPU_ID;
    }

    public Registers getRegisters() {
        return registers;
    }

    public void setRegisters(Registers registers) {
        this.registers = registers;
    }

    public MCU getMcu() {
        return mcu;
    }

    public void setMcu(MCU mcu) {
        this.mcu = mcu;
    }

}
