package com.ch.utils;

/**
 * 描述：com.ch.utils
 *
 * @author zhimin.ma
 * 2019/2/4.
 * @version 1.0
 * @since 1.8
 */
public class StringExtUtils  {

    private StringExtUtils() {
    }

    public static String formatSort(Number number, int length) {
        if (number == null) number = 0;
        return String.format("%0" + length + "d", number);
    }

    public static String lastStr(String s, String separator) {
        if (CommonUtils.isEmpty(s)) return "";
        if (s.contains(separator)) {
            int i = s.lastIndexOf(separator);
            return s.substring(i);
        }
        return s;
    }
}
