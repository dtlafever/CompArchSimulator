package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.util.Const;

import java.util.LinkedList;

public class Cache {
    private LinkedList<CacheLine> cacheLines;

    public Cache() {
        this.cacheLines = new LinkedList<CacheLine>();
    }

    public LinkedList<CacheLine> getCacheLines() {
        return this.cacheLines;
    }

    // add to the front of the list
    public void add(int addr, int value) {
        if (this.cacheLines.size() >= Const.MAX_CACHE_LINES) {
            this.cacheLines.removeLast();
        }
        this.cacheLines.addFirst(new CacheLine(addr, value));
    }

    public class CacheLine {
        int addr;
        int data;

        public CacheLine(int addr, int data) {
            this.addr = addr;
            this.data = data;
        }

        public int getAddr() {
            return this.addr;
        }

        public void setAddr(int addr) {
            this.addr = addr;
        }

        public int getData() {
            return this.data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }
}