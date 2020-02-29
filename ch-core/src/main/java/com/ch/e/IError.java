package com.ch.e;

/**
 * 错误接口
 *
 * @author 01370603
 */
public interface IError {

    String formatMsg(Object... args);

    enum Type {
        PUBLIC, CUSTOMER, UNKNOWN
    }

    Type getType();

    String getCode();

    String getName();
}
