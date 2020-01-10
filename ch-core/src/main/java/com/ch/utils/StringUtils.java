package com.ch.utils;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private StringUtils() {
    }

    public static String formatSort(Number number, int length) {
        if (number == null) number = 0;
        return String.format("%0" + length + "d", number);
    }

    public static String toString(Number number) {
        if (number == null) return null;
        return number.toString();
    }
}
