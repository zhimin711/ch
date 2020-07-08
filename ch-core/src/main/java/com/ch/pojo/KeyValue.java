package com.ch.pojo;

import java.util.Date;

/**
 * 描述：com.ch.pojo
 *
 * @author 80002023
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class KeyValue {
    /**
     * 键值
     */
    private String key;
    /**
     * 值
     */
    private String value;
    private Date date;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(Number key, String value) {
        this.key = key + "";
        this.value = value;
    }

    public KeyValue(Number key, Number value) {
        this.key = key + "";
        this.value = value + "";
    }

    public KeyValue(String key, String value, Date date) {
        this.key = key;
        this.value = value;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
