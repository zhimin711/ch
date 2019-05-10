package com.ch.utils;

import com.ch.e.PubException;
import com.ch.e.IError;
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
     * 创建异常
     *
     * @param error 错误
     * @return
     */
    public static PubException create(IError error) {
        return new PubException(error);
    }

    /**
     * 创建异常
     *
     * @param error 错误
     * @param msg   自定义错误信息
     * @return
     */
    public static PubException create(IError error, String msg) {
        return new PubException(error, msg);
    }


    /**
     * 抓取异常信息
     *
     * @param throwable--异常对象
     * @return
     */
    public static String fullString(Throwable throwable) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            logger.error("to String error!", e);
        } finally {
            IOUtils.close(sw, pw);
        }
        return "";
    }

}
