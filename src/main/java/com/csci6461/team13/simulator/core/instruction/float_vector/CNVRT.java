package com.csci6461.team13.simulator.core.instruction.float_vector;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;
import com.csci6461.team13.simulator.util.MachineFaultException;
import com.csci6461.team13.simulator.util.Register;

public class CNVRT extends Instruction {

    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int r = getR();
        int ix = getIx();
        int i = getI();
        int address = getAddress();

        int effAddress = calculateEA(ix, address, i, mcu, registers);
        
        int F = registers.getR(r);

        registers.setMAR(effAddress);
        registers.setMBR(mcu.getFromCache(registers.getMAR()));

        if (F == 0){
            registers.setR(r, Math.round(registers.getMBR()));
        }
        if (F == 1){
            registers.setConvertFRByNum(0, registers.getMBR());
        }

        return ExecutionResult.CONTINUE;
    }

    public static int calculateEA(int ix, int address, int i, MCU mcu, Registers registers) throws MachineFaultException {
		if (i == 0) {
            // NO indirect addressing
            if (ix == 0) {
                return address;
            } else {
                return address + registers.getX(ix);
            }
        } else if (i == 1) {
            // indirect addressing, but NO indexing
            if (ix == 0) {
                registers.setMAR(address);
                registers.setMBR(mcu.getFromCache(registers.getMAR()));
            } else {
                registers.setMAR(address + registers.getX(ix));
                registers.setMBR(mcu.getFromCache(registers.getMAR()));
            }
            return registers.getMBR();
        }
        return 0;
    }

}