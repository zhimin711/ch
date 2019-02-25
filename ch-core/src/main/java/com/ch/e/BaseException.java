package com.ch.e;

/**
 * 基本异常
 * Created by 01370603 on 2017/11/10.
 */
public class BaseException extends RuntimeException {

    private IError error;

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public BaseException(IError error) {
        super();
        this.error = error;
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param error Error.
     * @param s     the detail message.
     */
    public BaseException(IError error, String s) {
        super(s);
        this.error = error;
    }

    public IError getError() {
        return error;
    }
}
