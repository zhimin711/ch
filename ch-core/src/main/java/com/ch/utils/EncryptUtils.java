package com.ch.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/4.
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
     * @param privateKey
     * @param source
     * @return
     */
    public static String encrypt(String privateKey, String source) {
        try {
            if (StringUtils.isNotBlank(privateKey)) {
                return ConfigTools.encrypt(privateKey, source);
            } else {
                return ConfigTools.encrypt(source);
            }
        } catch (Exception e) {
            logger.error("Encrypt Error!");
//            e.printStackTrace();
        }
        return source;
    }

    /**
     * use config tool decrypt text by public key
     *
     * @param publicKey
     * @param source
     * @return
     */
    public static String decrypt(String publicKey, String source) {
        try {
            if (StringUtils.isNotBlank(publicKey)) {
                return ConfigTools.decrypt(publicKey, source);
            } else {
                return ConfigTools.decrypt(source);
            }
        } catch (Exception e) {
//            logger.info("public key: {}, cipher text: {}", publicKey, source);
            logger.error("Config tool decrypt error! public key: {}, cipher text: {}", publicKey, source);
//            e.printStackTrace();
        }
        return source;
    }

    /**
     * generate keypair
     * key[0] private key
     * key[1] public key
     *
     * @param keySize
     * @return
     */
    public static String[] genKeyPair(int keySize) {
        try {
            return ConfigTools.genKeyPair(keySize);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Config tool genKeyPair error! message: {}", e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }
}
