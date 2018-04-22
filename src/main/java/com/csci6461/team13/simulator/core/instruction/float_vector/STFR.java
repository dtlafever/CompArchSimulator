package com.csci6461.team13.simulator.core.instruction.float_vector;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.ui.controllers.RegisterEditPanelController;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;
import com.csci6461.team13.simulator.util.MachineFaultException;
import com.csci6461.team13.simulator.util.Register;

public class STFR extends Instruction {

    @Override
    public ExecutionResult execute(CPU cpu) throws MachineFaultException {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int fr = getR();
        int ix = getIx();
        int i = getI();
        int address = getAddress();

        int effAddress = calculateEA(ix, address, i, mcu, registers);
        
        int cfr=registers.getFR(fr);
		String buffer="0000000000000000";
		String frs=Integer.toBinaryString(cfr);
		if(frs.length()<16)
		frs=buffer.substring(0, 16-frs.length())+frs;
		
		int man=Integer.parseInt(frs.substring(8, 16),2);
		int exp=Integer.parseInt(frs.substring(0, 8),2);

		registers.setMAR(effAddress);
		registers.setMBR(exp);
		mcu.storeToCache(registers.getMAR(), registers.getMBR());

		registers.setMAR(effAddress + 1);
		registers.setMBR(man);
		mcu.storeToCache(registers.getMAR(), registers.getMBR());

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