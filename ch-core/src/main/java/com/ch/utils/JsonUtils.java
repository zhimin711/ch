package com.ch.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    enum ToolType {
        GSON, JACKSON, JSON
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
                return toJsonOfGson(object);
            case JACKSON:
                return toJsonOfJackson(object);
            default:
                return toJsonOfGson(object);

        }
    }


    /**
     * use gson plugin format object to json
     *
     * @param object 任意对象
     * @return json Json格式化字符串
     */
    private static String toJsonOfGson(Object object) {
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

    /**
     * Json 转 Map
     *
     * @param json json格式化字符串
     * @return Map
     */
    public static Map<String, Object> fromJson(String json) {
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
     *
     * @param arrayJson json格式化数组
     * @return 实体集合
     */
    public static <T> List<T> fromJsonToList(String arrayJson) {
        if (arrayJson == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<T> records = mapper.readValue(arrayJson, new TypeReference<List<T>>() {
            });
            if (records != null) {
                return records;
            }
        } catch (Exception e) {
            logger.error("parse array json string to List T error!", e);
        }
        return null;
    }

    /**
     * Class<T> clazz
     *
     * @param arrayJson json格式化数组
     * @return 实体集合
     */
    public static <T> List<T> fromJsonToListOfGson(String arrayJson) {
        if (arrayJson == null) {
            return null;
        }

        try {
            List<T> records = newInstance().fromJson(arrayJson, new TypeToken<T>() {
            }.getType());
            if (records != null) {
//                logger.info("fromJson: string to list size {}", records.size());
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
