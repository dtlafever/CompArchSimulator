package com.csci6461.team13.simulator.util;
import java.util.HashMap;

public class Const{
    // Hashmap for quick retrevial of Opcodes
    public static final HashMap<Integer, String> OPCODE = new HashMap<Integer, String>();
	static {
		OPCODE.put(0, "HALT");
        OPCODE.put(1, "LDR");
        OPCODE.put(2, "STR");
		OPCODE.put(3, "LDA");
		OPCODE.put(41, "LDX");
		OPCODE.put(42, "STX");
	}
}