package com.csci6461.team13.simulator.core;

// NOTE: we are using integers for each of the registers.
//       This allows us to use signed if we need it, otherwise
//       just act like it is unsigned since nothing in the registers
//       are greater than 16 bits.

import com.csci6461.team13.simulator.util.Register;

public class Registers {
    //----------------------
    //      REGISTERS 
    //----------------------

    //--- 12 bits ---

    // The register that holds the next address 
    // to be processed for execution
    static int PC;

    //--- 4 bits ---

    // Condition Code: setup when arithmetic/logical
    // operations are executed;
    // overflow   = 0
    // underflow  = 1
    // DivZero    = 2
    // EqualOrNot = 3
    static int CC;

    // Machine Fault Register: contains the ID code if
    // a machine fault after it occurs
    // Illegal Memory Address to Reserved Locations = 0
    // Illegal TRAP code                            = 1
    // Illegal Operation Code                       = 2
    // Illegal Memory Address beyond 2048 (memory)  = 3
    static int MFR;

    //--- 16 bits ---

    // current address to fetch
    static int MAR;

    // Memory Buffer Register: holds the word just fetched 
    // from or the word to be /last stored into memory
    static int MBR;

    //Instruction Register: holds the instruction 
    // to be executed
    static int IR;

    // Machine Status Register: certain bits
    // record the status of the health of the machine
    static int MSR;

    // General Purpose Registers
    static int R0;
    static int R1;
    static int R2;
    static int R3;

    // Index Register: contains a base address that
    // supports base register addressing of memory.
    static int X1;
    static int X2;
    static int X3;

    //----------------------
    //      CONSTANTS 
    //----------------------

    public int OVERFLOW = 0;
    public int UNDERFLOW = 1;
    public int DIVZERO = 2;
    public int EQUALORNOT = 3;

    //----------------------
    //    BASIC FUNCTIONS
    //----------------------

    // Default Constructor
    public Registers() {
        this.CC = 0;
        this.IR = 0;
        this.MAR = 0;
        this.MBR = 0;
        this.MFR = 0;
        this.MSR = 0;
        this.PC = 0;
        this.R0 = 0;
        this.R1 = 0;
        this.R2 = 0;
        this.R3 = 0;
        this.X1 = 0;
        this.X2 = 0;
        this.X3 = 0;
    }

    // Set all registers to 0
    public void resetRegisters() {
        this.CC = 0;
        this.IR = 0;
        this.MAR = 0;
        this.MBR = 0;
        this.MFR = 0;
        this.MSR = 0;
        this.PC = 0;
        this.R0 = 0;
        this.R1 = 0;
        this.R2 = 0;
        this.R3 = 0;
        this.X1 = 0;
        this.X2 = 0;
        this.X3 = 0;
    }

    //----------------------
    //      GETTERS/SETTERS
    //----------------------

    // Returns the bit length of the register
    public int getBitLength(String regName){
        return Register.valueOf(regName).getBitLength();
    }

    public int getCC() {
        return this.CC;
    }

    // Returns true if the specified bit is setup to true
    // OVERFLOW   = 0
    // UNDERFLOW  = 1
    // DIVZERO    = 2
    // EQUALORNOT = 3
    public boolean getCCByBit(int bit) {
        return ((this.CC & (1 << bit)) != 0);
    }

    public void setCC(int cc) {
        this.CC = cc;
    }

    // Set CC to a certain bit
    // OVERFLOW   = 0
    // UNDERFLOW  = 1
    // DIVZERO    = 2
    // EQUALORNOT = 3
    public void setCCByBit(int bit, boolean flag) {
        if (flag) {
            this.CC = (this.CC | (1 << bit));
        } else {
            int mask = ~(1 << bit);
            this.CC = this.CC & mask;
        }
    }

    public int getIR() {
        return this.IR;
    }

    public void setIR(int ir) {
        this.IR = ir;
    }

    public int getMAR() {
        return this.MAR;
    }

    public void setMAR(int mar) {
        this.MAR = mar;
    }

    public int getMBR() {
        return this.MBR;
    }

    public void setMBR(int mbr) {
        this.MBR = mbr;
    }

    public int getMFR() {
        return this.MFR;
    }

    public void setMFR(int mfr) {
        this.MFR = mfr;
    }

    public int getMSR() {
        return this.MSR;
    }

    public void setMSR(int msr) {
        this.MSR = msr;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int pc) {
        this.PC = pc;
    }

    public void incrementPC() {
        this.PC++;
    }

    public int getR0() {
        return this.R0;
    }

    public void setR0(int r0) {
        this.R0 = r0;
    }

    public int getR1() {
        return this.R1;
    }

    public void setR1(int r1) {
        this.R1 = r1;
    }

    public int getR2() {
        return this.R2;
    }

    public void setR2(int r2) {
        this.R2 = r2;
    }

    public int getR3() {
        return this.R3;
    }

    public void setR3(int r3) {
        this.R3 = r3;
    }

    public void setR(int num, int r) {
        if (num == 0)
            this.R0 = r;
        if (num == 1)
            this.R1 = r;
        if (num == 2)
            this.R2 = r;
        if (num == 3)
            this.R3 = r;
    }

    public int getR(int num) {
        if (num == 0)
            return this.R0;
        if (num == 1)
            return this.R1;
        if (num == 2)
            return this.R2;
        if (num == 3)
            return this.R3;
        return 0;
    }

    public int getX1() {
        return this.X1;
    }

    public void setX1(int x1) {
        this.X1 = x1;
    }

    public int getX2() {
        return this.X2;
    }

    public void setX2(int x2) {
        this.X2 = x2;
    }

    public int getX3() {
        return this.X3;
    }

    public void setX3(int x3) {
        this.X3 = x3;
    }

    public int getX(int num) {
		if (num == 1)
			return this.X1;
		if (num == 2)
			return this.X2;
		if (num == 3)
			return this.X3;
		return 0;
	}

	public void setX(int num, int x) {
		if (num == 1)
			this.X1 = x;
		if (num == 2)
			this.X2 = x;
		if (num == 3)
			this.X3 = x;
	}
}