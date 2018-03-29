package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.ui.basic.Program;
import com.google.gson.Gson;

/**
 * @author zhiyuan
 */
public class ProgramUtil {

    public static Gson gson;

    static {
        gson = new Gson();
    }

    public static Program readBinaryProgram() {
        Program program = new Program();

        return program;
    }

    public static void writeBinaryProgram(Program program) {
        String originStr = program.toString();

    }
}
