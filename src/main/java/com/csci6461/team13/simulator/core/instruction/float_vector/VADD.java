package com.csci6461.team13.simulator.core.instruction.float_vector;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.MCU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.CoreUtil;
import com.csci6461.team13.simulator.util.Register;

public class VADD extends Instruction {

    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        MCU mcu = cpu.getMcu();
        int fr = getR();
        int ix = getIx();
        int i = getI();
        int addrV1 = getAddress();
        int addrV2 = addrV1 + 1;

        int effAddrV1 = calculateEA(ix, addrV1, i, mcu, registers);
        int effAddrV2 = calculateEA(ix, addrV2, i, mcu, registers);
        fr = 3;

        for (int k = 0; i < fr; k++){
            registers.setMAR(effAddrV1 + k);
            registers.setMBR(mcu.getFromCache(registers.getMAR()));
            int v1 = registers.getMBR();

            registers.setMAR(effAddrV2 + i);
            registers.setMBR(mcu.getFromCache(registers.getMAR()));
            int v2 = registers.getMBR();

            int result = v1 + v2;

            //check overflow
            int MAX_VALUE = 2^ 6;
            int MIN_VALUE = -2 ^ 6 - 1;
            if(result > MAX_VALUE || result < MIN_VALUE){
                registers.setCCByBit(Const.ConditionCode.OVERFLOW.getValue(), true);
            }else{
                registers.setMBR(result);
                registers.setMAR(effAddrV1 + i);
                mcu.storeToCache(registers.getMAR(), result);
            }

        }

        return ExecutionResult.CONTINUE;
    }

    public static int calculateEA(int ix, int address, int i, MCU mcu, Registers registers){
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