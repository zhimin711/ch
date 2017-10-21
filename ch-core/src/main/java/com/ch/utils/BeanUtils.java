package com.ch.utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * BeanUtils
 * Created by 80002023 on 2017/4/26.
 */
public class BeanUtils {

    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

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
