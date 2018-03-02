package com.csci6461.team13.simulator.core.instruction.io;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Keyboard;
import com.csci6461.team13.simulator.core.io.Printer;
import com.csci6461.team13.simulator.util.CoreUtil;

import java.util.List;


/**
 * @author zhiyuan
 */
public class OUT extends Instruction {
    @Override
    public ExecutionResult execute(CPU cpu) {
        Registers registers = cpu.getRegisters();
        List<Device> devices = cpu.getDevices();
        int devId = this.getAddress();
        Device device = CoreUtil.findDevice(devices, devId);
        if (device != null && device instanceof Printer) {
            int value = registers.getR(this.getR());
            ((Printer) device).append(value);
            return ExecutionResult.CONTINUE;
        } else {
            return ExecutionResult.RETRY;
        }
    }
}
