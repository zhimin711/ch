package com.ch.result;

import java.util.List;

/**
 * @author 01370603
 */
@FunctionalInterface
public interface InvokerList<T> {

    List<T> invoke() throws Exception;

}

