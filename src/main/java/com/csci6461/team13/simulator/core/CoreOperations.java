package com.csci6461.team13.simulator.core;

import java.math.BigInteger;
import com.csci6461.team13.simulator.util.Const;

public class CoreOperations {

    // convert a binary value to a decimal value
	public int binaryToDecimal(int binary) {
		return new BigInteger(String.valueOf(binary), 2).intValue();
	}

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    // NOTE: I dont know if we want this here, might want
    // the fetch-decode-execute cycle in another class

    // Grab the instruction at the PC location
    public void fetch(Registers registers, MCU mcu) {
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a seperate adder for PC
        registers.setIR(registers.getMBR());
    }

    // Decode the current instruction and execute it.
    public void decodeAndExecute(String inst, Registers registers, MCU mcu) {
        //DECODE
        String opCode = inst.substring(0, 6);

        Instruction instruction = new Instruction();

        //TODO: clean up so it isn't using switch cases
        if (Const.OPCODE.containsKey(opCode)) {
            instruction = new Instruction(inst);
            int effectiveAddress = instruction.getEffectiveAddress(mcu, registers);

            //EXECUTE
            switch (Const.OPCODE.get(opCode)) {
                case "LDR":
                    registers.setMAR(effectiveAddress);
                    registers.setMBR(mcu.getWord(registers.getMAR()));
                    registers.setR(binaryToDecimal(instruction.getR()), registers.getMBR());
                    break;

                case "STR":
                    registers.setMAR(effectiveAddress);
                    registers.setMBR(registers.getR(instruction.getR()));
                    mcu.storeWord(registers.getMAR(), registers.getMBR());                    
                    break;
            
                case "LDA":
                    registers.setR(binaryToDecimal(instruction.getR()), effectiveAddress);
                    break;

                case "LDX":
                    registers.setMAR(effectiveAddress);
                    registers.setMBR(mcu.getWord(registers.getMAR()));
                    registers.setX(binaryToDecimal(instruction.getIx()), registers.getMBR());
                    break;

                case "SDX":
                    registers.setMAR(effectiveAddress);
                    registers.setMBR(registers.getX(instruction.getIx()));
                    mcu.storeWord(registers.getMAR(), registers.getMBR());
                    break;

                default:
                    break;
            }
        }else{
            //TODO: add machine fault if not a good instruction
        }
    }
}
