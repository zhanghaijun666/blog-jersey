package com.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * @author zhanghaijun
 */
public class Utils {

    public static void main(String[] args) throws Exception {
        System.out.println("uuid:" + getUUID());
        System.out.println("uuid:" + getUUID());
        System.out.println("MD5:123" + getMD5Str("s s"));
        System.out.println("MD5:123" + getMD5Str("ss"));
        
        System.out.println("3位随机数字验证码：" + getVerificationCode(3));
        System.out.println("6位随机数字验证码：" + getVerificationCode(6));

    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String getMD5Str(String str) throws Exception {
        if (str == null) {
            str = "";
        }
        str = str.trim();
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("MD5加密出现错误，" + e.toString());
        }
    }

    /**
     * 获取随机数字验证码
     *
     * @param verificationCodeLength
     * @return
     */
    public static String getVerificationCode(int verificationCodeLength) {
        //所有候选组成验证码的字符，可以用中文
        String[] verificationCodeArrary = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String verificationCode = "";
        Random random = new Random();
        //此处是生成验证码的核心了，利用一定范围内的随机数做为验证码数组的下标，循环组成我们需要长度的验证码，做为页面输入验证、邮件、短信验证码验证都行
        for (int i = 0; i < verificationCodeLength; i++) {
            verificationCode += verificationCodeArrary[random.nextInt(verificationCodeArrary.length)];
        }
        return verificationCode;
    }
}
