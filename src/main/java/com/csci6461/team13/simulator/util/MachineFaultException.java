package com.csci6461.team13.simulator.util;

public class MachineFaultException extends Exception {

    private int faultCode;
    private String message;

    public MachineFaultException(int faultCode, String message) {
        this.faultCode = faultCode;
        this.message = message;
    }

    public MachineFaultException(int faultCode) {
        this.faultCode = faultCode;
    }

    public int getFaultCode() {
        return this.faultCode;
    }

    public String getMessage() {
        return this.message;
    }


}
