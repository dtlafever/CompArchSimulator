package com.csci6461.team13.simulator.core.io;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.LinkedList;

/**
 * @author zhiyuan
 */
public abstract class Input extends Device {

    private final LinkedList<Character> inputBuffer;
    public BooleanProperty waitingForInput = new SimpleBooleanProperty(false);

    public Input(int devId, String name) {
        super(devId, name);
        inputBuffer = new LinkedList<>();
    }

    /**
     * read one from buffer
     */
    public synchronized Character read() {
        return inputBuffer.pollFirst();
    }

    /**
     * put a character into buffer
     */
    public final synchronized boolean write(char character) {
        inputBuffer.offer(character);
        return true;
    }

    /**
     * put characters into buffer
     */
    public synchronized boolean write(char[] characters) {
        for (char character : characters) {
            inputBuffer.offer(character);
        }
        return true;
    }

    public synchronized boolean replaceWith(char[] characters) {
        inputBuffer.clear();
        write(characters);
        return true;
    }

    public synchronized String getBufferedString() {
        StringBuilder builder = new StringBuilder();
        for (char character : inputBuffer) {
            builder.append(character);
        }
        return builder.toString();
    }

    public synchronized boolean flush() {
        inputBuffer.clear();
        waitingForInput.set(false);
        return true;
    }
}
