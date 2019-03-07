package com.ch.doc.poi;


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
public class ColumnDefine {

    // 属性名称
    private String propName;
    // 列标题
    private String title;
    // 列位置，在导入时，用于匹配列与属性
    private int index;
    //列类型
    private String type;

    private String cellType;

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
