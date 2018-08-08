package com.ch.result;

/**
 * 描述：定义错误枚举类
 *
 * @author 80002023
 * 2017/3/10.
 * @version 1.0
 * @since 1.8
 */
public enum ErrorCode {
    DEFAULT("0", "默认错误"),//不使用
    ADD("100", "添加失败"),//
    CREATE("101", "创建失败"),//
    UPDATE("102", "更新失败"),//
    DELETE("103", "删除失败"),//
    USERNAME("200", "账号错误"),//
    PASSWORD("201", "密码错误"),//
    NOT_ALLOWED("300", "不允许"),//
    NOT_AUTH("403", "未授权"),//
    UNKNOWN("-1", "未知错误");

    private final String code;
    private final String name;

    ErrorCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ErrorCode fromCode(String code) {
        if(code == null){
           return UNKNOWN;
        }
        switch (code) {
            case "100":
                return ADD;
            case "101":
                return CREATE;
            case "102":
                return UPDATE;
            case "103":
                return DELETE;
            case "104":
            case "105":
                break;
            case "200":
                return USERNAME;
            case "201":
                return PASSWORD;
            case "202":
            case "203":
            case "204":
            case "205":
                break;
            case "300":
                return NOT_ALLOWED;
            case "403":
                return NOT_AUTH;
            default:
                return DEFAULT;
        }
        return UNKNOWN;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
