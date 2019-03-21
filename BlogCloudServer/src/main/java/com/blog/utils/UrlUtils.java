package com.blog.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author zhanghaijun
 */
public class UrlUtils {

    /**
     * @param str 文件名
     * @param enc StandardCharsets.UTF_8
     * @return
     */
    public static String urlEncode(String str, Charset enc) {
        try {
            //URLEncoder.encode replace whitespace to +, but it should be %20 for best match protocol
            return URLEncoder.encode(str, enc.name()).replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {
            return str;
        }
    }
}
