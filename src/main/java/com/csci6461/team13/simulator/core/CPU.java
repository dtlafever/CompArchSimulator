package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.Instruction;
import com.csci6461.team13.simulator.util.Const;
import java.util.UUID;

public class CPU {

    // identity of cpu
    private String CPU_ID = UUID.randomUUID().toString();

    private Registers registers;
    private MCU mcu;
    private ALU alu;

    // initialize a new cpu
    public CPU() {
        // initialize all cpu components
        registers = new Registers();
        mcu = new MCU();
        alu = new ALU(registers, mcu);
        System.out.println("CPU " + CPU_ID + " started");
    }

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    // Grab the instruction at the PC location
    public void fetch() {
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a seperate adder for PC
        registers.setIR(registers.getMBR());
    }

    // Decode the current instruction and execute it.
    // return false if halt opcode is seen
    public boolean decodeAndExecute(int inst) {
        boolean next = true;
        //DECODE
        Instruction instruction = new Instruction(inst);
        int opCode = instruction.getOpcode();

        //TODO: clean up so it isn't using switch cases
        if (Const.OPCODE.containsKey(opCode)) {
            instruction = new Instruction(inst);

            //EXECUTE
            switch (Const.OPCODE.get(opCode)) {
                case "LDR":
                    alu.LDR(instruction);
                    break;
                case "STR":
                    alu.STR(instruction);
                    break;

                case "LDA":
                    alu.LDA(instruction);
                    break;

                case "LDX":
                    alu.LDX(instruction);
                    break;

                case "STX":
                    alu.STX(instruction);
                    break;
                case "HALT":
                    next = false;
                    break;

                default:
                    break;
            }
        }else{
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
