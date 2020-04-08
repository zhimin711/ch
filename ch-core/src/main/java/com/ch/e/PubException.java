package com.ch.e;

import com.ch.result.Error;

/**
 * 公共异常
 * Created by 01370603 on 2017/11/10.
 */
public class PubException extends RuntimeException {

    private IError error;

    /**
     * Constructs an <code>RuntimeException</code> with no
     * detail message.
     */
    public PubException(IError error) {
        super(error.getName());
        this.error = error;
    }

    /**
     * Constructs an <code>RuntimeException</code> class
     * with the specified detail message.
     *
     * @param error Error.
     * @param s     the detail message.
     */
    public PubException(IError error, String s) {
        super(s);
        this.error = new Error(error.getCode(), s);
    }

    /**
     * Constructs an <code>RuntimeException</code> with no
     * detail message.
     *
     * @param e the Throwable.
     */
    public PubException(IError error, Throwable e) {
        super(e);
        this.error = error;
    }

    /**
     * Constructs an <code>RuntimeException</code> class
     * with the specified detail message.
     *
     * @param error Error.
     * @param s     the detail message.
     * @param e     the Throwable.
     */
    public PubException(IError error, String s, Throwable e) {
        super(s, e);
        this.error = new Error(error.getCode(), s);
    }

    /**
     * Constructs an <code>RuntimeException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     * @param e the Throwable.
     */
    public PubException(String s, Throwable e) {
        super(s, e);
        this.error = PubError.UNKNOWN;
    }

    public IError getError() {
        return error;
    }
}
