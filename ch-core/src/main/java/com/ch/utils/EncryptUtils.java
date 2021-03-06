package com.ch.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class EncryptUtils {

    private final static Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    public static final String ALGORITHM_MD5 = "MD5";

    public static final String ALGORITHM_SHA_1 = "SHA-1";
    public static final String ALGORITHM_SHA_256 = "SHA-256";
    public static final String ALGORITHM_SHA_348 = "SHA-348";
    public static final String ALGORITHM_SHA_512 = "SHA-512";

    private EncryptUtils() {
    }

    public static String generate() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    public static String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * use config tool encrypt text by private key
     *
     * @param privateKey 私钥
     * @param source     明文串
     * @return 加密串
     */
    public static String encrypt(String privateKey, String source) {
        try {
            if (StringUtils.isNotBlank(privateKey)) {
                return ConfigTools.encrypt(privateKey, source);
            } else {
                return ConfigTools.encrypt(source);
            }
        } catch (Exception e) {
            logger.error("Encrypt Error!", e);
        }
        return source;
    }

    /**
     * use config tool decrypt text by public key
     *
     * @param publicKey 公钥
     * @param source    加密串
     * @return 解密串
     */
    public static String decrypt(final String publicKey, final String source) {
        try {
            if (StringUtils.isNotBlank(publicKey)) {
                return ConfigTools.decrypt(publicKey, source);
            } else {
                return ConfigTools.decrypt(source);
            }
        } catch (Exception e) {
            logger.error("Config tool decrypt error! public key: {" + publicKey + "}, cipher text: {" + source + "}", e);
        }
        return source;
    }

    /**
     * generate keypair
     * key[0] private key
     * key[1] public key
     *
     * @param keySize 钥匙算法位数
     * @return 钥匙对（0私钥与1公钥）
     */
    public static String[] genKeyPair(int keySize) {
        try {
            return ConfigTools.genKeyPair(keySize);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Config tool genKeyPair error!", e);
        }
        return null;
    }

    public static String md5(String str) {
        return MD5(str);
    }

    private static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }
}
