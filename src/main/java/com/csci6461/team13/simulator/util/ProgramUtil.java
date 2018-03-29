package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.ui.basic.Program;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhiyuan
 */
public class ProgramUtil {

    public static Gson gson;

    static {
        gson = new Gson();
    }

    public static Program readBinaryProgram(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String binaryString = new String(bytes);
        return Program.fromBinaryString(binaryString);
    }
}
