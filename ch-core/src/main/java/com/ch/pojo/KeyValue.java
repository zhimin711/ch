package com.ch.pojo;

import java.util.Date;

/**
 * 描述：com.ch.pojo
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class KeyValue {

    private String key;
    private String value;
    private Date expires;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, String value, Date expires) {
        this.key = key;
        this.value = value;
        this.expires = expires;
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

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
