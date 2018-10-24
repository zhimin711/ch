package com.ch.result;

import com.ch.err.Error;
import com.ch.err.ErrorCode;
import com.ch.type.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 描述：com.zh.http
 *
 * @author 80002023
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class BaseResult<T> implements Serializable {

    /**
     * 请求状态
     */
    private int status;


    /**
     * 记录集合(包含错误)
     */
    private Collection<T> rows = new ArrayList<>();

    /**
     * 请求错误信息
     */
    private Error error;

    /**
     * @param status 请求状态
     */
    public BaseResult(Status status) {
        setStatus(status);
    }


    public BaseResult(ErrorCode error) {
        setStatus(Status.ERROR);
        this.error = new Error(error);
    }

    public BaseResult(ErrorCode error, String msg) {
        setStatus(Status.ERROR);
        this.error = new Error(error.getCode(), msg);
    }

    /**
     * 根据记录集合创建一个请求结果
     *
     * @param rows 记录集合
     */
    public BaseResult(Collection<T> rows) {
        setStatus(Status.SUCCESS);
        setRows(rows);
    }

    /**
     * 根据一条记录创建一个请求结果
     *
     * @param record 记录
     */
    public BaseResult(T record) {
        setStatus(Status.SUCCESS);
        this.put(record);
    }

    /**
     * 放一条记录到请求结果
     *
     * @param record 记录
     */
    public void put(T record) {
        if (rows == null) {
            rows = new HashSet<T>();
        }
        rows.add(record);
    }

    public Collection<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = rows;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.getNum();
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public void setError(ErrorCode error, String msg) {
        this.error = new Error(error.getCode(), msg);
    }

    public boolean isSuccess() {
        return Status.isSuccess(status);
    }

    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }

    public T get() {
        if (isEmpty()) {
            return null;
        }
        return rows.iterator().next();
    }
}
