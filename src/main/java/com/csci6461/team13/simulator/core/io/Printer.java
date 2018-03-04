package com.csci6461.team13.simulator.core.io;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author zhiyuan
 */
public class Printer extends Device{

    /**
     * out put history for binding usage
     * */
    private StringProperty text;

    public Printer(int devId, String name) {
        super(devId, name);
        text = new SimpleStringProperty("");
    }

    public synchronized void appendAsChar(int character){
        text.set(text.get()+(char)character);
    }

    public synchronized void appendAsNum(int character){
        text.set(text.get()+character);
    }

    public synchronized void clear(){
        text.set("");
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public static void main(String[] args){
        Printer printer = new Printer(1, "printer");
        printer.appendAsChar(65);
        System.out.println(printer.text.get());
    }
}
