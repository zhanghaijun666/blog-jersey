package com.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author zhanghaijun
 */
public class EncryptUtils {

    public static String sha1(byte[] buffer) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(buffer);
            return toHex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }

    private static String sha1(byte[] buffer, int start, int length, boolean bease64, boolean needSalt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(buffer, start, length);
            if (needSalt) {
                byte[] saltBuffer = "blog".getBytes("UTF-8");
                digest.update(saltBuffer, 0, saltBuffer.length);
            }
            byte[] result = digest.digest();
            if (bease64) {
                return base64(result);
            } else {
                return toHex(result);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(BlogUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String base64(byte[] result) {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64(true);
        return base64.encodeToString(result).replace("\n", "").trim();
    }

    public static String toHex(byte[] result) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
