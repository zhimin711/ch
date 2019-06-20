package com.ch.doc.poi;


import lombok.Data;

/**
 * *********************************************
 * Copyright ch.
 * All rights reserved.
 * Description: 数据导入、导出的列定义
 * HISTORY
 * *********************************************
 * ID   DATE           PERSON          REASON
 * *********************************************
 *
 * @author 01370603
 */
@Data
public class ColumnDefine {

    // 属性名称
    private String propName;
    // 列标题
    private String title;
    // 列位置，在导入时，用于匹配列与属性
    private int index;
    /**
     * 列类型是否为必填
     */
    private String type = "N";

    private String cellType;

}
