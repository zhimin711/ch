package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author 01370603
 * @date 2018/8/14 11:56
 */
public class ExceptionUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

    /**
     * 抓取异常信息
     *
     * @param throwable--异常对象
     * @return
     */
    public static String getDetailMessage(Throwable throwable) {
        String result = "";
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            result = sw.toString();
        } catch (Exception e) {
            logger.error("getDetailMessage", e);
        } finally {
            IOUtils.close(sw, pw);
        }
        return result;
    }
}
