package com.ch.err;

/**
 * 服务运行时异常
 *
 * @author zhimin
 * @date 2018/8/13 上午8:48
 */
public class ServiceException extends BaseException {

    /**
     * Constructs an <code>BaseException</code> with no
     * detail message.
     */
    public ServiceException() {
        super(ErrorCode.DEFAULT);
    }

    /**
     * Constructs an <code>BaseException</code> class
     * with the specified detail message.
     *
     * @param error ErrorCode message.
     */
    public ServiceException(ErrorCode error) {
        super(error, error.getName());
    }

    /**
     * Constructs an <code>BaseException</code> class
     * with the specified detail message.
     *
     * @param error ErrorCode message.
     * @param s     the detail message.
     */
    public ServiceException(ErrorCode error, String s) {
        super(error, s);
    }

    /**
     * Constructs an <code>BaseException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     */
    public ServiceException(String s) {
        super(ErrorCode.DEFAULT, s);
    }

}
