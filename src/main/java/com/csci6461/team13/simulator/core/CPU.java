package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CPU {

    // identity of cpu
    private final String CPU_ID = UUID.randomUUID().toString();

    private Registers registers;
    private MCU mcu;
    private ALU alu;
    private List<Device> devices;

    // initialize a new cpu
    public CPU() {
        // initialize all cpu components
        registers = new Registers();
        mcu = new MCU();
        alu = new ALU(registers, mcu);
        devices = new ArrayList<>();
        System.out.println(String.format("CPU %s started", CPU_ID));
    }

    // flush this cpu
    public void reset() {
        registers.reset();
        mcu.reset();
        System.out.println(String.format("CPU %s has been flush", CPU_ID));
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

    /**
     * Decode the current instruction in IR and execute it.
     */
    public ExecutionResult decodeAndExecute() {
        ExecutionResult executionResult = ExecutionResult.CONTINUE;
        int word = registers.getIR();
        //DECODE
        Instruction instruction = Instruction.build(word);

        if (instruction != null) {
            //EXECUTE
            executionResult = instruction.execute(this);
            executionResult.setMessage(instruction.getMessage());
            if(executionResult.equals(ExecutionResult.RETRY)||executionResult.equals(ExecutionResult.HALT)){
                int prevVal = registers.getPC()-1;
                registers.setPC(prevVal);
            }
        } else {
            //TODO: add machine fault if not a good instruction
        }
        return executionResult;
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

    public List<Device> getDevices() {
        return devices;
    }
}
