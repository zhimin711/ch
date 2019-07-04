package com.ch;

/**
 * 状态常量
 *
 * @author zhimin.ma on 2019/3/20.
 */
public enum Status {
    FAILED("0"), //失败
    SUCCESS("1"), //成功
    ERROR("2"), //错误
    DELETE("3"), //超时
    TIMEOUT("4"), //超时
    UNSELECTED("0"), //选中
    SELECTED("1"), //选中
    ENABLED("1"), //启用
    DISABLED("0"), //禁用
    YES("1"), //是
    NO("0"), //否
    UNKNOWN("-1");//未知

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public Integer getNum() {
        return Integer.parseInt(code);
    }

    public String getCode() {
        return code;
    }

    /**
     * 两个状态之前判断输入值
     *
     * @param value 输入值
     * @param zero  状态1
     * @param one   状态2
     * @return 状态1/2
     */
    private static Status getStatus(Object value, Status zero, Status one) {
        String tmp = toCode(value);
        switch (tmp) {
            case "0":
                return zero;
            case "1":
                return one;
            default:
                return UNKNOWN;
        }
    }

    public static Status fromValue(Object value) {
        String tmp = toCode(value);
        switch (tmp) {
            case "0":
                return FAILED;
            case "1":
                return SUCCESS;
            case "2":
                return ERROR;
            case "3":
                return DELETE;
            case "4":
                return TIMEOUT;
            default:
                return UNKNOWN;
        }
    }

    public static Status forSelected(Object value) {
        return getStatus(value, UNSELECTED, SELECTED);
    }

    public static Status forEnabled(Object value) {
        return getStatus(value, DISABLED, ENABLED);
    }

    public static Status forYes(Object value) {
        return getStatus(value, NO, YES);
    }

    private static String toCode(Object value) {
        String tmp = "-1";
        if (value == null) {
            return "-1";
        }
        if (value instanceof Number) {
            tmp = String.valueOf(((Number) value).intValue());
        } else if (value instanceof String) {
            tmp = (String) value;
        }
        return tmp;
    }

    public static boolean isSuccess(Object value) {
        return Status.fromValue(value) == Status.SUCCESS;
    }

    public static boolean isDelete(Object value) {
        return Status.fromValue(value) == Status.DELETE;
    }

    public boolean isSuccess() {
        return this == Status.SUCCESS;
    }

    public boolean isError() {
        return this == Status.ERROR;
    }

    public boolean isSelected() {
        return this == Status.ERROR;
    }

    public boolean isEnabled() {
        return this == Status.ENABLED;
    }
}
