package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.MachineFaultException;

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
        //mcu.storeIntoCache(registers.getPC(), );
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getFromCache(registers.getMAR()));
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a separate adder for PC
        registers.setIR(registers.getMBR());
        return registers.getIR();
    }

    /**
     * Decode the current instruction in IR and execute it.
     */
    public ExecutionResult decodeAndExecute() {
        ExecutionResult executionResult;
        int word = registers.getIR();
        //DECODE
        Instruction instruction = Instruction.build(word);
        try{
            if (instruction != null) {
                //EXECUTE
                executionResult = instruction.execute(this);
                executionResult.setMessage(instruction.getMessage());
            } else {
                // This instruction doesn't exist
                throw new MachineFaultException(Const.FaultCode.ILL_OPRC.getValue());
            }
        } catch (MachineFaultException t) {
            executionResult = ExecutionResult.HALT;
            t.printStackTrace();

        }
        return executionResult;
    }

    private void handleMachineFault(int faultCode, String message) {
		// when a machine fault occurs, we should save current values of PC and
		// MSR into reserved locations in memory.
		registers.setMAR(4);
		registers.setMBR(registers.getPC());
		mcu.storeToCache(registers.getMAR(), registers.getMBR());

        // using location 5 (since it is not used)
		registers.setMAR(5);
		registers.setMBR(registers.getMSR());
		mcu.storeToCache(registers.getMAR(), registers.getMBR()); 

		registers.setMFR(faultCode);

		// now we should fetch from location 1 into the PC
		registers.setPC(mcu.getFromCache(1));

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
