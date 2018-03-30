package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.TestPrograms;
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
        String programString = new String(bytes);
        return Program.fromBinaryString(programString);
    }

    public static Program readBinaryProgram(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String programString = new String(bytes);
        return Program.fromBinaryString(programString);
    }

    /**
     * create two files of the programs in the desktops
     * these files are binary format of programs
     * */
    public static void exportToDesktop() {
        Program programOne = TestPrograms.getOne();
        Program programTwo = TestPrograms.getTwo();
        System.out.println("-----WRITE-----");
        System.out.println(programOne.toString());
        System.out.println(programTwo.toString());
        try {
            String userHome = System.getProperty("user.home");
            String targetFoleder = String.format("%s%s",userHome, "/Desktop");
            String onePath = String.format("%s%s", targetFoleder, "/one.prog");
            String twoPath = String.format("%s%s", targetFoleder, "/two.prog");
            Files.write(Paths.get(onePath), programOne.getBinaryBytesFormat());
            Files.write(Paths.get(twoPath), programTwo.getBinaryBytesFormat());

            Program tempOne = ProgramUtil.readBinaryProgram(onePath);
            Program tempTwo = ProgramUtil.readBinaryProgram(twoPath);

            System.out.println("-----READ-----");
            System.out.println(tempOne.toString());
            System.out.println(tempTwo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
