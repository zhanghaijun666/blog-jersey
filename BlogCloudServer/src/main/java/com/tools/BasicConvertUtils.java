package com.tools;

import org.javalite.common.Convert;

/**
 * @author zhanghaijun
 */
public class BasicConvertUtils {

    public static String toString(Object value, final String defaultValue) {
        return value == null ? defaultValue : Convert.toString(value);
    }

    public static boolean toBoolean(Object value) {
        return Convert.toBoolean(value);
    }

    public static double toDouble(Object value, final double defaultValue) {
        try {
            return Convert.toDouble(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static float toFloat(Object value, final float defaultValue) {
        return value == null ? defaultValue : Convert.toFloat(value);
    }

    public static long toLong(Object value, final long defaultValue) {
        try {
            return Convert.toLong(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static int toInteger(Object value, final Integer defaultValue) {
        try {
            return Convert.toInteger(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static short toShort(Object value, final short defaultValue) {
        try {
            return Convert.toShort(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * int到byte[] 由高位到低位
     *
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

}
