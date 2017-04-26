package com.ch.utils;

import com.ch.pojo.KeyValue;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述：通用工具类
 *
 * @author 80002023
 *         2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class CommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {
    }

    /**
     * 对象比较 支持类型
     * 基本类型：Integer、Long、Double、Float、byte、short
     * 其它类型：String、Date
     *
     * @param a 对象a
     * @param b 对象b
     * @return true or false
     */
    public static boolean isEquals(final Object a, final Object b) {
//        logger.debug("{} === {}", a, b);
        if (isNotEmpty(a) && isNotEmpty(b)) {
            if (a instanceof Number && b instanceof Number) {
                return a.equals(b);
            } else if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else if (a instanceof Boolean && b instanceof Boolean) {
                return a.equals(b);
            } else if (a instanceof Date && b instanceof Date) {
                return ((Date) a).getTime() == ((Date) b).getTime();
            }
        }
        return false;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return true or false
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        logger.debug("CommonUtils.isEmpty param class name: {}", obj.getClass());
        if (obj instanceof String) {
            if (((String) obj).trim().length() == 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).isEmpty()) {
                return true;
            }
        } else if (obj.getClass().isArray()) {
            if (((Object[]) obj).length == 0) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否不为空
     * Java封装基本类型
     *
     * @param obj 对象
     * @return true or false
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断对象数组是否不为空
     * Java封装基本类型
     *
     * @param objects 对象集合
     * @return true or false
     */
    public static boolean isNotEmpty(Object... objects) {
        if (objects != null && objects.length > 0) {
            boolean isNot = true;
            for (Object obj : objects) {
                if (!isNotEmpty(obj)) {
                    isNot = false;
                    break;
                }
            }
            return isNot;
        }
        return false;
    }

    /**
     * 网址最后"/"处理
     *
     * @param url 字符串
     * @return true or false
     */
    public static String urlHandler(final String url) {
        if (!isNotEmpty(url)) {
            return "";
        }
        String tmp = url;
        if (!tmp.startsWith("/")) {
            tmp = "/" + tmp;
        }
        if (tmp.endsWith("/")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        } else if (tmp.endsWith("*") && !tmp.endsWith("/*") && !tmp.endsWith(".*")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
//        logger.info("url[{}] handler [{}]", url, tmp);
        return tmp;
    }

    /**
     * check string is number
     *
     * @param str 字符串
     * @return true or false
     */
    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 检测邮箱地址是否合法
     *
     * @param email 字符串
     * @return true合法 false不合法
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        return p.matcher(email).matches();
    }

    /**
     * check string is decimal
     *
     * @param str 字符串
     * @return true or false
     */
    public static boolean isDecimal(String str) {
        String s = "0.00";
        if (StringUtils.isNotBlank(str)) {
            s = str.replaceFirst(".", "");
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(s).matches();
    }

    /**
     * check string contain digit
     *
     * @param str 字符串
     * @return true or false
     */
    public static boolean hasDigit(String str) {
        Pattern pattern = Pattern.compile(".*\\d+.*");
        return pattern.matcher(str).matches();
    }


    /**
     * Get list keyValue by key
     *
     * @param records KeyValue集合
     * @param key     Key键值
     * @return key的值
     */
    public static KeyValue getKey(final List<KeyValue> records, String key) {
        if (records != null) {
            List<KeyValue> result = records.stream().filter(r -> isEquals(key, r.getKey())).collect(Collectors.toList());
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }


}
