package com.ch.doc.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;

/**
 * ExcelUtils
 * 97
 * 2007
 * Created by 01370603 on 2017/11/29.
 */
public class ExcelUtils {

    private ExcelUtils() {
    }

    public enum ExcelType {
        H, X
    }

    public static void create(ExcelType type) throws Exception {
        Workbook wb;
        if (type == ExcelType.H) {
            wb = new HSSFWorkbook();//97
        } else {
            wb = new SXSSFWorkbook();
        }
        Sheet sheet = wb.createSheet();
        //这个就是合并单元格
        //参数说明：1：开始行 2：结束行  3：开始列 4：结束列
        //比如我要合并 第二行到第四行的    第六列到第八列     sheet.addMergedRegion(new CellRangeAddress(1,3,5,7));
        int index = 6;
        for (int i = 0; i < 10; i++) {
            int start = index;
            int end = index + 3;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, start, end));
            index = end + 1;
        }
        Row row = sheet.createRow(0);
        for (int i = 0; i < index; i++) {
            if (i < 6) {
                row.createCell(i).setCellValue("" + i);
            } else {
                row.createCell(i).setCellValue("" + i);
                i = i + 3;
            }
        }
        index = 1;
        for (int i = 0; i < 10; i++) {
            int rowStart = index;
            int rowEnd = rowStart + 3;
            for (int j = 0; j < 6; j++) {
                sheet.addMergedRegion(new CellRangeAddress(rowStart, rowEnd, j, j));
            }
            index = rowEnd + 1;
        }

        wb.write(new FileOutputStream("D:\\opt\\test.xlsx"));
    }

}
