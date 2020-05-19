package com.ch.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * 对象工具
 *
 * @author 01370603
 * @date 2017/4/26.
 */
public class BeanExtUtils {

    private final static Logger logger = LoggerFactory.getLogger(BeanExtUtils.class);

    private BeanExtUtils() {
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
     * @param isOverride    是否覆盖
     */
    public static void setFieldValue(Object target, Map<String, ?> fieldValueMap, boolean isOverride) {
        if (fieldValueMap == null || fieldValueMap.isEmpty()) return;
        Class<?> cls = target.getClass();
        // 取出bean里的所有方法
//        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        fieldValueMap.forEach((k, v) -> {
            if (CommonUtils.isEmpty(v)) return;
            for (Field field : fields) {
                if (!CommonUtils.isEquals(k, field.getName())) {
                    continue;
                }
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                    if (!isOverride) {//判断是否有值
                        Method method = pd.getReadMethod();
                        if (method == null) continue;
                        Object v1 = method.invoke(target);
                        if (v1 != null) {
                            continue;
                        }
                    }
                    Method method = pd.getWriteMethod();
                    if (method == null) continue;
                    Object val = parseValue(field.getType(), v);
                    if (CommonUtils.isNotEmpty(val)) {
                        method.invoke(target, val);
                    }
                } catch (Exception e) {
                    logger.error("setFieldValue Error!", e);
                }
            }
        });

    }

    private static Object parseValue(Class<?> type, Object value) {
        if (CommonUtils.isEmpty(value)) return null;
        if (type.isInstance(value)) return value;
        Object obj = null;
        String fieldType = type.getSimpleName();
        boolean isStr = value instanceof String;
        boolean isNum = value instanceof Number;
        boolean isDate = value instanceof Date;
        if ("String".equals(fieldType)) {
            obj = isNum ? value.toString() : (isDate ? DateUtils.format((Date) value) : null);
        } else if ("Date".equals(fieldType) || "Timestamp".equals(fieldType)) {
            obj = isStr ? DateUtils.parse((String) value) : null;
        } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
            obj = isNum ? ((Number) value).intValue() : null;
            if (obj == null && isStr) {
                obj = Integer.parseInt(value.toString());
            }
        } else if ("Long".equalsIgnoreCase(fieldType) || "long".equals(fieldType)) {
            obj = isNum ? ((Number) value).longValue() : null;
            if (obj == null && isStr) {
                obj = Long.valueOf(value.toString());
            }
        } else if ("Double".equalsIgnoreCase(fieldType) || "double".equals(fieldType)) {
            obj = isNum ? ((Number) value).doubleValue() : null;
            if (obj == null && isStr) {
                obj = Double.valueOf(value.toString());
            }
        } else if ("Float".equalsIgnoreCase(fieldType) || "float".equals(fieldType)) {
            obj = isNum ? ((Number) value).floatValue() : null;
            if (obj == null && isStr) {
                obj = Float.valueOf(value.toString());
            }
        } else if ("Boolean".equalsIgnoreCase(fieldType) || "boolean".equals(fieldType)) {
            if (isStr) {
                if ("true".equalsIgnoreCase(value.toString()) || "1".equalsIgnoreCase(value.toString())) {
                    obj = true;
                } else if ("false".equalsIgnoreCase(value.toString()) || "0".equalsIgnoreCase(value.toString())) {
                    obj = false;
                }
            }
            if (isNum) {
                if (value.equals(0)) {
                    obj = false;
                } else if (value.equals(1)) {
                    obj = true;
                }
            }
        } else {
            logger.info("not support type" + fieldType);
        }
        return obj;
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
     * 取Bean声明的属性和值对应关系的MAP(注过滤空属性)
     *
     * @param bean 目标对象
     * @return Map
     */
    public static Map<String, Object> getDeclaredFieldValueMap(Object bean) {
        return getDeclaredFieldValueMap(bean, true);

    }


    /**
     * 取Bean声明的属性和值对应关系的MAP
     *
     * @param bean         目标对象
     * @param isFilterNull 是否过滤空值
     * @return Map
     */
    public static Map<String, Object> getDeclaredFieldValueMap(Object bean, boolean isFilterNull) {
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
                if (fieldVal != null || !isFilterNull) {
                    valueMap.put(field.getName(), fieldVal);
                }
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
            if (CommonUtils.isEmpty(fieldName)) {
                return null;
            }
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
            if (CommonUtils.isEmpty(fieldName)) {
                return null;
            }
            Method m = obj.getClass().getMethod(getGetMethodName(fieldName));
            return m.invoke(obj);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 比较两个实体属性值，返回一个boolean,true则表时两个对象中的属性值无差异
     *
     * @param oldObject 进行属性比较的对象1
     * @param newObject 进行属性比较的对象2
     * @param properties 比较的对象属性
     * @return 属性差异比较结果boolean
     */
    public static boolean compareObject(Object oldObject, Object newObject, String... properties) {
        Map<String, Map<String, Object>> resultMap = compareFields(oldObject, newObject, properties);
        return resultMap.size() <= 0;
    }

    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个Map分别存oldObject,newObject此属性名的值
     *
     * @param oldObject 进行属性比较的对象1
     * @param newObject 进行属性比较的对象2
     * @param properties 比较的对象属性
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Map<String, Object>> compareFields(Object oldObject, Object newObject, String... properties) {
        Map<String, Map<String, Object>> map = null;

        try {
            /**
             * 只有两个对象都是同一类型的才有可比性
             */
            if (oldObject.getClass() == newObject.getClass()) {
                map = new HashMap<>();

                Class clazz = oldObject.getClass();
                //获取object的所有属性
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();

                for (PropertyDescriptor pd : pds) {
                    //遍历获取属性名
                    String name = pd.getName();
                    if (!Lists.newArrayList(properties).contains(name)) {
                        continue;
                    }
                    //获取属性的get方法
                    Method readMethod = pd.getReadMethod();

                    // 在oldObject上调用get方法等同于获得oldObject的属性值
                    Object oldValue = readMethod.invoke(oldObject);
                    // 在newObject上调用get方法等同于获得newObject的属性值
                    Object newValue = readMethod.invoke(newObject);

                    if (oldValue instanceof Collection) {
                        continue;
                    }

                    if (newValue instanceof Collection) {
                        continue;
                    }

                    if (oldValue instanceof Timestamp) {
                        oldValue = new Date(((Timestamp) oldValue).getTime());
                    }

                    if (newValue instanceof Timestamp) {
                        newValue = new Date(((Timestamp) newValue).getTime());
                    }

                    if (oldValue == null && newValue == null) {
                        continue;
                    } else if (oldValue == null) {
                        Map<String, Object> valueMap = new HashMap<String, Object>();
                        valueMap.put("oldValue", null);
                        valueMap.put("newValue", newValue);
                        map.put(name, valueMap);
                        continue;
                    }

                    if (!oldValue.equals(newValue)) {// 比较这两个值是否相等,不等就可以放入map了
                        Map<String, Object> valueMap = new HashMap<String, Object>();
                        valueMap.put("oldValue", oldValue);
                        valueMap.put("newValue", newValue);

                        map.put(name, valueMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

}
