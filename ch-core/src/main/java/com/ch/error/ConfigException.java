package com.ch.error;

/**
 * 未知异常
 * Created by 01370603 on 2017/11/10.
 */
public class ConfigException extends RuntimeException {

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public ConfigException() {
        super();
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     */
    public ConfigException(String s) {
        super(s);
    }
}
