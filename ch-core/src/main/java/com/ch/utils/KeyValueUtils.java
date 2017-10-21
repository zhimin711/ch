package com.ch.utils;

import com.ch.pojo.KeyValue;

import java.util.List;
import java.util.stream.Collectors;

/**
 * KeyValue Utils
 *
 * @author zhimin
 */
public class KeyValueUtils {

    private KeyValueUtils() {
    }

    /**
     * Get list keyValue by keytils
     *
     * @param records KeyValue集合
     * @param key     Key键值
     * @return key的值
     */
    public static KeyValue getKeyValueByKey(final List<KeyValue> records, final String key) {
        if (CommonUtils.isEmpty(records)) {
            return null;
        }
        List<KeyValue> result = records.stream().filter(r -> CommonUtils.isEquals(key, r.getKey())).collect(Collectors.toList());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
