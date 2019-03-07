package com.ch.doc.poi;

import com.ch.e.CoreError;
import com.ch.utils.ExceptionUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ExcelImportTs {
    private Integer maxCount;

    private RecordDefineTs recordDefine;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ExcelImportTs(RecordDefineTs recordDefine, Integer maxCount) {
        this.recordDefine = recordDefine;
        this.maxCount = maxCount;
    }

    public static Workbook create(InputStream inp) throws IOException, InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        return new HSSFWorkbook(inp);
        // throw new IllegalArgumentException("excel can not be resolve");
    }

    /**
     * 读取模板，并为指定单元格设置值，获取指定单元格的值
     *
     * @param uploadFile 上传文件 startRowIndex 文件读取开始下标 flag 上传文件的格式(非空为2007格式)
     * @throws Throwable
     * @throws InvocationTargetException
     */
    public List<?> readExcel(File uploadFile, Class<?> clazz, int startRowIndex, int sheetIndex)
            throws Throwable {
        FileInputStream fIn = null;
        Workbook readWorkBook = null;
        List<Object> recList = new ArrayList<Object>();
        try {
            fIn = new FileInputStream(uploadFile);
            readWorkBook = create(fIn);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("Read Excel Exception: ", e1);
            throw ExceptionUtils.create(CoreError.INVALID);
        } finally {
            if (fIn != null) {
                fIn.close();
            }
        }
        if (readWorkBook != null) {
            Sheet readSheet = readWorkBook.getSheetAt(sheetIndex);
            if (readSheet != null) {
                Row rowFirst = readSheet.getRow(0);
                if (rowFirst != null) {
                    if (recordDefine.getColumns() != null
                            && recordDefine.getColumns().size() != rowFirst
                            .getPhysicalNumberOfCells()) {
                        throw ExceptionUtils.create(CoreError.INVALID);
                    }
                }
                int rowcount = readSheet.getLastRowNum();
                if (maxCount != null) {
                    if (rowcount > maxCount) {
                        throw ExceptionUtils.create(CoreError.OUT_OF_LIMIT);
                    }
                }

                Cell cell = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                for (int rowIndex = startRowIndex; rowIndex <= rowcount; rowIndex++) {
                    Row row = readSheet.getRow(rowIndex);
                    Object obj = clazz.newInstance();
                    int index = 0;
                    if (row != null) {
                        for (ColumnDefine column : recordDefine.getColumns()) {
                            cell = row.getCell(index);
                            String cellValue = null;
                            if (cell != null) {
                                // 判断当前Cell的Type
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_FORMULA:
                                        try {
                                            FormulaEvaluator e = new XSSFFormulaEvaluator(
                                                    (XSSFWorkbook) cell.getSheet().getWorkbook());
                                            e.evaluateFormulaCell(cell);
                                            cellValue = String.valueOf(cell.getNumericCellValue());
                                        } catch (IllegalStateException e) {
                                            cellValue = cell.getRichStringCellValue().toString().trim();
                                        }
                                        break;
                                    // 如果当前Cell的Type为NUMERIC
                                    case Cell.CELL_TYPE_NUMERIC: {
                                        // 判断当前的cell是否为Date
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            // 如果是Date类型则，取得该Cell的Date值,把Date转换成YYYY-MM-DD格式的字符串
                                            cellValue = dateFormat.format(cell.getDateCellValue());
                                        } else {// 如果是纯数字
                                            // 取得当前Cell的数值
                                            if ("float".equals(column.getCellType())) {
                                                cellValue = Float.valueOf(cell.toString()).toString();
                                            } else {
                                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                                cellValue = cell.toString();
                                            }
                                        }
                                        break;
                                    }
                                    // 如果当前Cell的Type为STRIN
                                    case Cell.CELL_TYPE_STRING:
                                        // 取得当前的Cell字符串
                                        cellValue = cell.getStringCellValue().trim();
                                        break;
                                    // 默认的Cell值
                                    case Cell.CELL_TYPE_BLANK:
                                        if (!column.getType().equals("Y")
                                                && !column.getType().equals("N")) {
                                            throw ExceptionUtils.create(CoreError.NON_NULL);
                                        }
                                        break;
                                    default:
                                        cellValue = "";
                                }
                            } else {
                                if (!column.getType().equals("Y") && !column.getType().equals("N")) {

                                    throw ExceptionUtils.create(CoreError.NON_NULL);
                                }
                            }
                            Class<?> propertyType = PropertyUtils.getPropertyType(obj,
                                    column.getPropName());
                            Object value = null;
                            try {
                                if (propertyType == Long.class || long.class == propertyType) {
                                    if (StringUtils.isNotEmpty(cellValue)) {

                                        value = NumberUtils.createBigDecimal(cellValue).longValue();
                                        // Long.valueOf(cellValue);
                                    }
                                } else if (propertyType == Double.class
                                        || propertyType == double.class) {
                                    if (StringUtils.isNotEmpty(cellValue)) {
                                        value = Double.valueOf(cellValue);
                                    }
                                } else if (propertyType == Date.class) {
                                    if (cellValue != null && !cellValue.equals("")) {
                                        // if (cellValue.length() == 7) {
                                        // value = YMFormat.parse(cellValue);
                                        // } else {
                                        value = dateFormat.parse(cellValue);
                                        // }
                                    }
                                } else if (propertyType == String.class) {
                                    value = cellValue;
                                } else if (propertyType == Integer.class
                                        || propertyType == int.class) {
                                    if (StringUtils.isNotEmpty(cellValue)) {
                                        value = NumberUtils.createBigDecimal(cellValue).intValue();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw ExceptionUtils.create(CoreError.NOT_ALLOWED);
                            }
                            PropertyUtils.setProperty(obj, column.getPropName(), value);
                            index++;
                        }
                        recList.add(obj);
                    }
                }
            }
        }
        return recList;
    }

}
