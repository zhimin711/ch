package com.ch.utils;

import java.util.regex.Pattern;

/**
 * LogUtils
 * Created by 01370603 on 2017/11/22.
 */
public class LogUtils {

    private LogUtils() {
    }

    public static boolean isLogFormat(String str) {
        String pattern = ".*[\u007C\u0020](DEBUG|INFO|WARN|ERROR).*";
        return Pattern.matches(pattern, str);
    }

    /**
     * format(#%d{yyyy-MM-dd HH:mm:ss.SSS}|%-5level|%thread| %class.%method\\(%F:%L\\)|%msg%n)
     * @param str
     * @return
     */
    public static boolean isLogFormat1(String str) {
        String pattern = ".*[\u007C](DEBUG|INFO|WARN|ERROR)[\u0020\u007C]*[\u007C].*";
        return Pattern.matches(pattern, str);
    }

    /**
     * format(%d{MM-dd HH:mm:ss.SSS} [%thread] %-5level %class.%method\\(%F:%L\\) - %msg%n)
     * @param str
     * @return
     */
    public static boolean isLogFormat2(String str) {
        String pattern = ".*\\u005B*\\u005D\u0020(DEBUG|INFO|WARN|ERROR)\u0020.*";
        return Pattern.matches(pattern, str);
    }


    public static boolean isVerticalLineSplit(String str) {
        String pattern = ".*[\u007C](DEBUG|INFO|WARN|ERROR).*";
        return Pattern.matches(pattern, str);
    }

    /**
     * 判断日志信息是否包含响应、URL及时间（毫秒）
     * <p>Struts</p>
     *
     * @param str 日志信息
     * @return
     */
    public static boolean hasResponseUrl(String str) {
        String pattern = ".*Response .*\u002E(pvt|pub|arz) in.*ms.";
        return Pattern.matches(pattern, str);
    }
}
