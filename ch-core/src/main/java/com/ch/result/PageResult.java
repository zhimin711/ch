package com.ch.result;

import com.ch.Status;
import com.ch.e.IError;

import java.util.Collection;

/**
 * 描述：封装HTTP请求分页结果
 *
 * @author zhimin.ma
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class PageResult<T> extends Result<T> {

    /**
     * 当前页
     */
    private int pageNum = 1;
    /**
     * 每页记录数
     */
    private int pageSize = 10;
    /**
     * 总页数
     */
    private long total = 0;

    public PageResult() {
        super(Status.FAILED);
    }

    public PageResult(Status status) {
        super(status);
    }

    /**
     * 构造结果
     *
     * @param total 总数
     * @param rows  记录数
     */
    public static <T> PageResult<T> success(long total, Collection<T> rows) {
        PageResult<T> res = new PageResult<>(Status.SUCCESS);
        res.setTotal(total);
        res.setRows(rows);
        return res;
    }

    public static <T> PageResult<T> success() {
        return new PageResult<>(Status.SUCCESS);
    }

    /**
     * 根据记录集合创建一个请求结果
     *
     * @param row 记录集合
     */
    public static <T> PageResult<T> success(T row) {
        PageResult<T> res = new PageResult<>(Status.SUCCESS);
        res.put(row);
        return res;
    }

    /**
     * 根据记录集合创建一个请求结果
     *
     * @param rows 记录集合
     */
    public static <T> PageResult<T> success(Collection<T> rows) {
        PageResult<T> res = new PageResult<>(Status.SUCCESS);
        res.setRows(rows);
        return res;
    }

    public static <T> PageResult<T> failed() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> error(IError error) {
        PageResult<T> res = new PageResult<>(Status.ERROR);
        res.setCode(error.getCode());
        res.setMessage(error.getName());
        return res;
    }

    public static <T> PageResult<T> error(IError error, String message) {
        PageResult<T> res = new PageResult<>(Status.ERROR);
        res.setCode(error.getCode());
        res.setMessage(message);
        return res;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
