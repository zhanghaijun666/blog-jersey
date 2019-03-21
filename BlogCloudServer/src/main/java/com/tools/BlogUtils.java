package com.tools;

import com.blog.config.Configuration;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author haijun.zhang
 */
public class BlogUtils {

    public static String sha1Hex(String str) {
        return DigestUtils.sha1Hex(Configuration.getInstance().getSalt() + str);
    }
}
