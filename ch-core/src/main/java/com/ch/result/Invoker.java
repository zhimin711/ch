package com.ch.result;

/**
 * @author 01370603
 */
@FunctionalInterface
public interface Invoker<T> {

    T invoke() throws Exception;

}

