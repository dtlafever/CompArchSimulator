package csci6461.team13.core;

public class CoreOperations {

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    // NOTE: I dont know if we want this here, might want
    // the fetch-decode-execute cycle in another class

    // Grab the instruction at the PC location
    public static void fetch(Registers registers, MCU mcu){
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getWord(registers.MAR));
        registers.incrementPC(); // we have a seperate adder for PC
        registers.setIR(registers.getMBR());
    }

    // Decode the current instruction.
    public static void decode(){

    }

    public static void execute(){

    }

}
