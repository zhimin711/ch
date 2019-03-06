package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象工具
 *
 * @author 01370603
 * @date 2017/4/26.
 */
public class BeanUtils {

    private final static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    private BeanUtils() {
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
                    logger.error("set target object of property value error!", e);
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
     * 取Bean声明的属性和值对应关系的MAP
     *
     * @param bean 目标对象
     * @return Map
     */
    public static Map<String, String> getDeclaredFieldValueMap2(Object bean) {
        Map<String, String> valueMap = new HashMap<>();
        Map<String, Object> map = getDeclaredFieldValueMap(bean);
        if (map.isEmpty()) return valueMap;
        map.forEach((k, v) -> {
            if (v == null) {
                return;
            }
            String tmp = v.toString();
            if (v instanceof Date) {
                tmp = DateUtils.format((Date) v);
            }
            valueMap.put(k, tmp);
        });
        return valueMap;
    }


    /**
     * 取Bean声明的属性和值对应关系的MAP
     *
     * @param bean 目标对象
     * @return Map
     */
    public static Map<String, Object> getDeclaredFieldValueMap(Object bean) {
        Map<String, Object> valueMap = new HashMap<>();
        if (bean == null) return valueMap;
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String fieldGetName = getGetMethodName(field.getName());
                if (!existsGetMethod(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName);
                Object fieldVal = fieldGetMet.invoke(bean);
                valueMap.put(field.getName(), fieldVal);
            } catch (Exception e) {
                logger.error("getDeclaredFieldValueMap Error!", e);
            }
        }
        return valueMap;

    }


    /**
     * 取Bean的属性和值对应关系的MAP
     *
     * @param bean 目标对象
     * @return Map
     */
    public static Map<String, String> getKeyValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, String> valueMap = new HashMap<>();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();

        for (Method method : methods) {
            try {
                String methodName = method.getName();
                String returnTypeName = method.getReturnType().getName();
                if ("void".equals(returnTypeName) || methodName.startsWith("set")) {
                    continue;
                }
//                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
//                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
                Object fieldVal = method.invoke(bean);
                String result = null;
                if ("Date".equals(returnTypeName) || "Timestamp".equals(returnTypeName)) {
                    result = DateUtils.format((Date) fieldVal);
                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                valueMap.put(methodName, result);
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

    /**
     * 根据方法名取属性值
     *
     * @param obj    目标对象
     * @param method 方法名
     * @param <T>    返回类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueByMethodName(Object obj, String method) {
        try {
            Method m = obj.getClass().getMethod(method);
            return (T) m.invoke(obj);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 根据属性名取属性值
     *
     * @param obj       目标对象
     * @param fieldName 属性名
     * @param <T>       返回类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue2ByProperty(Object obj, String fieldName) {
        try {
            return (T) getValueByProperty(obj, fieldName);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 根据属性名取属性值
     *
     * @param obj       目标对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getValueByProperty(Object obj, String fieldName) {
        try {
            Method m = obj.getClass().getMethod(getGetMethodName(fieldName));
            return m.invoke(obj);
        } catch (Exception ignored) {
        }
        return null;
    }


}
