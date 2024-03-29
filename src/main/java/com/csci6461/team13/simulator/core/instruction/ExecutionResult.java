package com.csci6461.team13.simulator.core.instruction;

/**
 * @author zhiyuan
 */
public enum ExecutionResult {

    /**
     * continue next execution
     */
    CONTINUE,

    /**
     * unsuccessful execution
     * pause on current instruction
     * until it has been successfully executed
     */
    RETRY,

    /**
     * terminate current program
     */
    HALT;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
