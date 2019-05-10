package com.ch.e;

/**
 * 描述：公共错误
 *
 * @author zhimin.ma
 * 2019/3/10.
 * @version 1.0
 * @since 1.8
 */
public enum PubError implements IError {
    DEFAULT("0", "默认错误"),//不使用
    ARGS("10", "参数错误/无效"),//
    CONFIG("11", "配置错误/无效"),//
    CONNECT("12", "连接错误/无效"),//
    ADD("100", "添加失败"),//
    CREATE("101", "创建失败"),//
    UPDATE("102", "更新失败"),//
    DELETE("103", "删除失败"),//
    NOT_LOGIN("200", "未登录"),//
    USERNAME("201", "账号错误"),//
    PASSWORD("202", "密码错误"),//
    USERNAME_OR_PASSWORD("203", "账号或密码不正确"),//
    NOT_ALLOWED("300", "不允许"),//
    NOT_EXISTS("301", "不存在"),//
    NON_NULL("302", "不为空"),//
    EXISTS("303", "已存在"),//
    INVALID("304", "无效"),//
    OUT_OF_LIMIT("305", "溢出/超出限制"),//
    NOT_AUTH("403", "未授权"),//UNAUTHORIZED
    UNKNOWN("-1", "未知错误");

    private final String code;
    private final String name;

    PubError(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PubError fromCode(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        switch (code) {
            case "10":
                return ARGS;
            case "11":
                return CONFIG;
            case "12":
                return CONNECT;
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
                return NOT_LOGIN;
            case "201":
                return USERNAME;
            case "202":
                return PASSWORD;
            case "203":
                return USERNAME_OR_PASSWORD;
            case "204":
            case "205":
                break;
            case "300":
                return NOT_ALLOWED;
            case "301":
                return NOT_EXISTS;
            case "302":
                return NON_NULL;
            case "303":
                return EXISTS;
            case "304":
                return INVALID;
            case "305":
                return OUT_OF_LIMIT;
            case "403":
                return NOT_AUTH;
            default:
                return DEFAULT;
        }
        return UNKNOWN;
    }


    @Override
    public Type getType() {
        return Type.PUBLIC;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
