package com.csci6461.team13.simulator.ui.helpers;

public class RegisterEditHelper {

    private RegisterEditHelper(){}

    public static boolean[] registerValueToBooleans(int value) {
        StringBuilder builder = new StringBuilder(Integer.toBinaryString
                (value));
        String binaryString = builder.reverse().toString();
        boolean[] booleans = new boolean[16];
        for (int i = 0; i < binaryString.length(); i++) {
            booleans[i] = binaryString.charAt(i) == '1';
        }
        return booleans;
    }

    public static int registerBooleansToValue(boolean[] booleans) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < booleans.length; i++) {
            builder.append(booleans[i] ? '1' : '0');
        }
        String binaryString = builder.reverse().toString();
        int sum = Integer.parseInt(binaryString, 2);
        return sum;
    }
}
