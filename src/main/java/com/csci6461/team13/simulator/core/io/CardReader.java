package com.csci6461.team13.simulator.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * this device reads all bytes from a file
 *
 * @author zhiyuan
 */
public class CardReader extends Input {

    public CardReader(int devId, String name) {
        super(devId, name);
    }

    public synchronized boolean write(File file) throws IOException {
        String string = "";
        if (file.canRead()) {
            byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            string = new String(bytes);
        }
        return this.write(string.toCharArray());
    }
}
