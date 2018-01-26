import Registers;
import MCU;

/**
 * Main
 */
public class FrontPanel {

    // Initialize the registers and memory control unit
    private void initCPU(){
        registers = new Registers();
        mcu = new MCU();
    }

    public FrontPanel(){
        initCPU();
        //TODO: initialize UI
    }

    public initMachine(){
        initCPU();
        //TODO: initialize UI
    }

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    //NOTE: I dont know if we want this here, might want
    //      the fetch-decode-execute cycle in another class


    //Grab the instruction at the PC location
    public static void fetch(){
        registers.setMAR(registers.getPC());
        registers.setMBR(mcu.getWord(MAR));
        registers.incrementPC(); // we have a seperate adder for PC
        registers.setIR(registers.getMBR());
    }

    //Decode the current instruction. 
    public static void decode(){

    }

    public static void execute(){

    }

    ///////////////////////////////////////////

    public static void main(String[] args){
        System.out.println("Front Panel Started.");

        //reset registers, start everything up
        initMachine();

        //Fetch, decode, execute
        while (true) {
            // Main Cycle
            fetch();
            decode();
            execute();
            CYCLE = CYCLE + 1;
        }
      }
}