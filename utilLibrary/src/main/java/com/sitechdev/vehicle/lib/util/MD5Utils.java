package com.sitechdev.vehicle.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * MD5加密工具类
 *
 * @author liuhe
 * @created 2017/8/3
 */
public class MD5Utils {
    private static final String TAG = MD5Utils.class.getSimpleName();
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA256 = "SHA-256";
    private static final char[] MD5_HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5Utils() {
    }

    /**
     * 获取文件的 MD5
     */
    public static String getMd5(File file) {
        return encode(file, ALGORITHM_MD5);
    }

    public static String getSha256(File file) {
        return encode(file, ALGORITHM_SHA256);
    }

    public static String encode(File file, String encodeType) {
        if (null == file || !file.exists()) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encodeType);
            FileInputStream inputStream = new FileInputStream(file);
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
            //必须把文件读取完毕才能拿到md5
            byte[] buffer = new byte[4096];
            while (digestInputStream.read(buffer) > -1) {
            }
            MessageDigest digest = digestInputStream.getMessageDigest();
            digestInputStream.close();
            byte[] md5 = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        try {
            byte[] keyBytes = android.util.Base64.decode(publicKey, android.util.Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(android.util.Base64.decode(sign, android.util.Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String encryptionMD5(String content) throws NoSuchAlgorithmException {
        byte[] strTemp = new byte[0];
        try {
            strTemp = content.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] md = md5(strTemp);
        return toHexString(md);
    }

    public static String encryptionMD5(String content, String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] strTemp = content.getBytes(charset);
        byte[] md = md5(strTemp);
        return toHexString(md);
    }

    public static byte[] md5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest mdTemp = MessageDigest.getInstance(ALGORITHM_MD5);
        mdTemp.update(data);
        byte[] md = mdTemp.digest();
        return md;
    }

    private static String toHexString(byte[] data) {
        int j = data.length;
        char[] str = new char[j * 2];
        int k = 0;

        for (int i = 0; i < j; ++i) {
            byte b = data[i];
            str[k++] = MD5_HEX_DIGITS[b >> 4 & 15];
            str[k++] = MD5_HEX_DIGITS[b & 15];
        }

        return new String(str);
    }

    /**
     * SHA256加密
     *
     * @param content
     * @return
     */
    public static String encrytSHA256(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("sha-256");
            byte[] bytes = md5.digest((content).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
