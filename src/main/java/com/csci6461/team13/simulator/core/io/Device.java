package com.csci6461.team13.simulator.core.io;

/**
 * @author zhiyuan
 */
public abstract class Device {

    public Device(int devId, String name) {
        this.devId = devId;
        this.name = name;
    }

    private int devId;
    private String name;

    public int getDevId() {
        return devId;
    }

    public String getName() {
        return name;
    }
}
