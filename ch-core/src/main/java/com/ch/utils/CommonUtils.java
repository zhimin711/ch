package com.ch.utils;

import com.ch.pojo.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class CommonUtils {


    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    static final Class<Integer> INTEGER_CLASS = Integer.class;
    static final Class<Long> LONG_CLASS = Long.class;
    static final Class<Double> DOUBLE_CLASS = Double.class;
    static final Class<Float> FLOAT_CLASS = Float.class;
    static final Class<String> STRING_CLASS = String.class;
    static final Class<Boolean> BOOLEAN_CLASS = Boolean.class;
    static final Class<Date> DATE_CLASS = Date.class;
    static final Class<Collection> COLLECTION_CLASS = Collection.class;

    private CommonUtils() {
    }

    /**
     * Java封装基本类型比较 Integer、String、Long、Double、Float
     *
     * @param a
     * @param b
     * @return true or false
     */
    public static boolean isEquals(final Object a, final Object b) {
//        logger.debug("{} === {}", a, b);
        if (INTEGER_CLASS.isInstance(a) && INTEGER_CLASS.isInstance(b)) {
            int x = Integer.valueOf(a.toString());
            int y = Integer.valueOf(b.toString());
            return x == y;
        } else if (LONG_CLASS.isInstance(a) && LONG_CLASS.isInstance(b)) {
            return a.equals(b);
        } else if (DOUBLE_CLASS.isInstance(a) && DOUBLE_CLASS.isInstance(b)) {
            return a.equals(b);
        } else if (FLOAT_CLASS.isInstance(a) && FLOAT_CLASS.isInstance(b)) {
            return a.equals(b);
        } else if (STRING_CLASS.isInstance(a) && STRING_CLASS.isInstance(b)) {
            return a.equals(b);
        } else if (BOOLEAN_CLASS.isInstance(a) && BOOLEAN_CLASS.isInstance(b)) {
            return a.equals(b);
        }
        return false;
    }

    public static boolean isEmpty(final Object[] arr) {
        return arr == null || arr.length <= 0;
    }

    /**
     * Java封装基本类型
     *
     * @param objects
     * @return
     */
    public static boolean isNotBlank(Object... objects) {
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
     * Java封装基本类型
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        logger.info("object class: {}", obj.getClass());
        if (STRING_CLASS.isInstance(obj)) {
            return StringUtils.isNotEmpty(String.valueOf(obj));
        } else if (COLLECTION_CLASS.isInstance(obj)) {
            return !((Collection) obj).isEmpty();
        }
        return true;
    }

    /**
     * @param url
     * @return
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
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 检测邮箱地址是否合法
     *
     * @param email
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
     * @param str
     * @return
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
     * @param str
     * @return
     */
    public static boolean hasDigit(String str) {
        Pattern pattern = Pattern.compile(".*\\d+.*");
        return pattern.matcher(str).matches();
    }


    /**
     * Get list keyValue by key
     *
     * @param records
     * @param key
     * @return
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

    /**
     * set target object of property value
     *
     * @param target
     * @param methodName
     * @param value
     * @param clazz
     * @return
     */
    public static Object setPropertyValue(Object target, String methodName, Object value, Class clazz) {
        if (target != null && methodName != null) {
            Class<?> actualEditable = target.getClass();

            Method method = null;
            try {
                method = actualEditable.getMethod(methodName, clazz);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (method != null) {
                method.setAccessible(true);
                try {
                    method.invoke(target, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return target;
    }
}
