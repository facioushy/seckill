package com.hik.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author SYSTEM
 */
public class MD5Utils {

    public static String md5(String key) {
        return DigestUtils.md5Hex(key);
    }

    public static final String salt = "1a2b3c4d";

    /**
     * 一次加密
     * @param inputPass
     * @return
     */
    public static String inputPassFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass +
                salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 二次加密
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPass2DBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPass2DBPass(String inputPass, String saltDB) {
        String formPass = inputPassFormPass(inputPass);
        String dbPass = formPass2DBPass(formPass, saltDB);
        return dbPass;
    }

}
