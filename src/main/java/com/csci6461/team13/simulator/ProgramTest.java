package com.csci6461.team13.simulator;

import com.csci6461.team13.simulator.ui.basic.Program;
import com.csci6461.team13.simulator.util.Const;

import java.util.Random;

/**
 * @author zhiyuan
 */
public class ProgramTest {

    public static void main(String[] args) {
        for (int i = 0; i < 21; i++) {
            System.out.print(Math.abs(new Random().nextInt(200))+" ");
        }
        System.out.println();
        Program program = TestPrograms.getTwo();
        System.out.println(program.toString());
        String string = program.toFixedLenBinaryString(Const.CPU_BIT_LENGTH);
        Program program1 = Program.fromBinaryString(string);
        System.out.println(program1.getDescription());
        System.out.println(string);
    }
}
