package com.csci6461.team13.simulator.core.io;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Pattern pattern = Pattern.compile("^(\\d{1,5})(\\s*\n)");
            Matcher matcher = pattern.matcher(newValue);
            if (matcher.matches()) {
                String valStr = matcher.group().trim();
                System.out.println(valStr);
                Integer value = Integer.valueOf(valStr);
                write(value);
                buffer.set("");
                disable();
            }
        });
    }

    public static void main(String[] args) {
        Keyboard keyboard = new Keyboard(1, "2");
        keyboard.buffer.set("12 13 14 1232 12352");
    }

    @Override
    public synchronized boolean flush() {
        buffer.set("");
        return super.flush();
    }

    public synchronized void enable(){
        waitingForInput.set(true);
    }

    public synchronized void disable(){
        waitingForInput.set(false);
    }

    public String getBuffer() {
        return buffer.get();
    }

    public StringProperty bufferProperty() {
        return buffer;
    }
}
