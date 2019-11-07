package com.ch.utils;

/**
 * 描述：com.ch.utils
 *
 * @author zhimin.ma
 * 2019/2/4.
 * @version 1.0
 * @since 1.8
 */
public class StringExtUtils {

    private StringExtUtils() {
    }

    /**
     * @param number
     * @param length
     * @return
     */
    public static String formatSort(Number number, int length) {
        if (number == null) number = 0;
        return String.format("%0" + length + "d", number);
    }

    /**
     * @param s         字符串
     * @param separator 连接符号
     * @return
     */
    public static String lastStr(String s, String separator) {
        if (CommonUtils.isEmpty(s)) return "";
        if (s.contains(separator)) {
            int i = s.lastIndexOf(separator);
            return s.substring(i);
        }
        return s;
    }

    /**
     * 链接字符串数组
     *
     * @param separator 连接符号
     * @param args      字符串数组
     * @return
     */
    public static String linkStr(String separator, String... args) {
        if (CommonUtils.isEmpty(args)) return "";
        StringBuilder sb = new StringBuilder();
        for (String str : args) {
            if (CommonUtils.isEmpty(str)) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(separator).append(str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 检查字符串是包含指定字符集
     *
     * @param str  字符串
     * @param args 字符集
     * @return 是或否
     */
    public static boolean isExists(String str, String... args) {
        if (CommonUtils.isEmpty(str) || CommonUtils.isEmpty(args)) return false;
        for (String s : args) {
            if (CommonUtils.isEmpty(s)) {
                continue;
            }
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }
}