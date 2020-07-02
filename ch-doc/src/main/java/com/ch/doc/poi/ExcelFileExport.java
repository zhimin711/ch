package com.ch.doc.poi;

import com.ch.e.PubError;
import com.ch.utils.ExceptionUtils;
import com.ch.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
@Slf4j
public class ExcelFileExport {

    private SXSSFWorkbook workbook = null; // 工作簿
    private Sheet sheet = null; // 工作表，这里指第一个工作表
    private Sheet sheet1 = null;
    private int startRowIndex; // 开始行索引
    private int lastRowIndex; // 最后行索引
    private int defaultColumnWidth = 16; // 默认的列宽
    private int maxRecords = 60000; // 允许导出的最大记录行为6万条，必须是pageSize的整数倍
    // private String sheetName;
    private static final DateFormat sf = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 导出Excel
     *
     * @param startRowIndex 开始行索引，0表示第一行
     */
    public ExcelFileExport(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        // this.sheetName = sheetName;
        lastRowIndex = startRowIndex;

        workbook = new SXSSFWorkbook(1000);
        // 压缩临时文件
        workbook.setCompressTempFiles(true);
        sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(defaultColumnWidth);
    }

    /*
     * public Sheet createSheet(SXSSFWorkbook wb, String sheetName){ sheet =
     * wb.createSheet(sheetName);
     * sheet.setDefaultColumnWidth(defaultColumnWidth); return sheet; }
     */

    /**
     * 读取Excel模板导出Excel
     *
     * @param is            模板输入流
     * @param startRowIndex 开始行索引，0表示第一行
     */
    public ExcelFileExport(InputStream is, int startRowIndex) {
        this.startRowIndex = startRowIndex;
        lastRowIndex = startRowIndex;

        try {
            XSSFWorkbook xs = new XSSFWorkbook(is);
            workbook = new SXSSFWorkbook(xs);
        } catch (Exception e) {
            log.error("ExcelFileExport", e);
            throw ExceptionUtils.create(PubError.CREATE);
        } finally {
            IOUtils.close(is);
        }

        workbook.setCompressTempFiles(true);
        sheet = workbook.getSheetAt(0);
        sheet1 = workbook.getSheetAt(1);
    }

    /**
     * 第一行写入表头
     *
     * @param headers 表头值数组
     */
    public void writeHeader(Object[] headers) {
        writeHeader(headers, 0);
    }

