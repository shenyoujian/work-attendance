package com.ljs.common.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/17 20:40
 **/
public class SecurityUtils {

    /*
     * Author ljs
     * Description 密码加密
     * Date 2018/8/17 20:49
     **/
    public static String encrptyPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String result = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));
        return result;
    }

    /*
     * Author ljs
     * Description 密码校验
     * Date 2018/8/17 20:49
     **/
    public static boolean checkPassword(String inputPwd, String dbPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String result = encrptyPassword(inputPwd);
        if (result.equals(dbPwd)) {
            return true;
        }
        return false;
    }
}
