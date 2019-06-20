package com.ch.doc.poi;

import java.util.ArrayList;
import java.util.Collection;

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
public class RecordDefine {

    // 列定义
    private Collection<ColumnDefine> columns;

    public RecordDefine() {
        // this.readWorkBook = readWorkBook;HSSFWorkbook readWorkBook
        this.columns = new ArrayList<>();
    }

    public Collection<ColumnDefine> getColumns() {
        return columns;
    }

    public void addColumn(ColumnDefine column) {
        this.columns.add(column);
    }

    public void addColumnRec(String propName, String title, int index, String type) {
        ColumnDefine column = addColumn(propName, title, index, type);
        addColumn(column);
    }

    private ColumnDefine addColumn(String propName, String title, int index, String type) {
        ColumnDefine column = new ColumnDefine();
        column.setPropName(propName);
        column.setTitle(title);
        column.setIndex(index);
        column.setType(type);
        return column;
    }

    public void addColumnRec(String propName, String title, int index, String type, String cellType) {
        ColumnDefine column = addColumn(propName, title, index, type);
        column.setCellType(cellType);
        addColumn(column);
    }

}
