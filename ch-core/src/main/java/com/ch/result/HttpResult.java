package com.ch.result;

import com.ch.type.Status;

import java.util.Collection;

/**
 * 描述：封装HTTP请求结果
 *
 * @author zhimin
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class HttpResult<T> extends BaseResult<T> {

    public String param = "";

    public HttpResult() {
        super(Status.FAILED);
    }

    public HttpResult(Status status) {
        super(status);
    }

    public HttpResult(Collection<T> records) {
        super(records);
    }

    public HttpResult(T record) {
        super(record);
    }
}
