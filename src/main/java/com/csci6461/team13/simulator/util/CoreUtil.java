package com.csci6461.team13.simulator.util;

import com.csci6461.team13.simulator.core.io.Device;

import java.math.BigInteger;
import java.util.List;

public class CoreUtil {

    private CoreUtil() {

    }

    // convert a binary value to a decimal value
    public static int binaryToDecimal(int binary) {
        return new BigInteger(String.valueOf(binary), 2).intValue();
    }

    // int to bits parsers
    // sample results
    // [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0] <=> 100
    // [false, false, false, false, false, false, false, false, false, true, true, false, false, true, false, false] <=> 100
    public static boolean[] intToBooleans(int value) {
        byte[] bytes = intToBytes(value);
        boolean[] booleans = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            booleans[i] = bytes[i] == 1;
        }
        return booleans;
    }

    public static int booleansToInt(boolean[] booleans) {
        byte[] bytes = new byte[booleans.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = booleans[i] ? (byte) 1 : 0;
        }
        return bytesToInt(bytes);
    }

    public static byte[] intToBytes(int value) {
        String binaryString = Integer.toBinaryString(value);
        byte[] bytes = new byte[16];
        for (int i = 16 - binaryString.length(), j = 0; i < bytes.length; i++, j++) {
            bytes[i] = Byte.valueOf(String.valueOf(binaryString.charAt(j)));
        }
        return bytes;
    }

    public static int bytesToInt(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte bit : bytes) {
            builder.append(bit);
        }
        String binaryString = builder.toString();
        return Integer.parseInt(binaryString, 2);
    }

    public static String int2FixedLenStr(int value, int length) {
        StringBuilder strWord = new StringBuilder(Integer.toBinaryString(value));
        while (strWord.length() < length) {
            strWord.insert(0, "0");
        }

        return strWord.toString();
    }

    public static int maxOfBits(int length){
        StringBuilder strWord = new StringBuilder();
        while (strWord.length() < length) {
            strWord.insert(0, "1");
        }

        return Integer.parseInt(strWord.toString(), 2);
    }

    public static Device findDevice(List<Device> devices, int devId){

        for (Device device: devices){
            if(device.getDevId() == devId){
                return device;
            }
        }

        return null;
    }

}
