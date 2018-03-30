package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.TestPrograms;
import com.csci6461.team13.simulator.ui.basic.Program;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        return readBinaryProgram(file.getAbsolutePath());
    }

    public static Program readBinaryProgram(Path path) throws IOException {
        String programString = "";
        byte[] bytes = Files.readAllBytes(path);

        if (path.endsWith(".txt")) {
            programString = new String(bytes);
        }

        if (path.endsWith(".bin")) {
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(String.valueOf(aByte));
            }
            programString = builder.toString();
        }
        return Program.fromBinaryString(programString);
    }

    public static Program readBinaryProgram(String path) throws IOException {
        return readBinaryProgram(Paths.get(path));
    }

    /**
     * create two files of the programs in the desktops
     * these files are binary format of programs
     */
    public static void exportToDesktop() {
        Program programOne = TestPrograms.getOne();
        Program programTwo = TestPrograms.getTwo();
        try {
            String userHome = System.getProperty("user.home");
            String targetFolder = String.format("%s%s", userHome, "/Desktop");
            String onePath = String.format("%s%s", targetFolder, "/one");
            String twoPath = String.format("%s%s", targetFolder, "/two");
            Files.write(Paths.get(onePath + ".bin"), programOne.getBinaryFormat());
            Files.write(Paths.get(onePath + ".txt"), programOne.getBinaryTextFormat());
            Files.write(Paths.get(twoPath + ".bin"), programTwo.getBinaryFormat());
            Files.write(Paths.get(twoPath + ".txt"), programTwo.getBinaryTextFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        exportToDesktop();
    }
}
