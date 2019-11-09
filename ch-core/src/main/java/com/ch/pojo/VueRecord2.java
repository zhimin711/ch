package com.ch.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhimin
 * @date 2018/12/29 10:54
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VueRecord2 extends VueRecord {

    /**
     * 关键字
     */
    private String key;
}
