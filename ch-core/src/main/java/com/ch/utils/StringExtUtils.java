package com.ch.utils;

import com.ch.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

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

    public static List<Long> parseIds(String idStr) {
        if(CommonUtils.isEmpty(idStr)){
            return Lists.newArrayList();
        }
        String[] idArr = idStr.split(Constants.SEPARATOR_2);
        List<Long> ids = Lists.newArrayList();
        for (String id : idArr) {
            Number num = NumberUtils.createNumber(id);
            ids.add(num.longValue());
        }
        return ids;
    }

    public static Long[] parseIdArr(String idStr) {
        return parseIds(idStr).toArray(new Long[]{});
    }

    public static String toIdStr(Number[] ids) {
        List<String> idList = Lists.newArrayList();
        for (Number id : ids) {
            idList.add(id.toString());
        }
        return linkStr(Constants.SEPARATOR_2, idList.toArray(new String[]{}));
    }

    public static String toIdStr(List<Number> ids) {
        return toIdStr(ids.toArray(new Number[]{}));
    }
}
