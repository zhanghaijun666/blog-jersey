package com.utils;

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

}
