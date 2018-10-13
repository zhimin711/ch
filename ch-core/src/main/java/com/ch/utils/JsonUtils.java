package com.ch.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
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
public class JsonUtils {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    /**
     * 工具类型
     */
    public enum ToolType {
        GSON, JACKSON
    }

    /**
     * use gson plugin format object to json
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return toJson(object, ToolType.GSON);
        } catch (Exception e) {
            logger.error("object to json string error!", e);
        }
        return "";
    }

    /**
     * format object to json
     *
     * @param object 任意对象
     * @param type   格式化工具类型
     * @return Json格式化字符串
     */
    public static String toJson(Object object, ToolType type) {
        if (object == null) {
            return null;
        }
        switch (type) {
            case GSON:
                return toJson2(object);
            case JACKSON:
                return toJsonOfJackson(object);
            default:
                return toJsonOfJackson(object);

        }
    }


    /**
     * use gson plugin format object to json
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    private static String toJson2(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return newInstance().toJson(object);
        } catch (Exception e) {
            logger.error("use gson format object to json string error!", e);
        }
        return "";
    }

    /**
     * use jackson plugin format object to json
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    private static String toJsonOfJackson(Object object) {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("use jackson format object to json string error!", e);
        }
        return "";
    }

    /**
     * use gson plugin format object to json and Without Expose
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    public static String toJsonWithoutExpose(Object object) {
        if (object == null) {
            return null;
        }
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.excludeFieldsWithoutExposeAnnotation();
            Gson gson = gb.create();
            return gson.toJson(object);
        } catch (Exception e) {
            logger.error("use gson plugin format object to json and Without Expose error!", e);
        }
        return "";
    }

    /**
     * use gson plugin format object to json and Without Expose
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    public static String toJsonDateFormat(Object object, DateUtils.Pattern pattern) {
        if (object == null) {
            return null;
        }
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.setDateFormat(pattern.getValue());
            gb.excludeFieldsWithoutExposeAnnotation();
            Gson gson = gb.create();
            return gson.toJson(object);
        } catch (Exception e) {
            logger.error("use gson plugin format object to json and Without date format error!", e);
        }
        return "";
    }

    /**
     * 解析Json反射对象
     * parse json to class
     *
     * @param json     json格式化字符串
     * @param classOfT 返回映射类
     * @return 返回实体对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        if (json == null) {
            return null;
        }
        try {
            return newInstance().fromJson(json, classOfT);
        } catch (Exception e) {
            logger.error("parse json to class error!", e);
        }
        return null;
    }

    public static <T> T fromJson(String json, ToolType type) {
        switch (type) {
            case GSON:
                return newInstance().fromJson(json, new TypeToken<T>() {
                }.getType());
            case JACKSON:
                return fromJson(json);
        }
        return null;
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
            logger.error("parse json to class error!", e);
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
            logger.error("parse json to class error!", e);
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
            logger.error("parse array json string to List T error!", e);
        }
        return null;
    }

    /**
     * Gson
     * new TypeToken<List<KeyValue>>() {}.getType()
     *
     * @param arrayJson json格式化数组
     * @param typeOfT   TypeToken {@link TypeToken}
     * @return 实体集合
     */
    public static <T> List<T> fromJsonToList(String arrayJson, Type typeOfT) {
        if (arrayJson == null) {
            return null;
        }

        try {
            List<T> records = newInstance().fromJson(arrayJson, typeOfT);
            if (records != null) {
                return records;
            }
        } catch (Exception e) {
            logger.error("parse array json string to List T error!", e);
        }
        return null;
    }

    private static Gson newInstance() {
        return new Gson();
    }
}