    /**
     * 写入表头
     *
     * @param headers        表头值数组
     * @param headerRowIndex 表头行号,0表示第一行
     */
    public void writeHeader(Object[] headers, int headerRowIndex) {
        Row row = sheet.createRow(headerRowIndex);// 写入第一行
        CellStyle headerStyle = getHeaderStyle(workbook);
        Cell cell = null;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            setCellValue(cell, headers[i]);
        }
    }

    /**
     * 写入表头
     *
     * @param headers        表头值数组
     * @param headerRowIndex 表头行号,0表示第一行
     */
    public void writeHeaderRed(Object[] headers, int headerRowIndex) {
        Row row = sheet.createRow(headerRowIndex);// 写入第一行
        CellStyle headerStyle = getHeaderStyleRed(workbook);
        CellStyle style = getHeaderStyle(workbook);
        Cell cell = null;
        // 给表头第一列设置红色样式
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        setCellValue(cell, headers[0]);
        for (int i = 1; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            setCellValue(cell, headers[i]);
        }
    }

    /**
     * 获取表头的样式
     *
     * @return
     */
    protected CellStyle getHeaderStyle(SXSSFWorkbook workbook) {
        // sheet页标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        short cIndex = IndexedColors.GREY_25_PERCENT.getIndex();
        headerStyle.setFillForegroundColor(cIndex);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 设置边框线条
        short borderThin = CellStyle.BORDER_THIN;
        headerStyle.setBorderTop(borderThin);
        headerStyle.setBorderBottom(borderThin);
        headerStyle.setBorderLeft(borderThin);
        headerStyle.setBorderRight(borderThin);
        // 设置字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("微软雅黑");
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * 获取表头的样式,设置表头第一列为红色
     *
     * @return
     */
    protected CellStyle getHeaderStyleRed(SXSSFWorkbook workbook) {
        // sheet页标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        short cIndex = IndexedColors.GREY_25_PERCENT.getIndex();
        headerStyle.setFillForegroundColor(cIndex);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 设置边框线条
        short borderThin = CellStyle.BORDER_THIN;
        headerStyle.setBorderTop(borderThin);
        headerStyle.setBorderBottom(borderThin);
        headerStyle.setBorderLeft(borderThin);
        headerStyle.setBorderRight(borderThin);
        // 设置字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(RED.index);
        font.setBold(true);
        font.setFontHeightInPoints((short) 9);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * 设置cell的值
     *
     * @param cell
     * @param value
     */
    private void setCellValue(Cell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue(sf.format((Date) value));
        }
    }

    /**
     * 页数据接口
     */
    public interface PageData<T> {
        List<T> getData(int pageSize, int pageStart);
    }

    /**
     * 向Excel文件中分页写入多行数据
     *
     * @param dataList             每页数据
     * @param cellMapperExportList Cell导出映射集合
     */
    public <T> void writeRowsByList(List<T> dataList, List<CellMapperExport> cellMapperExportList) {
        String[] fieldNames = new String[cellMapperExportList.size()];
        boolean[] isLocks = new boolean[cellMapperExportList.size()];
        String[] dataFormat = new String[cellMapperExportList.size()];
        int i = 0;
        boolean hasLock = false;
        for (CellMapperExport cellMapperExport : cellMapperExportList) {
            fieldNames[i] = cellMapperExport.getFieldName();
            isLocks[i] = cellMapperExport.isLock();
            dataFormat[i] = cellMapperExport.getDataFormat();
            if (cellMapperExport.isLock()) {
                hasLock = true;
            }
            i++;
        }
        if (!hasLock) {
            isLocks = null;
        }
        List<Object[]> dataListResult = new ArrayList<Object[]>();
        for (T entity : dataList) {
            dataListResult.add(entityToArray(entity, fieldNames));
        }
        if (dataListResult.size() > 0) {
            writeRows(dataListResult, isLocks, dataFormat);
        }
    }

    /**
     * 向Excel文件写入多行失败结果数据
     *
     * @param pageData             每页数据
     * @param cellMapperExportList Cell导出映射集合
     */
    public <T> void writeRowsByPage(PageData<T> pageData, List<CellMapperExport> cellMapperExportList) {
        String[] fieldNames = new String[cellMapperExportList.size()];
        boolean[] isLocks = new boolean[cellMapperExportList.size()];
        String[] dataFormat = new String[cellMapperExportList.size()];
        int i = 0;
        boolean hasLock = false;
        for (CellMapperExport cellMapperExport : cellMapperExportList) {
            fieldNames[i] = cellMapperExport.getFieldName();
            isLocks[i] = cellMapperExport.isLock();
            dataFormat[i] = cellMapperExport.getDataFormat();
            if (cellMapperExport.isLock()) {
                hasLock = true;
            }
            i++;
        }
        if (!hasLock) {
            isLocks = null;
        }

        int pageStart = 0;
        int pageSize = pageSize();
        boolean result = false;
        do {
            List<T> datas = pageData.getData(pageSize, pageStart);
            if (datas == null || datas.isEmpty()) {
                break;
            }
            pageStart = getNextPageStart(pageSize, pageStart);
            if (pageStart > maxRecords) {
                break;
            }
            List<Object[]> dataList = new ArrayList<Object[]>();
            for (T entity : datas) {
                dataList.add(entityToArray(entity, fieldNames));
            }

            writeRows(dataList, isLocks, dataFormat);
            result = datas.size() == pageSize;
        } while (result);
    }

    /**
     * 将实体属性转化为对象数组
     *
     * @param entity:实体
     * @param fieldNames:实体属性映射数组,可以是childEntity.fieldName/childEntity.fieldName2形式的,只支持一个.和一个/
     * @return
     */

    private <T> Object[] entityToArray(T entity, String[] fieldNames) {
        List<Object> objList = new ArrayList<Object>();
        Object value = null;
        for (String fieldName : fieldNames) {
            // 如果是entity.childEntity.fieldName的形式
            if (fieldName.contains("/")) {
                String[] fieldNameArr = fieldName.split("/");
                value = fieldHandler(entity, fieldNameArr[0]) + "/" + fieldHandler(entity, fieldNameArr[1]);
            } else {
                value = fieldHandler(entity, fieldName);
            }
            objList.add(value);
        }
        return objList.toArray();
    }

    private <T> Object fieldHandler(T entity, String fieldName) {
        Object value = null;
        try {
            if (fieldName.contains(".")) {
                String[] fieldNameArr = fieldName.split("\\.");
                String childEntityName = fieldNameArr[0];
                String childEntityFieldName = fieldNameArr[1];
                if (PropertyUtils.getProperty(entity, childEntityName) != null) {
                    value = PropertyUtils.getProperty(PropertyUtils.getProperty(entity, childEntityName), childEntityFieldName);
                }
                // value =
                // AssignValueForAttributeUtil.getAttrributeValue(AssignValueForAttributeUtil.getAttrributeValue(entity,childEntityName),childEntityFieldName);
            } else { // 如果是entity.fieldName的形式
                value = PropertyUtils.getProperty(entity, fieldName);
                // value =
                // AssignValueForAttributeUtil.getAttrributeValue(entity,
                // fieldName);
            }
        } catch (Exception e) {
            log.error("fieldHandler", e);
        }
        return value;
    }

    /**
     * 写多行数据
     *
     * @param rowValuesList 行值
     * @param isLocks       是否锁定数组
     */
    public void writeRows(List<Object[]> rowValuesList, boolean[] isLocks, String[] dataFormat) {
        CellStyle dataStyle = getDataStyle(workbook);
        CellStyle unLockStyle = null;
        if (isLocks != null) {
            unLockStyle = workbook.createCellStyle();
            unLockStyle.cloneStyleFrom(dataStyle);
            unLockStyle.setLocked(false);
        }
        Iterator<Object[]> iterator = rowValuesList.iterator();
        Object[] rowValues = null;
        while (iterator.hasNext()) {
            rowValues = iterator.next();
            writeRow(rowValues, dataStyle, isLocks, unLockStyle, dataFormat);
        }
    }

    /**
     * 获取数据的样式
     *
     * @return
     */
    protected CellStyle getDataStyle(SXSSFWorkbook workbook) {
        // sheet页数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        // 设置边框线条
        short borderDotted = CellStyle.BORDER_DOTTED;
        dataStyle.setBorderTop(borderDotted);
        dataStyle.setBorderBottom(borderDotted);
        dataStyle.setBorderLeft(borderDotted);
        dataStyle.setBorderRight(borderDotted);
        // 设置字体
        XSSFFont font2 = (XSSFFont) workbook.createFont();
        font2.setFontName("微软雅黑");
        font2.setFontHeightInPoints((short) 10);
        dataStyle.setFont(font2);
        return dataStyle;
    }

    private void writeRow(Object[] rowValues, CellStyle dataStyle, boolean[] isLocks, CellStyle unLockStyle, String[] dataFormat) {
        Row row = sheet.createRow(lastRowIndex);
        // Cell cell = row.createCell(0);
        // cell.setCellStyle(dataStyle);
        // cell.setCellValue(getSerialNumber(lastRowIndex));
        for (int i = 1; i <= rowValues.length; i++) {
            // cell = row.createCell(i);
            Cell cell = row.createCell(i - 1);
            if (isLocks == null || isLocks[i - 1]) {
                cell.setCellStyle(dataStyle);
            } else {
                unLockStyle = workbook.createCellStyle();
                unLockStyle.cloneStyleFrom(dataStyle);
                unLockStyle.setLocked(false);
                if (dataFormat[i - 1] != null) {
                    XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
                    unLockStyle.setDataFormat(format.getFormat(dataFormat[i - 1]));
                }
                cell.setCellStyle(unLockStyle);
            }
            setCellValue(cell, rowValues[i - 1]);
        }
        lastRowIndex++;
    }

    /**
     * 写入excel
     *
     * @param os:输出流
     */
    public void write(OutputStream os) {
        try {
            workbook.write(os);
            workbook.dispose();
        } catch (Exception e) {
            log.error("write", e);
            throw ExceptionUtils.create(PubError.ADD);
        } finally {
            IOUtils.close(os);
        }
    }

    /**
     * workbook写数据
     *
     * @param file 数据写入的文件
     */
    public String writeExcel(File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
            workbook.dispose();
        } catch (Exception e) {
            log.error("write", e);
            throw ExceptionUtils.create(PubError.ADD);
        } finally {
            IOUtils.close(os);
        }

        return file.getParent();
    }

    /**
     * 获取行的序号
     *
     * @param rowIndex
     * @return
     */
    private int getSerialNumber(int rowIndex) {
        if (rowIndex == startRowIndex) {
            return 1;
        } else {
            Row rowPrev = sheet.getRow(rowIndex - 1);
            Cell firstCell = rowPrev.getCell(0);
            return (int) (firstCell.getNumericCellValue() + 1);
        }
    }

    /**
     * 获取分页大小
     *
     * @return
     */
    protected int pageSize() {
        return 3000;
    }

    private int getNextPageStart(int pageSize, int pageStart) {
        return (pageSize + pageStart);
    }

    // -------------------------------------------------------------------------------
    // h.d

    /**
     * 向Excel文件中分页写入多行数据
     *
     * @param dataList             每页数据
     * @param cellMapperExportList Cell导出映射集合
     */
    public <T> void writeRowsByListTwo(List<T> dataList, List<CellMapperExport> cellMapperExportList, int size) {
        String[] fieldNames = new String[cellMapperExportList.size()];
        boolean[] isLocks = new boolean[cellMapperExportList.size()];
        String[] dataFormat = new String[cellMapperExportList.size()];
        int i = 0;
        boolean hasLock = false;
        for (CellMapperExport cellMapperExport : cellMapperExportList) {
            fieldNames[i] = cellMapperExport.getFieldName();
            isLocks[i] = cellMapperExport.isLock();
            dataFormat[i] = cellMapperExport.getDataFormat();
            if (cellMapperExport.isLock()) {
                hasLock = true;
            }
            i++;
        }
        if (!hasLock) {
            isLocks = null;
        }
        List<Object[]> dataListResult = new ArrayList<Object[]>();
        for (T entity : dataList) {
            dataListResult.add(entityToArray(entity, fieldNames));
        }
        if (dataListResult.size() > 0) {
            writeRowsTwo(dataListResult, isLocks, dataFormat, size);
        }
    }

    public void writeRowsTwo(List<Object[]> rowValuesList, boolean[] isLocks, String[] dataFormat, int size) {
        CellStyle dataStyle = getDataStyle(workbook);
        CellStyle unLockStyle = null;
        if (isLocks != null) {
            unLockStyle = workbook.createCellStyle();
            unLockStyle.cloneStyleFrom(dataStyle);
            unLockStyle.setLocked(false);
        }
        Iterator<Object[]> iterator = rowValuesList.iterator();
        Object[] rowValues = null;
        while (iterator.hasNext()) {
            rowValues = iterator.next();
            writeRowTwo(rowValues, dataStyle, isLocks, unLockStyle, dataFormat, size);
        }
    }

    private void writeRowTwo(Object[] rowValues, CellStyle dataStyle, boolean[] isLocks, CellStyle unLockStyle, String[] dataFormat, int size) {
        Row row = sheet1.createRow(lastRowIndex - size);
        for (int i = 1; i <= rowValues.length; i++) {
            Cell cell = row.createCell(i - 1);
            if (isLocks == null || isLocks[i - 1]) {
                cell.setCellStyle(dataStyle);
            } else {
                unLockStyle = workbook.createCellStyle();
                unLockStyle.cloneStyleFrom(dataStyle);
                unLockStyle.setLocked(false);
                if (dataFormat[i - 1] != null) {
                    XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
                    unLockStyle.setDataFormat(format.getFormat(dataFormat[i - 1]));
                }
                cell.setCellStyle(unLockStyle);
            }
            setCellValue(cell, rowValues[i - 1]);
        }
        lastRowIndex++;
    }
    // -------------------------------------------------------------------------------
}
