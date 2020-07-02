package com.ch.utils;

import com.ch.e.PubException;
import com.ch.e.IError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author 01370603
 * @date 2018/8/14 11:56
 */
@Slf4j
public class ExceptionUtils {

    /**
     * 创建异常1
     *
     * @param error 错误
     * @return
     */
    public static PubException create(IError error) {
        return new PubException(error);
    }

    /**
     * 创建异常2
     *
     * @param error     错误
     * @param throwable 异常
     * @return
     */
    public static PubException create(IError error, Throwable throwable) {
        return new PubException(error, throwable);
    }

    /**
     * 创建异常3
     *
     * @param error 错误
     * @param msg   自定义错误信息
     * @return
     */
    public static PubException create(IError error, String msg) {
        return new PubException(error, msg);
    }

    /**
     * 创建异常4
     *
     * @param error     错误
     * @param msg       自定义错误信息
     * @param throwable 异常
     * @return
     */
    public static PubException create(IError error, String msg, Throwable throwable) {
        return new PubException(error, msg, throwable);
    }

    /**
     * 创建异常5
     *
     * @param msg       自定义错误信息
     * @param throwable 异常
     * @return
     */
    public static PubException create(String msg, Throwable throwable) {
        return new PubException(msg, throwable);
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
            log.error("to String error!", e);
        } finally {
            IOUtils.close(sw, pw);
        }
        return "";
    }

    /**
     * 抛异常1
     *
     * @param error 错误
     * @return
     */
    public static void _throw(IError error) {
        throw new PubException(error);
    }

    /**
     * 抛异常2
     *
     * @param error     错误
     * @param throwable 异常
     * @return
     */
    public static void _throw(IError error, Throwable throwable) {
        throw new PubException(error, throwable);
    }

    /**
     * 抛异常3
     *
     * @param error 错误
     * @param msg   自定义错误信息
     * @return
     */
    public static void _throw(IError error, String msg) {
        throw new PubException(error, msg);
    }

    /**
     * 抛异常4
     *
     * @param error     错误
     * @param msg       自定义错误信息
     * @param throwable 异常
     * @return
     */
    public static void _throw(IError error, String msg, Throwable throwable) {
        throw new PubException(error, msg, throwable);
    }

    /**
     * 抛异常5
     *
     * @param msg       自定义错误信息
     * @param throwable 异常
     * @return
     */
    public static void _throw(String msg, Throwable throwable) {
        throw new PubException(msg, throwable);
    }

    /**
     * 抛异常6
     *
     * @param error
     * @param args
     */
    public static void _throw(IError error, Object... args) {
        throw new PubException(error, error.formatMsg(args));
    }

}
