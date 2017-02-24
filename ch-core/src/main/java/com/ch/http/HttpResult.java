package com.ch.http;

import com.ch.Constants;

import java.util.Collection;

/**
 * 描述：com.ch.http
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class HttpResult<T> extends BaseResult<T> {

    public String param = "";

    public HttpResult() {
        super(Constants.FAILED);
    }

    public HttpResult(int status) {
        super(status);
    }

    public HttpResult(Collection<T> records) {
        super(records);
    }

    public HttpResult(T record) {
        super(record);
    }
}
