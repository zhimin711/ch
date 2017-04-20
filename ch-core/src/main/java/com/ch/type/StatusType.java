package com.ch.type;

/**
 * 状态常量
 * Created by 80002023 on 2017/4/20.
 */
public enum StatusType {
    FAILED(0, "0", "失败"), //失败
    SUCCESS(1, "1", "成功"), //成功
    ERROR(2, "2", "错误"), //错误
    SELECTED(1, "1", "选中"), //选中
    ENABLED(1, "1", "启用"), //启用
    DISABLED(0, "0", "禁用"), //禁用
    YES(1, "1", "是"), //是
    NO(0, "0", "否"), //否
    IS(1, "1", "是"), //是
    NON(0, "0", "否"), //否
    UNKNOW(-1, "-1", "未知");
    private final Integer num;
    private final String code;
    private final String name;

    StatusType(Integer num, String code, String name) {
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
}
