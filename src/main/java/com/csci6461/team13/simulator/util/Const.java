package com.csci6461.team13.simulator.util;

import javafx.beans.binding.StringBinding;

public class Const {

    private Const() {
    }

    // The initial memory address to start our pre loaded program
    public static int ROM_ADDR = 100;

    // Cache
    public static final int MAX_CACHE_LINES = 16;

    // Memory
    public static final int MAX_MEMORY = 2048;
    public static final int EXPANDED_MAX_MEMORY = 4096;

    public final static String DRACULA_THEME_URL = "static/dracula.css";
    public final static String BOOTSTRAP3_THEME_URL = "static/bootstrap3.css";
    public static StringBinding UNIVERSAL_STYLESHEET_URL;
    public static String ICON_URL = "static/icon_x.jpg";

}