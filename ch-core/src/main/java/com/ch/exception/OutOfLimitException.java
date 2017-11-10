package com.ch.exception;

/**
 * 超出限制异常
 * Created by 01370603 on 2017/11/10.
 */
public class OutOfLimitException extends RuntimeException {

    /**
     * Constructs an <code>OutOfLimitException</code> with no
     * detail message.
     */
    public OutOfLimitException() {
        super();
    }

    /**
     * Constructs a new <code>OutOfLimitException</code>
     * class with an argument indicating the illegal index.
     *
     * @param index the illegal index.
     */
    public OutOfLimitException(int index) {
        super("Array index out of range: " + index);
    }

    /**
     * Constructs an <code>OutOfLimitException</code> class
     * with the specified detail message.
     *
     * @param s the detail message.
     */
    public OutOfLimitException(String s) {
        super(s);
    }
}
