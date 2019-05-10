package com.ch.e;

/**
 * 公共异常
 * Created by 01370603 on 2017/11/10.
 */
public class PubException extends RuntimeException {

    private IError error;

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public PubException(IError error) {
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
    public PubException(IError error, String s) {
        super(s);
        this.error = error;
    }

    public IError getError() {
        return error;
    }
}
