package com.csci6461.team13.simulator.core.io;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.LinkedList;

/**
 * @author zhiyuan
 */
public abstract class Input extends Device {

    public BooleanProperty waitingForInput = new SimpleBooleanProperty(false);

    public Input(int devId, String name) {
        super(devId, name);
        inputBuffer = new LinkedList<>();
    }

    private final LinkedList<Integer> inputBuffer;

    public final synchronized Integer read() {
        return inputBuffer.pollLast();
    }

    public final synchronized boolean write(int character) {
        inputBuffer.offer(character);
        return true;
    }

    public synchronized boolean flush(){
        inputBuffer.clear();
        return true;
    }
}
