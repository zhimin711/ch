package com.ch.http;

/**
 * 描述：定义错误枚举类
 *
 * @author 80002023
 *         2017/3/10.
 * @version 1.0
 * @since 1.8
 */
public enum ErrorCode {
    DEFAULT("100", "默认错误"),//不使用
    NOT_ALLOWED("300", "不允许"),//
    NOT_AUTH("403", "未授权"),//
    ERROR("-1", "未知错误");

    private final String code;
    private final String name;

    ErrorCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ErrorCode fromCode(String code) {
        switch (code) {
            case "100":
                return DEFAULT;
            case "101":
            case "102":
            case "103":
            case "104":
            case "105":
                break;
            case "200":
            case "201":
            case "202":
            case "203":
            case "204":
            case "205":
                break;
            case "300":
                return NOT_ALLOWED;
            case "403":
                return NOT_AUTH;
        }
        return ERROR;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
