package com.csci6461.team13.simulator.core.instruction.miscellaneous;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.MachineFaultException;

public class TRAP extends Instruction {


    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int trapCode = this.getAddress();

        if (trapCode > 15 || trapCode < 0) {
            throw new MachineFaultException(Const.FaultCode.ILL_TRPC.getValue(), Const.FaultCode.ILL_TRPC.getMessage());
        }

        // store pc+1 into memory 2
        registers.setMAR(2);
        registers.setMBR(registers.getPC() + 1);
        mcu.storeToCache(registers.getMAR(), registers.getMBR());
        // goes to the routine whose address is in memory location 0
        registers.setMAR(0);
        registers.setMBR(mcu.getFromCache(registers.getMAR()));
        int tableAddress = registers.getMBR();

        registers.setMAR(trapCode + tableAddress);
        registers.setMBR(mcu.getFromCache(registers.getMAR()));
        int routine = registers.getMBR();
        registers.setPC(routine);

        do {
            registers.setMAR(registers.getPC());
            registers.setMBR(mcu.getFromCache(registers.getMAR()));
            registers.setIR(registers.getMBR());

            cpu.decodeAndExecute();

        } while (registers.getIR() != 0);

        // return to the instruction
        registers.setMAR(2);
        registers.setMBR(mcu.getFromCache(registers.getMAR()));
        registers.setPC(registers.getMBR());

        return ExecutionResult.CONTINUE;
    }
}
