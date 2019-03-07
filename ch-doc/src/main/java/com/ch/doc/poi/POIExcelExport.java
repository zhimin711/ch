/*
 * Copyright (c) 2015, S.F. Express Inc. All rights reserved.
 */
package com.ch.doc.poi;

import com.ch.e.CoreError;
import com.ch.utils.ExceptionUtils;
import com.ch.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述：POIExcel导出类（注：可用于多工作表）
 *
 * @author 01370603
 * @since 1.0
 */
public class POIExcelExport {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DateFormat sf = new SimpleDateFormat("yyyy/MM/dd");

    private SXSSFWorkbook workbook = null; // 工作簿
    private Sheet sheet = null; // 工作表，这里指第一个工作表

    private int startRowIndex; // 开始行索引
    private int lastRowIndex; // 最后行索引

    /**
     * 导出Excel
     */
    public POIExcelExport() {
        this(1);
    }

    /**
     * 导出Excel
     *
     * @param startRowIndex 开始行索引，0表示第一行
     */
    public POIExcelExport(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        lastRowIndex = startRowIndex;

        workbook = new SXSSFWorkbook(1000);
        workbook.setCompressTempFiles(true);
        sheet = workbook.createSheet();
    }

    /**
     * 读取Excel模板导出Excel
     *
     * @param tplFile       模板Excel文件
     * @param startRowIndex 开始行索引，0表示第一行
     * @throws FileNotFoundException
     */
    public POIExcelExport(File tplFile, int startRowIndex) throws FileNotFoundException {
        this(new FileInputStream(tplFile), startRowIndex);
    }

    /**
     * 读取Excel模板导出Excel
     *
     * @param is            模板输入流
     * @param startRowIndex 开始行索引，0表示第一行
     */
    public POIExcelExport(InputStream is, int startRowIndex) {
        this.startRowIndex = startRowIndex;
        lastRowIndex = startRowIndex;

        try {
            XSSFWorkbook xs = new XSSFWorkbook(is);
            workbook = new SXSSFWorkbook(xs);
        } catch (Exception e) {
            logger.error("write", e);
            throw ExceptionUtils.create(CoreError.CREATE);
        } finally {
            IOUtils.close(is);
        }

        workbook.setCompressTempFiles(true);
        sheet = workbook.getSheetAt(0);
    }

