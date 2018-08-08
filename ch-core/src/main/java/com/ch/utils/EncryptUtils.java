package com.ch.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import com.ch.exception.InvalidArgumentException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

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

    public enum AlgorithmType {
        MD5("MD5"), AES("AES"), DES("DES"), SHA_1("SHA-1"), SHA_256("SHA-256"), SHA_348("SHA-348"), SHA_512("SHA-512");
        private final String code;

        AlgorithmType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

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
            MessageDigest md = MessageDigest.getInstance(AlgorithmType.MD5.code);
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(AlgorithmType type, String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(type.code);

            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密
     *
     * @param source   byte[]
     * @param password String
     * @return byte[]
     */
    public static String encryptDES(String source, String password) {
        try {
            if (CommonUtils.isEmpty(password) || password.length() % 8 != 0) {
                throw new InvalidArgumentException("invalid password must be not null or length not 8*");
            }
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，使用KeySpec
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(AlgorithmType.DES.code);
            SecretKey secureKey = keyFactory.generateSecret(desKey);
            return encodeBase64(encrypt(source.getBytes(), AlgorithmType.DES, secureKey));
        } catch (Throwable e) {
            logger.error("DES encrypt error!", e);
        }
        return source;
    }

    /**
     * 加密
     *
     * @param source   byte[]
     * @param password String
     * @return byte[]
     */
    public static String decryptDES(String source, String password) {
        try {
            if (CommonUtils.isEmpty(password) || password.length() % 8 != 0) {
                throw new InvalidArgumentException("invalid password must be not null or length not 8*");
            }
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(AlgorithmType.DES.code);
            SecretKey secureKey = keyFactory.generateSecret(desKey);
            return new String(decrypt(decodeBase64(source.getBytes()), AlgorithmType.DES, secureKey));
        } catch (Throwable e) {
            logger.error("DES decrypt error!", e);
        }
        return source;
    }


    /**
     * 加密
     *
     * @param source   byte[]
     * @param password String
     * @return byte[]
     */
    public static String encryptAES(String source, String password) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AlgorithmType.AES.code);// 创建AES的Key生产者
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            keyGenerator.init(128, secureRandom);
            // 根据用户密码，生成一个密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AlgorithmType.AES.code);// 转换为AES专用密钥
            return encodeBase64(encrypt(source.getBytes(), AlgorithmType.AES, secretKeySpec));
        } catch (Throwable e) {
            logger.error("AES encrypt error!", e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param source   byte[]
     * @param password String
     * @return byte[]
     */
    public static String decryptAES(String source, String password) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AlgorithmType.AES.code);// 创建AES的Key生产者
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AlgorithmType.AES.code);// 转换为AES专用密钥
            return new String(decrypt(decodeBase64(source.getBytes()), AlgorithmType.AES, secretKeySpec));
        } catch (Throwable e) {
            logger.error("AES decrypt error!", e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param source
     * @param type
     * @param secureKey
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] source, AlgorithmType type, SecretKey secureKey) throws Exception {
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(type.code);
        //用密匙初始化Cipher对象
        if (type == AlgorithmType.AES) {
            cipher.init(Cipher.ENCRYPT_MODE, secureKey);
        } else {
            //创建算法随机数源
            SecureRandom random = new SecureRandom();
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, random);
        }
        //现在，获取数据并加密
        //正式执行加密操作
        return cipher.doFinal(source);
    }

    /**
     * 加密
     *
     * @param source
     * @param type
     * @param secureKey
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] source, AlgorithmType type, SecretKey secureKey) throws Exception {
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(type.code);
        //用密匙初始化Cipher对象

        if (type == AlgorithmType.AES) {
            cipher.init(Cipher.DECRYPT_MODE, secureKey);
        } else {
            //创建算法随机数源
            SecureRandom random = new SecureRandom();
            cipher.init(Cipher.DECRYPT_MODE, secureKey, random);
        }
        //现在，获取数据并加密
        //正式执行加密操作
        return cipher.doFinal(source);
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


    /**
     * @param str
     * @return
     */
    public static String encodeBase64(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

    /**
     * @param str
     * @return
     */
    public static String encodeBase64(byte[] str) {
        return new String(Base64.getEncoder().encode(str));
    }

    /**
     * @param str
     * @return
     */
    public static String decodeBase64(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes()));
    }

    /**
     * @param str
     * @return
     */
    public static byte[] decodeBase64(byte[] str) {
        return Base64.getDecoder().decode(str);
    }

}
