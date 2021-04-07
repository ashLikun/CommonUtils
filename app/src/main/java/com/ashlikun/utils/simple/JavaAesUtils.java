package com.ashlikun.utils.simple;


import com.ashlikun.utils.encryption.AESUtils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES����
 */
public class JavaAesUtils {

    /**
     * 加密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey){
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) {
        String aa = "8xxxSRkzLfjuzFkhbP4YYrijDYm5v5ZTgve79+C7ozXhE/d70RGlfyxI6PRBStX7XexXcbD/QlEerr5clbbDzQ==";
        String decrypt = Decrypt(aa, "8d68a9777b8b7115364452c712837616");
        System.out.println("decrypt : " + decrypt);
    }
}

