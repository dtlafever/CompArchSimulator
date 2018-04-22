package com.csci6461.team13.simulator.core;

import com.csci6461.team13.simulator.core.Cache.CacheLine;
import com.csci6461.team13.simulator.util.Const;
import com.csci6461.team13.simulator.util.MachineFaultException;

import java.util.ArrayList;

/**
 * Memory Control Unit
 * Reserved Locations of Memory:
 * 0 - Reserved for the Trap instruction for Part III.
 * 1 - Reserved for a machine fault
 * 2 - Store PC for Trap
 * 3 - Not Used
 * 4 - Store PC for Machine Fault
 * 5 - Not Used
 */
public class MCU {
    // 16 bit words, so be careful
    private ArrayList<Integer> memory = null;

    // 16 blocks of cache
    private Cache cache;

    // initialize all memory to 0 with a size of 2048
    public MCU() {
        reset();
    }

    // Expand memory to 4096
    public void expandMemorySize() {
        if (this.memory != null && this.memory.size() > 0) {
            this.memory.ensureCapacity(Const.EXPANDED_MAX_MEMORY);
            for (int curSize = memory.size(); curSize < Const.EXPANDED_MAX_MEMORY; curSize++) {
                this.memory.add(0);
            }
        }
    }

    // get the current size of memory
    public int getMemSize() {
        if (this.memory != null) {
            return this.memory.size();
        } else {
            this.memory = new ArrayList<>();
            return this.memory.size();
        }
    }

    /**
     * find a empty block, return the start address
     */
    public int getBlockStart(int blockSize) {
        return 0;
    }

    // return the word from memory at a particular address
    public int getWord(int addr) throws MachineFaultException {
        if (addr >= this.memory.size()) {
            throw new MachineFaultException(Const.FaultCode.ILL_MEM_BYD.getValue(), Const.FaultCode.ILL_MEM_BYD.getMessage());
        }
        return this.memory.get(addr);
    }

    // setup a word in memory to a particular value
    public void storeWord(int addr, int value) throws MachineFaultException {
        if (this.memory != null) {
            if (addr >= this.memory.size()) {
                expandMemorySize();
            }
            if (addr >= this.memory.size()) {
                throw new MachineFaultException(Const.FaultCode.ILL_MEM_BYD.getValue(), Const.FaultCode.ILL_MEM_BYD.getMessage());
            }
            this.memory.set(addr, value);
        }
    }

    public void reset() {
        if (this.memory == null) {
            this.memory = new ArrayList<>(Const.MAX_MEMORY);
            for (int i = 0; i < Const.MAX_MEMORY; i++) {
                this.memory.add(i, 0);
            }
        } else {
            for (int i = 0; i < this.memory.size(); i++) {
                this.memory.set(i, 0);
            }
        }
        this.cache = new Cache();
    }

    public Cache getCache() {
        return this.cache;
    }

    // Checks to see if the address exists in cache and if so, return the value.
    // Otherwise, fetch it from memory and store to cache.
    public int getFromCache(int addr) throws MachineFaultException {
        // Let us first see if the block is already in cache
        for (CacheLine line : cache.getCacheLines()) {
            if (addr == line.getAddr()) {
                return line.getData();
            }
        }
        // The address does not exist, lets add it to the cache and fetch
        // a word from memory
        int value = getWord(addr);
        cache.add(addr, value);
        return value;
    }

    // store into cache and memory, replacing values in cache if they already exists.
    public void storeToCache(int addr, int value) throws MachineFaultException {
        storeWord(addr, value);

        // check if block exists already
        for (CacheLine line : cache.getCacheLines()) {
            if (addr == line.getAddr()) {
                // We should replace the block
                line.setData(value);
                return;
            }
        }
        // The address does not exist, add to cache
        cache.add(addr, value);
    }
}