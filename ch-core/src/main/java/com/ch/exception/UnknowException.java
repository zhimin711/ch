package com.ch.exception;

/**
 * 未知异常
 * Created by 01370603 on 2017/11/10.
 */
public class UnknowException extends RuntimeException {

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public UnknowException() {
        super();
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     */
    public UnknowException(String s) {
        super(s);
    }
}
