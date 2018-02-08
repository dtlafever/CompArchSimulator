package com.csci6461.team13.simulator.util;
import java.util.HashMap;

public class Const{
    // Hashmap for quick retrevial of Opcodes
    public static final HashMap<String, String> OPCODE = new HashMap<String, String>();
	static {
        OPCODE.put("000001", "LDR");
        OPCODE.put("000010", "STR");
		OPCODE.put("000011", "LDA");
		OPCODE.put("101001", "LDX");
		OPCODE.put("101010", "STX");
	}
}