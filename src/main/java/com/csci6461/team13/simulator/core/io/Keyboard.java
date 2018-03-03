package com.csci6461.team13.simulator.core.io;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author zhiyuan
 */
public class Keyboard extends Input {
    private final StringProperty buffer;

    public Keyboard(int devId, String name) {
        super(devId, name);
        buffer = new SimpleStringProperty("");
        // read one integer value, ends with a new line
        buffer.addListener((observable, oldValue, newValue) -> {
            char[] characters = newValue.toCharArray();
            replaceWith(characters);
        });
    }

    public static void main(String[] args) {
        Keyboard keyboard = new Keyboard(1, "2");
        keyboard.buffer.set("12 13 14 1232 12352");
        System.out.println(keyboard.buffer.toString());
    }

    @Override
    public final synchronized Character read() {
        Character character = super.read();
        String newValue = getBufferedString();
        buffer.set(newValue);
        return character;
    }

    @Override
    public synchronized boolean flush() {
        buffer.set("");
        return super.flush();
    }

    public synchronized void enable() {
        waitingForInput.set(true);
    }

    public synchronized void disable() {
        waitingForInput.set(false);
    }

    public synchronized String getBuffer() {
        return buffer.get();
    }

    public synchronized StringProperty bufferProperty() {
        return buffer;
    }
}
