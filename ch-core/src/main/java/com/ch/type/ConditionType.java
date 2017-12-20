package com.ch.type;

public enum ConditionType {
    /**
     * 空
     */
    NULL,
    /**
     * 不为空
     */
    NOT_NULL,
    /**
     * ＝等于
     */
    EQUAL_TO,
    /**
     * <>不等于
     */
    NOT_EQUAL_TO,
    /**
     * >大于
     */
    GREATER_THAN,
    /**
     * >=大于等于
     */
    GREATER_THAN_OR_EQUAL_TO,
    /**
     * <小于
     */
    LESS_THAN,
    /**
     * <=小于等于
     */
    LESS_THAN_OR_EQUALTO,
    /**
     * 包含
     */
    IN,
    /**
     * 不包含
     */
    NOT_IN,
    /**
     * 区间
     */
    BETWEEN
}
