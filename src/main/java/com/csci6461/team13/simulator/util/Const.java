package com.csci6461.team13.simulator.util;

import javafx.beans.binding.StringBinding;

public class Const {

    private Const() {
    }

    // The initial memory address to start our pre loaded program
    public static int ROM_ADDR = 10;

    // Cache
    public static final int MAX_CACHE_LINES = 16;

    // Memory
    public static final int MAX_MEMORY = 2048;
    public static final int EXPANDED_MAX_MEMORY = 4096;

    public final static String DRACULA_THEME_URL = "static/dracula.css";
    public final static String BOOTSTRAP3_THEME_URL = "static/bootstrap3.css";
    public static StringBinding UNIVERSAL_STYLESHEET_URL;
    public static String ICON_URL = "static/icon_x.jpg";

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

}