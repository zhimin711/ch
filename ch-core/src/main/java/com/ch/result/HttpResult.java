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

    private String param = "";

    public HttpResult() {
        super(Status.FAILED);
    }

    public HttpResult(Status status) {
        super(status);
    }

    public HttpResult(Status status, String param) {
        super(status);
        this.param = param;
    }

    public HttpResult(Collection<T> records) {
        super(records);
    }

    public HttpResult(Collection<T> records, String param) {
        super(records);
        this.param = param;
    }

    public HttpResult(T record) {
        super(record);
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
