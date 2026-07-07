/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Crypto {
    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; ++i) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String sha1(String base) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(base.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String cryptoMD5(String wordToHash) {
        StringBuilder hexString = new StringBuilder();
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(wordToHash.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; ++i) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                    hexString.append(hex.charAt(hex.length() - 1));
                    continue;
                }
                hexString.append(hex.substring(hex.length() - 2));
            }
            return hexString.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String loginBiometrie(String password) {
        String crypMd5 = Crypto.cryptoMD5("RS_" + password + "-er");
        String crypSha1 = Crypto.sha1(crypMd5);
        return crypSha1;
    }
}
