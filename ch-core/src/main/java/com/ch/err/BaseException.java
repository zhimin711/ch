package com.ch.err;

/**
 * 未知异常
 * Created by 01370603 on 2017/11/10.
 */
public abstract class BaseException extends RuntimeException {

    private ErrorCode error;

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public BaseException(ErrorCode error) {
        super();
        this.error = error;
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param error ErrorCode.
     * @param s     the detail message.
     */
    public BaseException(ErrorCode error, String s) {
        super(s);
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }
}
