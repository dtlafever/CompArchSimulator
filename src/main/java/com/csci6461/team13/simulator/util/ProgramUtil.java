package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.TestPrograms;
import com.csci6461.team13.simulator.ui.basic.Program;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhiyuan
 */
public class ProgramUtil {

    public static Program readBinaryProgram(File file) throws IOException {
        return readBinaryProgram(file.getAbsolutePath());
    }

    private static Program readBinaryProgram(Path path) throws IOException {
        String binaryString = "";
        byte[] bytes = Files.readAllBytes(path);
        String name = path.getFileName().toString();
        int i = name.lastIndexOf('.');
        String ext = i > 0 ? name.substring(i + 1) : "";

        if (ext.equals("txt")) {
            binaryString = new String(bytes);
        }

        if (ext.equals("bin")) {
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(String.valueOf(aByte));
            }
            binaryString = builder.toString();
        }

        if (binaryString.equals("")) {
            return null;
        }
        return Program.fromBinaryString(binaryString);
    }

    public static Program readBinaryProgram(String path) throws IOException {
        return readBinaryProgram(Paths.get(path));
    }

    /**
     * create files of the programs in the desktops
     */
    public static void exportToDesktop() {
        Program programOne = TestPrograms.getOne();
        Program programTwo = TestPrograms.getTwo();
        Program programThree = TestPrograms.getThree();
        try {
            String userHome = System.getProperty("user.home");
            String targetFolder = String.format("%s%s", userHome, "/Desktop/progs");
            Path folderPath = Paths.get(targetFolder);
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
            }
            String onePath = String.format("%s%s", targetFolder, "/one");
            String twoPath = String.format("%s%s", targetFolder, "/two");
            String threePath = String.format("%s%s", targetFolder, "/three");
            String twoParaPath = String.format("%s%s", targetFolder,
                    "/paragraph");
            Files.write(Paths.get(onePath + ".bin"), programOne.getBinaryFormat());
            Files.write(Paths.get(onePath + ".txt"), programOne.getBinaryTextFormat());
            Files.write(Paths.get(twoPath + ".bin"), programTwo.getBinaryFormat());
            Files.write(Paths.get(twoPath + ".txt"), programTwo.getBinaryTextFormat());
            Files.write(Paths.get(twoParaPath + ".txt"), Const.PROG_2_PARAGRAPH.getBytes());
            Files.write(Paths.get(threePath + ".bin"), programThree.getBinaryFormat());
            Files.write(Paths.get(threePath + ".txt"), programThree.getBinaryTextFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        exportToDesktop();
    }
}
