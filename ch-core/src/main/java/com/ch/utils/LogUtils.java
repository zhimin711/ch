package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * LogUtils
 * Created by 01370603 on 2017/11/22.
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static final String LEVEL_PATTERN = "(DEBUG|INFO|WARN|ERROR)";
    public static final String DATE_PATTERN = "\\d{1,4}[-|/|年|.]\\d{1,2}[-|/|月|.]\\d{1,2}";
    public static final String TIME_PATTERN = "\\d{1,2}[:|时|点]\\d{1,2}[:|分]\\d{1,2}[.]\\d{1,3}";
    public static final String THREAD_PATTERN = "[\\u007C|\\u005B].*[\\u007C|\\u005D]";

    private LogUtils() {
    }

    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }


    /**
     * (|[- )%-5level(|]- )
     *
     * @param str 输入字符串
     * @return
     */
    public static boolean isFormat(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = ".*[\\u007C|\\u005B|\\u002D|\\u0020]" + LEVEL_PATTERN + "[\\u007C|\\u005D|\\u002D|\\u0020].*";
        return Pattern.matches(pattern, str);
    }

    /**
     * %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %msg%n
     *
     * @param str 输入字符串
     * @return
     */
    public static boolean isLogFormat(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = "([#]?)" + DATE_PATTERN + "(\\s)" + TIME_PATTERN + "(\\s)" + THREAD_PATTERN + "(\\s)" + LEVEL_PATTERN + ".*(\\s)*";
        return Pattern.matches(pattern, str);
    }

    /**
     * %d{yyyy-MM-dd HH:mm:ss.SSS}|%-5level|%thread| %msg%n
     *
     * @param str 日志信息
     * @return
     */
    public static boolean isLogFormat1(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = "([#]?)" + DATE_PATTERN + "(\\s)" + TIME_PATTERN + "(\\s?)" + "\\u007C" + LEVEL_PATTERN + "(\\s?)" + THREAD_PATTERN + ".*(\\s)*";
        return Pattern.matches(pattern, str);
    }

    /**
     * %d{MM-dd HH:mm:ss.SSS} [%thread] %-5level %msg%n
     *
     * @param str 日志信息
     * @return
     */
    public static boolean isLogFormat2(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = ".*\\u005B*\\u005D\u0020(DEBUG|INFO|WARN|ERROR)\u0020.*";
        return Pattern.matches(pattern, str);
    }


    public static boolean isLogError(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = ".*[\\u007C|\\u005B|\\u002D|\\u0020](ERROR)[\\u007C|\\u005D|\\u002D|\\u0020].*";
        return Pattern.matches(pattern, str);
    }

    public static boolean isLogSQL(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String sql = str.trim().toLowerCase();
        if (sql.equals("insert")) {
            return true;
        } else if (sql.equals("select")) {
            return true;
        } else if (sql.equals("update")) {
            return true;
        } else if (sql.equals("delete")) {
            return true;
        } else if (sql.equals("where")) {
            return true;
        } else if (sql.equals("and")) {
            return true;
        } else if (sql.equals("from")) {
            return true;
        } else if (sql.equals("set")) {
            return true;
        } else if (sql.equals("group by")) {
            return true;
        }

        if (sql.startsWith("insert ")) {
            return true;
        } else if (sql.startsWith("select ")) {
            return true;
        } else if (sql.startsWith("update ")) {
            return true;
        } else if (sql.startsWith("delete ")) {
            return true;
        } else if (sql.startsWith("and ")) {
            return true;
        } else if (sql.startsWith("where ")) {
            return true;
        } else if (sql.startsWith("set ")) {
            return true;
        } else if (sql.startsWith("from ")) {
            return true;
        } else if (sql.startsWith("this_.")) {
            return true;
        } else if (sql.startsWith("(select")) {
            return true;
        } else if (sql.startsWith("group by ")) {
            return true;
        }
        if (sql.contains("=?")) {
            return true;
        } else if (sql.contains("= ?")) {
            return true;
        } else if (sql.contains("is null")) {
            return true;
        } else if (sql.contains("in (")) {
            return true;
        } else return sql.contains(" as ");

    }


    public static boolean isLogInfoOrDebug(String str) {
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = ".*[\\u007C|\\u005B|\\u002D|\\u0020](DEBUG|INFO)[\\u007C|\\u005D|\\u002D|\\u0020].*";
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
        if (CommonUtils.isEmpty(str)) {
            return false;
        }
        String pattern = ".*Response .*\u002E(pvt|pub|arz) in.*ms.";
        return Pattern.matches(pattern, str);
    }
}
