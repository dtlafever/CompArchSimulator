package com.csci6461.team13.simulator.util;

public enum Register {
    PC(12),
    IR(16),
    MAR(16),
    MBR(16),
    MSR(16),
    CC(4),
    MFR(4),
    R0(16),
    R1(16),
    R2(16),
    R3(16),
    X1(16),
    X2(16),
    X3(16);

    private int bitLength;

    Register(int bitLength) {
        this.bitLength = bitLength;
    }

    public int getBitLength() {
        return this.bitLength;
    }

}
