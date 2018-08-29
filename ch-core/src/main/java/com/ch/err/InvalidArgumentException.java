package com.ch.err;

/**
 * 无效参数运行时异常
 *
 * @author zhimin
 * @date 2018/8/13 上午8:48
 */
public class InvalidArgumentException extends BaseException {

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public InvalidArgumentException() {
        super(ErrorCode.ARGS);
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param error ErrorCode message.
     */
    public InvalidArgumentException(ErrorCode error) {
        super(error, error.getName());
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param error ErrorCode message.
     * @param s     the detail message.
     */
    public InvalidArgumentException(ErrorCode error, String s) {
        super(error, s);
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     */
    public InvalidArgumentException(String s) {
        super(ErrorCode.ARGS, s);
    }

}
