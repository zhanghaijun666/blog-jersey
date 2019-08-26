package com.tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhanghaijun
 */
public class ByteSizeUtil {

    final static Map<String, Long> byteUnits = new HashMap<String, Long>() {
        {
            put("BYTE", 1024l);
            put("K", 1024l);
            put("KB", 1024l);
            put("M", 1024l * 1024l);
            put("MB", 1024l * 1024l);
            put("G", 1024l * 1024l * 1024l);
            put("GB", 1024l * 1024l * 1024l);
            put("T", 1024l * 1024l * 1024l * 1024l);
            put("TB", 1024l * 1024l * 1024l * 1024l);
        }
    };

    private static final float KB = 1024F;
    private static final float MB = 1024 * 1024F;
    private static final float GB = 1024 * 1024 * 1024F;
    private static final float TB = 1024 * 1024 * 1024F;
    private static final DecimalFormat formater = new DecimalFormat(".00");

    public static String sizeFormat(long size) {
        if (size < KB) {
            return size + "Byte";
        } else if (size < MB) {
            return formater.format(size / KB) + "KB";
        } else if (size < GB) {
            return formater.format(size / MB) + "MB";
        } else if (size < TB) {
            return formater.format(size / GB) + "GB";
        } else {
            return formater.format(size / TB) + "TB";
        }
    }

    public static long parseVolume(String value) {
        if (value == null) {
            return 0l;
        }
        Pattern pattern = Pattern.compile("^\\s*(?<num>\\d+(\\.\\d+)?)\\s*(?<unit>[A-Za-z]+)?\\s*$");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            float num = BasicConvertUtils.toFloat(matcher.group("num"), 0);
            String unit = matcher.group("unit");
            return BasicConvertUtils.toLong(byteUnits.keySet().contains(unit) ? num * byteUnits.get(unit.toUpperCase()) : num, 0);
        }
        return 0l;
    }

}
