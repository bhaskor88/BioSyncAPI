package com.bohniman.api.biosynchronicity.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AESEncryption {

    private static String secretStr;
    private static String initVectorStr;

    @Autowired
    public AESEncryption(CryptoProperties cryptoProperties) {
        secretStr = cryptoProperties.getSecretCode();
        initVectorStr = cryptoProperties.getInitVector();
    }

    // public static IvParameterSpec generateIv() {
    //     return new IvParameterSpec(AESEncryption.initVectorStr.getBytes());
    // }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVectorStr.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretStr.getBytes("UTF-8"), "AES");
     
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
     
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     
        return null;
    }

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVectorStr.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretStr.getBytes("UTF-8"), "AES");
     
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
     
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
