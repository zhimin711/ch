package com.ch.doc.poi;

import com.ch.e.PubError;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
import com.ch.utils.IOUtils;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
 * @author zhimin.ma
 */
public class ExcelImport {

    private Integer maxCount;

    private RecordDefine recordDefine;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private DecimalFormat decimalFormat = new DecimalFormat("0");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ExcelImport(RecordDefine recordDefine, Integer maxCount) {
        this.recordDefine = recordDefine;
        this.maxCount = maxCount;
    }

    public void setDateFormat(String pattern) {
        dateFormat.applyPattern(pattern);
    }

    public void setDecimalFormat(String pattern) {
        decimalFormat.applyPattern(pattern);
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
     * @param excelFile 上传文件 startRowIndex 文件读取开始下标 flag 上传文件的格式(非空为2007格式)
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<?> read(File excelFile, Class<?> clazz, int startRowIndex, int sheetIndex)
            throws Exception {
        FileInputStream fis = null;
        Workbook readWorkBook;
        List<Object> recList = new ArrayList<>();
        try {
            fis = new FileInputStream(excelFile);
            readWorkBook = create(fis);
        } catch (Exception e1) {
            logger.error("read file not found!", e1);
            throw ExceptionUtils.create(PubError.INVALID);
        } finally {
            IOUtils.close(fis);
        }
        Sheet readSheet = readWorkBook.getSheetAt(sheetIndex);
        if (readSheet != null) {
            Row rowFirst = readSheet.getRow(0);
            if (rowFirst != null) {
                if (recordDefine.getColumns() != null
                        && recordDefine.getColumns().size() > rowFirst.getPhysicalNumberOfCells()) {
                    throw ExceptionUtils.create(PubError.INVALID);
                }
            }
            int rowcount = readSheet.getLastRowNum();
            if (maxCount != null && rowcount > maxCount) {
                throw ExceptionUtils.create(PubError.OUT_OF_LIMIT);
            }
            Cell cell;
            for (int rowIndex = startRowIndex; rowIndex <= rowcount; rowIndex++) {
                Row row = readSheet.getRow(rowIndex);
                Object obj = clazz.newInstance();
                int index = 0;
                if (row == null) {
                    continue;
                }
                for (ColumnDefine column : recordDefine.getColumns()) {
                    cell = row.getCell(index);
                    if (cell == null && column.getType().equals("Y")) {
                        throw ExceptionUtils.create(PubError.NON_NULL);
                    }
                    String cellValue = parseCellValue(cell);
                    if (CommonUtils.isEmpty(cellValue) && column.getType().equals("Y")) {
                        throw ExceptionUtils.create(PubError.NON_NULL);
                    }
                    if (obj instanceof List) {
                        ((List) obj).add(cellValue);
                        index++;
                        continue;
                    } else if (obj instanceof Map) {
                        ((Map) obj).put(index, cellValue);
                        index++;
                        continue;
                    }
                    Class<?> propertyType = PropertyUtils.getPropertyType(obj, column.getPropName());
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
                        logger.error("read file new obj error!", e);
                        throw ExceptionUtils.create(PubError.NOT_ALLOWED);
                    }
                    PropertyUtils.setProperty(obj, column.getPropName(), value);
                    index++;
                }
                recList.add(obj);
            }
        }
        return recList;
    }

    private String parseCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
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
                    cellValue = decimalFormat.format(cell.getNumericCellValue());
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
                break;
            default:
        }
        return cellValue;
    }
}
