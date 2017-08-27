package com.ch.mybatis.exception;

/**
 * 自定义Mybatis异常
 */
public class MybatisException extends RuntimeException {

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }
}
