package com.csci6461.team13.simulator.ui.basic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author zhiyuan
 */
public class CacheRow {

    private final StringProperty addr;
    private final StringProperty data;

    public CacheRow(){
        this.addr = null;
        this.data = null;
    }

    public CacheRow(String addr, String data) {
        this.addr = new SimpleStringProperty(addr);
        this.data = new SimpleStringProperty(data);
    }

    public String getAddr() {
        return addr.get();
    }

    public StringProperty addrProperty() {
        return addr;
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }

    public void setAddr(String addr) {
        this.addr.set(addr);
    }

    public void setData(String data) {
        this.data.set(data);
    }
}
