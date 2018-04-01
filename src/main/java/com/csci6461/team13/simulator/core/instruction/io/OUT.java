package com.csci6461.team13.simulator.core.instruction.io;

import com.csci6461.team13.simulator.core.CPU;
import com.csci6461.team13.simulator.core.Registers;
import com.csci6461.team13.simulator.core.instruction.ExecutionResult;
import com.csci6461.team13.simulator.core.instruction.Instruction;
import com.csci6461.team13.simulator.core.io.Device;
import com.csci6461.team13.simulator.core.io.Printer;
import com.csci6461.team13.simulator.util.CoreUtil;

import java.util.List;


/**
 * two output mode:
 * 0 -> print as standard character
 * 1 -> print as integer
 *
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
            int mode = this.getI();
            if (mode == 0) {
                ((Printer) device).appendAsChar(value);
            } else {
                if (mode == 1) {
                    ((Printer) device).appendAsNum(value);
                } else {
                    this.message = "Invalid Output Mode: Mode=" + mode;
                }
            }
            return ExecutionResult.CONTINUE;
        } else {
            this.message = "NO AVAILABLE PRINTER: DevId=" + devId;
            return ExecutionResult.HALT;
        }
    }
}
