package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.Instruction;

import java.util.UUID;

public class CPU {

    // identity of cpu
    private final String CPU_ID = UUID.randomUUID().toString();

    private Registers registers;
    private MCU mcu;
    private ALU alu;

    // initialize a new cpu
    public CPU() {
        // initialize all cpu components
        registers = new Registers();
        mcu = new MCU();
        alu = new ALU(registers, mcu);
        System.out.println(String.format("CPU %s started", CPU_ID));
    }

    // reset this cpu
    public void reset() {
        registers.reset();
        mcu.reset();
        System.out.println(String.format("CPU %s has been reset", CPU_ID));
    }

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    // Grab the instruction at the PC location
    public int fetch() {
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a seperate adder for PC
        registers.setIR(registers.getMBR());
        return registers.getIR();
    }

    // Decode the current instruction and execute it.
    // return false if halt opcode is seen
    public boolean decodeAndExecute(int word) {
        boolean next = true;
        //DECODE
        Instruction instruction = Instruction.build(word);

        if (instruction != null) {
            //EXECUTE
            next = instruction.execute(registers, mcu);

        } else {
            //TODO: add machine fault if not a good instruction
        }
        return next;
    }

    //----------------------
    //      GETTERS/SETTERS
    //----------------------

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
