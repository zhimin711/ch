package com.ch.utils;

import com.ch.Constants;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
     * 格式数值为固定格式
     *
     * @param number 数值
     * @param length 长度
     * @return
     */
    public static String formatSort(Number number, int length) {
        if (number == null) number = 0;
        return String.format("%0" + length + "d", number);
    }

    /**
     * 取操作符后字符串
     *
     * @param s         字符串
     * @param separator 连接符号
     * @return
     */
    public static String lastStr(String s, String separator) {
        if (CommonUtils.isEmpty(s)) return "";
        if (s.contains(separator)) {
            int i = s.lastIndexOf(separator);
            return s.substring(i + 1);
        }
        return s;
    }

    /**
     * 取操作符后字符串并转换成Long Id
     *
     * @param str 字符串
     * @return id
     */
    public static Long lastId(String str) {
        String idStr = lastStr(str, Constants.SEPARATOR_2);
        return CommonUtils.isNumeric(idStr) ? Long.valueOf(idStr) : null;
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
     * 拆分字符串数组(不去重)
     *
     * @param separator 拆分符号
     * @param arrStr    数组字符串
     * @return
     */
    public static List<String> splitStr(String separator, String arrStr) {
        if (CommonUtils.isEmpty(arrStr)) return Lists.newArrayList();
        if (!arrStr.contains(separator)) {
            return Lists.newArrayList(arrStr);
        }
        String[] arr = arrStr.split(separator);
        return Lists.newArrayList(arr);
    }

    /**
     * 拆分字符串数组并去重(de-duplication)
     *
     * @param separator 拆分符号
     * @param arrStr    数组字符串
     * @return
     */
    public static List<String> splitStrAndDeDuplication(String separator, String arrStr) {
        List<String> list = splitStr(separator, arrStr);
        if (list.isEmpty() || list.size() == 1) return list;
        return Lists.newArrayList(Sets.newHashSet(list));
    }

    /**
     * 链接字符串数组（并忽略0）
     *
     * @param separator 连接符号
     * @param args      字符串数组
     * @return
     */
    public static String linkStrIgnoreZero(String separator, String... args) {
        if (CommonUtils.isEmpty(args)) return "";
        StringBuilder sb = new StringBuilder();
        for (String str : args) {
            if (CommonUtils.isEmpty(str) || CommonUtils.isEquals(Constants.DISABLED, str)) {
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

    /**
     * 解析id字符串为集合(List)
     *
     * @param idStr id字符串
     * @return Long集合
     */
    public static List<Long> parseIds(String idStr) {
        if (CommonUtils.isEmpty(idStr)) {
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

    /**
     * 解析id字符串为数组([])
     *
     * @param idStr id字符串
     * @return Long数组
     */
    public static Long[] parseIdArr(String idStr) {
        return parseIds(idStr).toArray(new Long[]{});
    }

    /**
     * ID数组转换为字符串(","拼接)
     *
     * @param ids id数组
     * @return id字符串
     */
    public static String toIdStr(Number[] ids) {
        List<String> idList = Lists.newArrayList();
        for (Number id : ids) {
            idList.add(id.toString());
        }
        return linkStr(Constants.SEPARATOR_2, idList.toArray(new String[]{}));
    }

    /**
     * ID集合转换为字符串(","拼接)
     *
     * @param ids id数组
     * @return id字符串
     */
    public static String toIdStr(List<Number> ids) {
        return toIdStr(ids.toArray(new Number[]{}));
    }

}
