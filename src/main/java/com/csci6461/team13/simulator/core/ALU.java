package com.csci6461.team13.simulator.core;

public class ALU {

    private Registers registers;
    private MCU mcu;

    ALU(Registers registers, MCU mcu) {
        this.registers = registers;
        this.mcu = mcu;
    }
}