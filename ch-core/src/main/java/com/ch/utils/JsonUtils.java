package com.ch.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class JsonUtils {

    private JsonUtils(){}

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
            return newInstance().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析Json反射对象
     * parse json to class
     *
     * @param json json格式化字符串
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param json json格式化字符串
     * @return
     */
    public static Map<String, Object> fromJson(String json) {
        if (json == null) {
            return null;
        }
        try {
            /*ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;

            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });

            logger.info("fromJson: {}", map);*/
//            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Class<T> clazz
     *
     * @param arrayJson json格式化数组
     * @return 实体集合
     */
    public static <T> List<T> fromJsonToList(String arrayJson) {
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
            e.printStackTrace();
        }
        return null;
    }

    private static Gson newInstance() {
        return new Gson();
    }
}
