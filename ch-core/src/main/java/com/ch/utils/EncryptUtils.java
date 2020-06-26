package com.ch.utils;

import com.ch.e.PubError;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 描述：com.ch.utils
 *
 * @author zhimin.ma
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class EncryptUtils {

    private final static Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    public enum AlgorithmType {
        MD5("MD5"),
        AES("AES"),
        /**
         * AES_CBC
         * "算法/模式/补码方式"
         */
        AES_CBC("AES/CBC/PKCS5Padding"),
        DES("DES"), SHA_1("SHA-1"),
        SHA_256("SHA-256"), SHA_348("SHA-348"), SHA_512("SHA-512");
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

    public static String md5(String str) {
        return MD5(str);
    }

    private static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(AlgorithmType.MD5.code);
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(AlgorithmType type, String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(type.code);

            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
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
                throw ExceptionUtils.create(PubError.ARGS, "invalid password must be not null or length not 8*");
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
                throw ExceptionUtils.create(PubError.ARGS, "invalid password must be not null or length not 8*");
            }
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(AlgorithmType.DES.code);
            SecretKey secureKey = keyFactory.generateSecret(desKey);
            return new String(decrypt(decodeBase64(source), AlgorithmType.DES, secureKey));
        } catch (Throwable e) {
            logger.error("DES decrypt error!", e);
        }
        return source;
    }


    /**
     * 加密AES
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
        return source;
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
            return new String(decrypt(decodeBase64(source), AlgorithmType.AES, secretKeySpec));
        } catch (Throwable e) {
            logger.error("AES decrypt error!", e);
        }
        return null;
    }


    /**
     * 加密AES CBC
     * 使用CBC模式，需要一个向量iv，可增加加密算法的强度
     *
     * @param source   String
     * @param password String
     * @param iv       String
     * @return String
     */
    public static String encryptAESCBC(String source, String password, String iv) {
        try {
            if (CommonUtils.isEmpty(password) || password.length() != 16) {
                throw ExceptionUtils.create(PubError.ARGS, "password must be not null or length not equals 16!");
            }

            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), AlgorithmType.AES.code);
            Cipher cipher = Cipher.getInstance(AlgorithmType.AES_CBC.code);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(source.getBytes());
            return encodeBase64(encrypted);
        } catch (Throwable e) {
            logger.error("AES CBC encrypt error!", e);
        }
        return source;
    }

    /**
     * 加密AES_CBC
     * //使用CBC模式，需要一个向量iv，可增加加密算法的强度
     *
     * @param source   byte[]
     * @param password String
     * @return byte[]
     */
    public static String decryptAESCBC(String source, String password, String iv) {
        try {
            if (CommonUtils.isEmpty(password) || password.length() != 16) {
                throw ExceptionUtils.create(PubError.ARGS, "password must be not null or length not equals 16!");
            }
            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), AlgorithmType.AES.code);
            Cipher cipher = Cipher.getInstance(AlgorithmType.AES_CBC.code);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encrypted = decodeBase64(source);//先用bAES64解密
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Throwable e) {
            logger.error("AES CBC decrypt error!", e);
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
    public static String encodeBase64(byte[] str) {
        return Base64.getEncoder().encodeToString(str);
    }

    /**
     * @param str
     * @return
     */
    public static String encodeBase64(String str) {
        return encodeBase64(str.getBytes());
    }

    /**
     * @param str
     * @return
     */
    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * @param str
     * @return
     */
    public static String decodeBase64ToString(String str) {
        return new String(decodeBase64(str));
    }
    /**
     * 获取该输入流的MD5值
     *
     * @param is 输入流
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getMD5(InputStream is) throws NoSuchAlgorithmException, IOException {
        StringBuilder md5 = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];

        int len;
        while ((len = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, len);
        };
        byte[] mdBytes = md.digest();

        // convert the byte to hex format
        for (byte mdByte : mdBytes) {
            md5.append(Integer.toString((mdByte & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }

}
