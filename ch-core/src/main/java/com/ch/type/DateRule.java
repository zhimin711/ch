package com.ch.type;

/**
 * 日期规则
 *
 * @author 01370603
 * @date 2018/8/17 16:50
 */
public enum DateRule {
    /**
     * 每天
     */
    DAY,
    /**
     * 每周
     */
    WEEK,
    /**
     * 每月
     */
    MONTH,
    /**
     * 季度
     */
    QUARTERLY,
    /**
     * 年
     */
    YEAR,
    UNKNOWN;

    public static DateRule fromCode(String code) {
        if (code == null) return UNKNOWN;
        String tmp = code.toUpperCase();
        switch (tmp) {
            case "DAY":
                return DAY;
            case "WEEK":
                return WEEK;
            case "MONTH":
                return MONTH;
            case "QUARTERLY":
                return QUARTERLY;
            case "YEAR":
                return YEAR;
            default:
                return UNKNOWN;
        }
    }
}
