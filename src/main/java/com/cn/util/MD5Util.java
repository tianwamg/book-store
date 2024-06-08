package com.cn.util;

import org.springframework.util.DigestUtils;

public class MD5Util {

    public static final String salt = "831d895e";

    public static String getMD5Digest(String plainText){
        return DigestUtils.md5DigestAsHex(plainText.concat(salt).getBytes());
    }
}
