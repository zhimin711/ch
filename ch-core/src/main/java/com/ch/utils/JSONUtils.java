package com.ch.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class JSONUtils {

    private final static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    private JSONUtils() {
    }

    /**
     * use gson plugin format object to json
     *
     * @param object 任意对象
     * @return Json格式化字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return newInstance().toJson(object);
        } catch (Exception e) {
            logger.error("use gson format object to json string error!", e);
        }
        return "{}";
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
//            gb.excludeFieldsWithoutExposeAnnotation();
//            gb.addSerializationExclusionStrategy()
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

    /**
     * 解析Json反射对象
     * parse json to class
     *
     * @param json     json格式化字符串
     * @param classOfT 返回映射类
     * @param pattern  时间格式化
     * @return 返回实体对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT, DateUtils.Pattern pattern) {
        if (json == null) {
            return null;
        }
        try {
            return newInstance(pattern).fromJson(json, classOfT);
        } catch (Exception e) {
            logger.error("parse json with date pattern to class error!", e);
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
    public static <T> List<T> fromJson(String arrayJson, Type typeOfT) {
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

    private static Gson newInstance(DateUtils.Pattern pattern) {
        return new GsonBuilder().setDateFormat(pattern.getValue()).create();
    }

    private static Gson newInstance2(Config config) {
        //：内部类(Inner Class)和嵌套类(Nested Class)的区别
        GsonBuilder gb = new GsonBuilder();
        if (config.nonNull) {
            gb.serializeNulls();
        }
        gb.setDateFormat(config.pattern.getValue());
        if (!config.innerSerializable) gb.disableInnerClassSerialization();
        if (config.nonExecutableJson) gb.generateNonExecutableJson();
        if (config.disableHtmlEscaping) gb.disableHtmlEscaping();
        if (config.format) gb.setPrettyPrinting();
        return gb.create();
    }

    class Config {
        //序列化null
        private boolean nonNull = false;
        // 设置日期时间格式，另有2个重载方法
        private DateUtils.Pattern pattern = DateUtils.Pattern.DATE_CN;
        // 禁此序列化内部类
        private boolean innerSerializable = true;
        //生成不可执行的Json（多了 )]}' 这4个字符）
        private boolean nonExecutableJson = false;
        //格式化输出
        private boolean format = false;
        //禁止转义html标签
        private boolean disableHtmlEscaping = false;

        public void setNonNull(boolean nonNull) {
            this.nonNull = nonNull;
        }

        public void setPattern(DateUtils.Pattern pattern) {
            this.pattern = pattern;
        }

        public void setInnerSerializable(boolean innerSerializable) {
            this.innerSerializable = innerSerializable;
        }

        public void setNonExecutableJson(boolean nonExecutableJson) {
            this.nonExecutableJson = nonExecutableJson;
        }

        public void setFormat(boolean format) {
            this.format = format;
        }

        public void setDisableHtmlEscaping(boolean disableHtmlEscaping) {
            this.disableHtmlEscaping = disableHtmlEscaping;
        }
    }
}
