package com.ch.type;

/**
 * 状态常量
 * Created by 80002023 on 2017/4/20.
 */
public enum Status {
    FAILED(0, "0", "失败"), //失败
    SUCCESS(1, "1", "成功"), //成功
    ERROR(2, "2", "错误"), //错误
    SELECTED(1, "1", "选中"), //选中
    ENABLED(1, "1", "启用"), //启用
    DISABLED(0, "0", "禁用"), //禁用
    YES(1, "1", "是"), //是
    NO(0, "0", "否"), //否
    HAS(1, "1", "有"), //有
    NON(0, "0", "空"), //空
    UNKNOWN(-1, "-1", "未知");

    private final Integer num;
    private final String code;
    private final String name;

    Status(Integer num, String code, String name) {
        this.num = num;
        this.code = code;
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Status fromValue(Object value) {
        if (value == null) {
            return UNKNOWN;
        }
        String tmp;
        if(value instanceof Number){
            tmp = String.valueOf(value);
        }else if(value instanceof String){
            tmp = (String) value;
        }else {
            return UNKNOWN;
        }
        switch (tmp){
            case "0":
            case "失败":
                return FAILED;
            case "1":
            case "成功":
                return SUCCESS;
            case "2":
                return ERROR;
            case "选中":
                return SELECTED;
            case "启用":
                return ENABLED;
            case "禁用":
                return DISABLED;
            case "是":
                return YES;
            case "否":
                return NO;
            case "有":
                return HAS;
            case "空":
                return NON;
            default:
                return UNKNOWN;
        }
    }

    public static boolean isSuccess(Object value){
        return Status.fromValue(value) == Status.SUCCESS;
    }
}
