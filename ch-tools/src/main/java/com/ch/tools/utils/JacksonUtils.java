package com.ch.tools.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
@Slf4j
public class JacksonUtils {

    private JacksonUtils() {
    }

    /**
     * use jackson plugin format object to json
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    private static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("use jackson format object to json string error!", e);
        }
        return "";
    }

    /**
     * 解析JSON字符串
     *
     * @param json json string
     * @param <T>  Object
     * @return - 返回实体对象
     */
    public static <T> T fromJson(String json) {
        return fromJson(json, new TypeReference<T>() {
        });
    }

    /**
     * 解析JSON字符串
     *
     * @param json json string
     * @param type {@link TypeReference}
     * @param <T>  返回映射类
     * @return 返回实体对象
     */
    public static <T> T fromJson(String json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON string to <T>
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("parse json to class error!", e);
        }
        return null;
    }

    /**
     * Json 转 Map
     *
     * @param json json格式化字符串
     * @return Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        if (json == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;

            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            return map;
        } catch (Exception e) {
            log.error("parse json to class error!", e);
        }
        return null;
    }

    /**
     * parse array json string to List T
     * Class<T> clazz
     * new TypeReference<List<T>>{}
     *
     * @param arrayJson json格式化数组
     * @param type      {@link TypeReference}
     * @return 实体集合
     */
    public static <T> List<T> fromJsonToList(String arrayJson, TypeReference<List<T>> type) {
        if (arrayJson == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<T> records = mapper.readValue(arrayJson, type);
            if (records != null) {
                return records;
            }
        } catch (Exception e) {
            log.error("parse array json string to List T error!", e);
        }
        return null;
    }
}
