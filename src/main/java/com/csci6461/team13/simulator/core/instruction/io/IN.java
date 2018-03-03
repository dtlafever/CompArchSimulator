package com.csci6461.team13.simulator.core.instruction.io;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Input;
import com.csci6461.team13.simulator.util.CoreUtil;

import java.util.List;


/**
 * one execution of IN reads a single char from
 * @author zhiyuan
 */
public class IN extends Instruction {
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        List<Device> devices = cpu.getDevices();
        int devId = this.getAddress();
        Device device = CoreUtil.findDevice(devices, devId);
        if(device != null && device instanceof Input){
            // read one from input buffer
            Character value = ((Input) device).read();
            if(value != null){
                registers.setR(this.getR(), value);
                ((Input) device).waitingForInput.set(false);
                return ExecutionResult.CONTINUE;
            }else{
                // no available value in buffer
                ((Input) device).waitingForInput.set(true);
                return ExecutionResult.RETRY;
            }
        }else{
            // no such device
            return ExecutionResult.HALT;
        }
    }
}
