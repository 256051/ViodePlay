package com.dsl.tvplayer.util;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    // 算法名称
    private final static String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private final static String algorithmStr = "AES/CBC/PKCS7Padding";

    static byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

    public static String encrypt(String message, String key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        byte[] srcBuff = message.getBytes(StandardCharsets.UTF_8);
        byte[] KEY = addZeroForNum(key,32).getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY, KEY_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher ecipher = Cipher.getInstance(algorithmStr);
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

        byte[] dstBuff = ecipher.doFinal(srcBuff);

        String base64 = Base64.encodeToString(dstBuff, Base64.NO_WRAP);

        return base64;

    }


    //左补零
    public static String addZeroForNum(String str,int strLength) {
        int strLen =str.length();
        if (strLen <strLength) {
            while (strLen< strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
//    sb.append(str).append("0");//右补0
                str= sb.toString();
                strLen= str.length();
            }
        }

        return str;
    }
}