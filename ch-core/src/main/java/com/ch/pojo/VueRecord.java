package com.ch.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhimin
 * @date 2018/12/29 10:54
 */
@Data
public class VueRecord implements Serializable {

    /**
     * 值
     */
    private String value;
    /**
     * 显示名称
     */
    private String label;
    /**
     * 禁用
     */
    private boolean disabled = false;
    /**
     * 子集
     */
    private List<VueRecord> children;

}
