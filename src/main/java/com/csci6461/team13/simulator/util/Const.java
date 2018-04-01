package com.csci6461.team13.simulator.util;

import javafx.beans.binding.StringBinding;

public class Const {

    private Const() {
    }

    // The initial memory address to start our pre loaded program
    // must be larger than 31
    public static int PROGRAM_STORAGE_ADDR = 40;
    public static int PROG_ADDR_POINTER = 9;

    public static final int CPU_BIT_LENGTH = 16;
    public static final int BIN_PROGRAM_BIT_LEN = 8;

    // Cache
    public static final int MAX_CACHE_LINES = 16;

    // Memory
    public static final int MAX_MEMORY = 2048;
    public static final int EXPANDED_MAX_MEMORY = 4096;

    public final static String DRACULA_THEME_URL = "static/dracula.css";
    public final static String BOOTSTRAP3_THEME_URL = "static/bootstrap3.css";
    public static StringBinding UNIVERSAL_STYLESHEET_URL;
    public static String ICON_URL = "static/icon_x.jpg";

    // program 2 paragraph
    public final static String PROG_2_PARAGRAPH = "This is the first sentence." +
            " This is the first part of the second sentence, second part of the second sentence. Third sentence. Fourth sentence. Fifth sentence. Sixth sentence.";

    public final static int DEVICE_ID_KEYBOARD = 0;
    public final static int DEVICE_ID_PRINTER = 1;
    public final static int DEVICE_ID_CARD_READER = 2;

    public enum ConditionCode {
        OVERFLOW(0), UNDERFLOW(1), DIVZERO(2), EQUALORNOT(3);
        int value;

        private ConditionCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    
	public enum FaultCode {
        ILL_MEM_RSV(0, "Illegal Memory Address to Reserved Locations"), 
        ILL_TRPC(1, "Illegal TRAP"), 
        ILL_OPRC(2, "Illegal Operation"), 
        ILL_MEM_BYD(3, "Illegal Memory Address beyond max size");
		int value;
		String messsage;

		private FaultCode(int value, String message) {
			this.value = value;
			this.messsage = message;
		}

		public int getValue() {
			return this.value;
		}

		public String getMessage() {
			return this.messsage;
		}
	}

}