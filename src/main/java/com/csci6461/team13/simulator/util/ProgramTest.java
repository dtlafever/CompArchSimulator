package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.ui.basic.Program;

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
        Program program = ROM.one;
        String string = program.toFixedLenBinaryString(16);
        System.out.print(string);
    }
}
