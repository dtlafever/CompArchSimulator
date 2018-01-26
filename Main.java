/**
 * Main
 */
public class Main {

    //----------------------
    //      REGISTERS 
    //----------------------

    //12 bits
    private static char  PC = 0;

    //4 bits
    private static char CC  = 0; //Condition Code: set when arithmetic/logical operations are executed; it has four 1-bit elements: overflow, underflow, division by zero, equal-or-not. They may be referenced as cc(0), cc(1), cc(2), cc(3). Or by the names OVERFLOW, UNDERFLOW, DIVZERO, EQUALORNOT
    private static char MFR = 0; //Machine Fault Register: contains the ID code if a machine fault after it occurs
    
    //16 bits
    private static char MAR = 0; //current address to fetch
    private static char MBR = 0; //Memory Buffer Register: holds the word just fetched from or the word to be /last stored into memory
    private static char IR  = 0; //Instruction Register: holds the instruction to be executed
    private static char MSR = 0; //Machine Status Register: certain bits record the status of the health of the machine
    //General Purpose Registers
    private static char R0  = 0;
    private static char R1  = 0;
    private static char R2  = 0;
    private static char R3  = 0;
    //Index Register: contains a base address that supports base register addressing of memory.
    private static char X1  = 0;
    private static char X2  = 0;
    private static char X3  = 0;

    //PRE: a defined object and the index(opcode) of the register
    //POST: returns the value of the register
    private static char getRegister(int index){

    }
    
    //PRE: a defined object and the name of the register
    //POST: returns the value of the register
    private static char getRegister(String reg){

    }
    
    //PRE: a defined object
    //POST: returns the PC value
    private static char getPC(){

    }
    
    //PRE: a defined object and the index(opcode) of the register,
    //     as well as the value to set.
    //POST: sets the register to the value.
    private static void setRegister(int index, char value){

    }
    
    //PRE: a defined object and the name of the register,
    //     as well as the value to set.
    //POST: sets the register to the value.
    private static void setRegister(String reg, char value){

    }

    //--------------------------
    //     FETCH/DECODE
    //--------------------------

    public initMachine(){

    }

    //Grab the instruction at the PC location
    public static void fetch(){
        MAR = PC;
        MBR = MEMORY[MAR];
        CYCLE = CYCLE + 1;
        IR = MBR;
    }

    //Decode the current instruction. Will do a
    // Load / Store,
    // Branch,
    // XEQ Instruction
    public static void decode(){

    }

    //public static void operandFetch(){
    //
    //}

    public static void execute(){

    }

    //public static void resultStore(){
    //
    //}
    
    // Determine next instruction, usually incrementing PC
    // but dont if we branched (maybe)
    //public static void nextInstruction(){
    //    
    //}

    //--------------------------
    //     ASSEMBLY COMMANDS
    //--------------------------

    

    ///////////////////////////////////////////

    public static void main(String[] args){
        System.out.println("I'm a Simple Program");

        //reset registers, start everything up
        initMachine();

        //Fetch, decode, execute
        while (true) {
            // Main Cycle
            fetch();
            decode();
            // Not always run
            //operandFetch();
            execute();
            //resultStore();
            PC = PC + 1;
            //nextInstruction();
        }
      }
}