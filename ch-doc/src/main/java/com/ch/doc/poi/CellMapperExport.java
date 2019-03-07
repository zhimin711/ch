package com.ch.doc.poi;

public class CellMapperExport {

    private String fieldName; // 属性名称，对应实体的属性名
    private boolean isLock = false; // 是否锁定，默认false
    private String dataFormat = null;

    /**
     * @param fieldName 属性名称
     */
    public CellMapperExport(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @param fieldName 属性名称
     * @param isLock    是否锁定
     */
    public CellMapperExport(String fieldName, boolean isLock) {
        this.fieldName = fieldName;
        this.isLock = isLock;
    }

    public CellMapperExport(String fieldName, boolean isLock, String dataFormat) {
        this.fieldName = fieldName;
        this.isLock = isLock;
        this.dataFormat = dataFormat;
    }

    public CellMapperExport(String fieldName, String dataFormat) {
        this.fieldName = fieldName;
        this.dataFormat = dataFormat;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

}
