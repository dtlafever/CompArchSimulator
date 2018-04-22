package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Inst;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.MachineFaultException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CPU {

    // identity of cpu
    private final String CPU_ID = UUID.randomUUID().toString();

    private Registers registers;
    private MCU mcu;
    private List<Device> devices;

    /**
     * staged instructions
     * */
    private LinkedList<Instruction> IFs;
    private LinkedList<Instruction> EXs;

    // initialize a new cpu
    public CPU() {
        // initialize all cpu components
        registers = new Registers();
        mcu = new MCU();
        IFs = new LinkedList<>();
        EXs = new LinkedList<>();
        devices = new ArrayList<>();
        System.out.println(String.format("CPU %s started", CPU_ID));
    }

    // flush this cpu
    public void reset() {
        registers.reset();
        mcu.reset();
        IFs.clear();
        EXs.clear();
        System.out.println(String.format("CPU %s was flushed", CPU_ID));
    }

    /**
     * do a cpu click cycle
     *
     * fetch->execute->memory access
     * */
    public ExecutionResult tick() throws MachineFaultException {
        Instruction instruction = EXs.poll();
        ExecutionResult executionResult = null;
        if(instruction != null){
            // do memory access
            instruction.mem(this);
        }

        instruction = IFs.poll();
        if(instruction != null){
            try {
                //EXECUTE
                executionResult = instruction.execute(this);
                executionResult.setMessage(instruction.getMessage());
                // move to next stage
                EXs.offer(instruction);
            } catch (MachineFaultException t) {
                // set machine fault code to register MFR
                registers.MFR = t.getFaultCode();
                throw t;
            }
        }

        fetch();
        if(executionResult == null){
            executionResult = ExecutionResult.CONTINUE;
        }
        return executionResult;
    }

    // Grab the instruction at the PC location
    public void fetch() throws MachineFaultException {
        //mcu.storeIntoCache(registers.getPC(), );
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getFromCache(registers.getMAR()));
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a separate adder for PC
        registers.setIR(registers.getMBR());
        // move instruction to second stage
        Instruction instruction = Instruction.build(registers.getIR());
        IFs.offer(instruction);
    }

    /**
     * Decode the current instruction in IR and execute it.
     */
    public ExecutionResult decodeAndExecute() {
        ExecutionResult executionResult;
        int word = registers.getIR();
        //DECODE
        Instruction instruction = Instruction.build(word);
        try {
            if (instruction != null) {
                //EXECUTE
                executionResult = instruction.execute(this);
                executionResult.setMessage(instruction.getMessage());
            } else {
                // illegal instruction, throw illegal operation code machine fault
                throw new MachineFaultException(Const.FaultCode.ILL_OPRC.getValue(), Const.FaultCode.ILL_OPRC.getMessage());
            }
        } catch (MachineFaultException t) {
            executionResult = ExecutionResult.HALT;
            // set machine fault code to register MFR
            registers.MFR = t.getFaultCode();
        }
        return executionResult;
    }

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
