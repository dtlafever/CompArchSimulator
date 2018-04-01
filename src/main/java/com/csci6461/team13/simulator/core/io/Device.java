package com.csci6461.team13.simulator.core.io;

/**
 * @author zhiyuan
 */
public abstract class Device {

    private int devId;
    private String name;
    Device(int devId, String name) {
        this.devId = devId;
        this.name = name;
    }

    public int getDevId() {
        return devId;
    }

    public String getName() {
        return name;
    }
}
