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

    /**
     * set target object of property value
     *
     * @param target     目标对象
     * @param methodName 对象方法
     * @param value      值
     * @param clazz      值类型
     * @return 返回对象
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


    /**
     * set属性的值到Bean
     *
     * @param target        目标对象
     * @param fieldValueMap 属性与值Map
     */
    public static void setFieldValue(Object target, Map<String, String> fieldValueMap) {
        Class<?> cls = target.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String fieldSetName = getSetMethodName(field.getName());
                if (!existSetMethod(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                String value = fieldValueMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(target, value);
                    } else if ("Date".equals(fieldType) || "Timestamp".equals(fieldType)) {
                        Date temp = DateUtils.parse(value);
                        fieldSetMet.invoke(target, temp);
                    } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                        Integer val = Integer.parseInt(value);
                        fieldSetMet.invoke(target, val);
                    } else if ("Long".equalsIgnoreCase(fieldType) || "long".equals(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(target, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType) || "double".equals(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(target, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType) || "boolean".equals(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(target, temp);
                    } else {
                        logger.info("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                logger.error("setFieldValue Error!", e);
            }
        }

    }


    /**
     * 取Bean的属性和值对应关系的MAP
     *
     * @param bean 目标对象
     * @return Map
     */
    public static Map<String, String> getFieldValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, String> valueMap = Maps.newHashMap();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String fieldType = field.getType().getSimpleName();
                String fieldGetName = getGetMethodName(field.getName());
                if (!existsGetMethod(methods, fieldGetName)) {
                    continue;
                }
//                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
//                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});

                Method fieldGetMet = cls.getMethod(fieldGetName);
                Object fieldVal = fieldGetMet.invoke(bean);
                String result = null;
                if ("Date".equals(fieldType) || "Timestamp".equals(fieldType)) {
                    result = DateUtils.format((Date) fieldVal);
                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                logger.error("getFieldValueMap Error!", e);
            }
        }
        return valueMap;

    }

    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods            对象方法
     * @param fieldSetMethodName Set方法名
     * @return boolean
     */
    private static boolean existSetMethod(Method[] methods, String fieldSetMethodName) {
        for (Method method : methods) {
            if (fieldSetMethodName.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods            对象方法
     * @param fieldGetMethodName Get方法名
     * @return boolean
     */
    private static boolean existsGetMethod(Method[] methods, String fieldGetMethodName) {
        for (Method method : methods) {
            if (fieldGetMethodName.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName 属性名
     * @return String
     */
    private static String getGetMethodName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName 属性名
     * @return String
     */
    private static String getSetMethodName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


}
