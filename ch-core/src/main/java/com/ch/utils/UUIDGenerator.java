package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Random;

/**
 * 描述：生成类似hibernate中uuid 32位主键序列
 *
 * @author 80002023
 * 2017/3/21.
 * @version 1.0
 * @since 1.8
 */
public class UUIDGenerator {

    private final static Logger logger = LoggerFactory.getLogger(UUIDGenerator.class);

    private static final int IP;

    static {
        int ipAddress;
        try {
            logger.info("This Server IP: {}", PlatformUtils.getLocalIp());
            ipAddress = ipToInt(PlatformUtils.getLocalAddress());
        } catch (Exception e) {
            ipAddress = 0;
        }
        IP = ipAddress;
    }

    private static short counter = (short) 0;
    private static short m = (short) 10;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    public UUIDGenerator() {
    }

    public static int ipToInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }


    public static int getJVM() {
        return JVM;
    }

    public static short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0)
                counter = 1;
            return counter++;
        }
    }

    public static short getLimitCount(int len) {
        int limit = m ^ len;
        synchronized (UUIDGenerator.class) {
            if (counter < 0 || counter >= limit) {
                counter = 1;
            }
            return counter++;
        }
    }

    public static int getIP() {
        return IP;
    }

    public static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    public static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private final static String sep = "";

    public static String format(int intValue) {
        String formatted = Integer.toHexString(intValue);
        StringBuilder buf = new StringBuilder("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    public static String format(short shortValue) {
        String formatted = Integer.toHexString(shortValue);
        StringBuilder buf = new StringBuilder("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public static String formatZero(short shortValue) {
        return String.format("%04d", shortValue);
    }

    public static String generate() {
        return format(getIP()) + sep +
                format(getJVM()) + sep +
                format(getHiTime()) + sep +
                format(getLoTime()) + sep +
                format(getCount());
    }


    public static String generate(String prefix) {
        return prefix + sep + format(getIP()).toUpperCase() + sep + DateUtils.format(DateUtils.currentTime(), DateUtils.Pattern.DATETIME_SHORT) + formatZero(getLimitCount(4));
    }

    /**
     * 按模块生成交易序号
     *
     * @param prefix    前缀
     * @param separator M-手机 T-pc:R-需求 C-竞价 T-招标
     * @return String code
     */
    public static String generateCode(String prefix, String separator) {
        return prefix + "-" + separator + "-" + DateUtils.format(DateUtils.currentTime(), DateUtils.Pattern.DATETIME_FULL_SHORT)
                + (new Random().nextInt(900) + 100);
    }

}