    /**
     * 添加导入标识(导入模板必调，否则可能导致模板无法导入)
     *
     * @param importId 导入ID（以字母或下划线开头，长度建议在8-30位，不建议输入特殊字符）
     */
    public void addImportId(String importId) {
        if (StringUtils.isEmpty(importId))
            return;
        Name name = workbook.getName(importId);
        if (name != null)
            return;
        name = workbook.createName();
        name.setNameName(importId);
        name.setComment("SF导入模板专用，请勿删除，删除后可能导致无法导入该模板数据");
        name.setRefersToFormula("1");
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
        Cell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            setCellValue(cell, headers[i]);
        }
    }

    /**
     * 获取行的序号
     *
     * @param rowIndex
     * @return
     */
    private int getSerialNumber(int rowIndex) {
        Row rowPrev = sheet.getRow(rowIndex - 1);
        if (rowPrev == null) {
            return 1;
        }
        Cell firstCell = rowPrev.getCell(0);
        try {
            return (int) (firstCell.getNumericCellValue() + 1);
        } catch (Exception e) {
            return 1;
        }
    }

    private void writeRow(Object[] rowValues, CellStyle dataStyle, boolean[] isLocks, CellStyle unLockStyle, boolean squence) {
        Row row = sheet.createRow(lastRowIndex);
        Cell cell;
        int col = 0;
        if (squence) {
            cell = row.createCell(col++);
            cell.setCellStyle(dataStyle);
            cell.setCellValue(getSerialNumber(lastRowIndex));
        }

        for (int i = col; i < rowValues.length + col; i++) {
            cell = row.createCell(i);
            if (isLocks == null || isLocks[i - col]) {
                cell.setCellStyle(dataStyle);
            } else {
                cell.setCellStyle(unLockStyle);
            }
            setCellValue(cell, rowValues[i - col]);
        }
        lastRowIndex++;
    }

    public <T> void writeRecords(List<T> data, List<String> fieldNames, boolean sequence) {
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (T entity : data) {
            dataList.add(entityToArray(entity, fieldNames.toArray(new String[]{})));
        }
        writeRows(dataList, null, sequence);
    }

    /**
     * 写多行数据
     *
     * @param rowValuesList 行值
     * @param sequence      是否生成序号
     */
    public void writeRows(List<Object[]> rowValuesList, boolean sequence) {
        writeRows(rowValuesList, null, sequence);
    }

    /**
     * 写多行数据
     *
     * @param rowValuesList 行值
     * @param isLocks       是否锁定数组
     * @param squence       是否生成序号
     */
    public void writeRows(List<Object[]> rowValuesList, boolean[] isLocks, boolean squence) {
        CellStyle dataStyle = getDataStyle(workbook);
        CellStyle unLockStyle = null;
        if (isLocks != null) {
            unLockStyle = workbook.createCellStyle();
            unLockStyle.cloneStyleFrom(dataStyle);
            unLockStyle.setLocked(false);
        }
        Iterator<Object[]> iterator = rowValuesList.iterator();
        Object[] rowValues;
        while (iterator.hasNext()) {
            rowValues = iterator.next();
            writeRow(rowValues, dataStyle, isLocks, unLockStyle, squence);
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
     * @param pageData             每页数据
     * @param cellMapperExportList Cell导出映射集合
     * @param squence              是否生成序号
     */
    public <T> void writeRowsByPage(PageData<T> pageData, List<CellMapperExport> cellMapperExportList, boolean squence) {
        String[] fieldNames = new String[cellMapperExportList.size()];
        boolean[] isLocks = new boolean[cellMapperExportList.size()];
        int i = 0;
        boolean hasLock = false;
        for (CellMapperExport cellMapperExport : cellMapperExportList) {
            fieldNames[i] = cellMapperExport.getFieldName();
            isLocks[i] = cellMapperExport.isLock();
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
            List<Object[]> dataList = new ArrayList<Object[]>();
            for (T entity : datas) {
                dataList.add(entityToArray(entity, fieldNames));
            }

            writeRows(dataList, isLocks, squence);
            result = datas.size() == pageSize;
        } while (result);
    }

    /**
     * 将实体属性转化为对象数组
     *
     * @param entity     实体
     * @param fieldNames 实体属性映射数组
     * @return
     */
    private <T> Object[] entityToArray(T entity, String[] fieldNames) {
        List<Object> objList = new ArrayList<Object>();
        boolean isMap = entity instanceof Map;
        for (String fieldName : fieldNames) {
            if (isMap) {
                Object o = ((Map<?, ?>) entity).get(fieldName);
                objList.add(o);
            } else {
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = null;
                try {
                    method = entity.getClass().getMethod(methodName);
                } catch (Exception e) {
                    logger.error("entityToArray", e);
                    throw ExceptionUtils.create(CoreError.INVALID);
                }
                Object value = null;
                try {
                    value = method.invoke(entity);
                } catch (Exception e) {
                    logger.error("entityToArray", e);
                    throw ExceptionUtils.create(CoreError.INVALID);
                }
                objList.add(value);
            }
        }
        return objList.toArray();
    }

    private int getNextPageStart(int pageSize, int pageStart) {
        return (pageSize + pageStart);
    }

    /**
     * 获取分页大小
     *
     * @return
     */
    protected int pageSize() {
        return 3000;
    }

    /**
     * workbook写数据
     *
     * @param file 数据写入的文件
     */
    public String write(File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
            workbook.dispose();
        } catch (Exception e) {
            logger.error("write", e);
            throw ExceptionUtils.create(CoreError.NOT_EXISTS);
        } finally {
            IOUtils.close(os);
        }

        return file.getParent();
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
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 9);
        headerStyle.setFont(font);
        return headerStyle;
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
        font2.setFontName("Times New Roman");
        font2.setFontHeightInPoints((short) 9);
        dataStyle.setFont(font2);
        return dataStyle;
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
     * 设置导出Excel的Sheet页
     *
     * @param sheetName Sheet页名称
     */
    public void setSheetAt(String sheetName) {
        setSheetAt(sheetName, startRowIndex);
    }

    /**
     * 设置导出Excel的Sheet页
     *
     * @param sheetName     Sheet页名称
     * @param startRowIndex 开始行索引，0表示第一行<b>(如果该Sheet页已设置序号，则该项设置无效)</b>
     */
    public void setSheetAt(String sheetName, int startRowIndex) {
        sheet = Optional.ofNullable(workbook.getSheet(sheetName)).orElseGet(() -> workbook.createSheet(sheetName));

        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum > this.startRowIndex) {
            lastRowIndex = lastRowNum + 1;
        } else {
            lastRowIndex = startRowIndex;
        }
    }

    /**
     * 设置导出Excel的Sheet页
     *
     * @param sheetIndex Sheet页索引，0表示第一页
     */
    public void setSheetAt(int sheetIndex) {
        setSheetAt(sheetIndex, startRowIndex);
    }

    /**
     * @param start
     * @param width
     */
    public void setColumnWidth(int start, int width) {
        sheet.setColumnWidth(start, width);
    }

    /**
     * @param start
     * @param end
     * @param width
     */
    public void setColumnsWidth(final int start, final int end, int width) {
        int index = start;
        do {
            setColumnWidth(index, width);
            index++;
        } while (index < end);
    }

    /**
     * 设置导出Excel的Sheet页
     *
     * @param sheetIndex    Sheet页索引，0表示第一页
     * @param startRowIndex 开始行索引，0表示第一行<b>(如果该Sheet页已设置序号，则该项设置无效)</b>
     */
    public void setSheetAt(int sheetIndex, int startRowIndex) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = numberOfSheets; i <= sheetIndex; i++) {
            sheet = workbook.createSheet();
        }
        if (sheetIndex < numberOfSheets) {
            sheet = workbook.getSheetAt(sheetIndex);
        }

        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum > this.startRowIndex) {
            lastRowIndex = lastRowNum + 1;
        } else {
            lastRowIndex = startRowIndex;
        }
    }
}
