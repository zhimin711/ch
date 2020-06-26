package com.ch.result;

public interface Callback<T> {

    boolean validate(T info);
}
