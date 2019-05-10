package com.ch.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.ch.Status;
import com.ch.e.IError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * 描述：com.ch.http
 *
 * @author zhimin.ma
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 请求状态
     */
    private int status;


    /**
     * 返回数据
     */
    private Collection<T> rows = new ArrayList<>();

    /**
     * 代码
     */
    private String code;
    /**
     * 消息
     */
    private String message;
    /**
     * 附加数据
     */
    private Map<String, Object> extra;

    /**
     * 服务器时间
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {
        setStatus(Status.FAILED);
    }

    /**
     * @param status 请求状态
     */
    public Result(Status status) {
        setStatus(status);
    }

    public static <T> Result<T> success() {
        return new Result<>(Status.SUCCESS);
    }

    /**
     * 根据记录集合创建一个请求结果
     *
     * @param row 记录集合
     */
    public static <T> Result<T> success(T row) {
        Result<T> res = new Result<>(Status.SUCCESS);
        res.put(row);
        return res;
    }

    /**
     * 根据记录集合创建一个请求结果
     *
     * @param rows 记录集合
     */
    public static <T> Result<T> success(Collection<T> rows) {
        Result<T> res = new Result<>(Status.SUCCESS);
        res.setRows(rows);
        return res;
    }

    public static <T> Result<T> failed() {
        return new Result<>();
    }

    public static <T> Result<T> error(IError error) {
        Result<T> res = new Result<>(Status.ERROR);
        res.setCode(error.getCode());
        res.setMessage(error.getName());
        return res;
    }

    public static <T> Result<T> error(IError error, String message) {
        Result<T> res = new Result<>(Status.ERROR);
        res.setCode(error.getCode());
        res.setMessage(message);
        return res;
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

    public boolean isSuccess() {
        return Status.isSuccess(status);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }

    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    public T get() {
        if (isEmpty()) {
            return null;
        }
        return rows.iterator().next();
    }
}
