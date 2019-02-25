package com.ch.result;

import com.ch.e.IError;
import com.ch.type.Status;

import java.util.Collection;

/**
 * 描述：封装HTTP请求分页结果
 *
 * @author 80002023
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class PageResult<T> extends BaseResult<T> {

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

    public PageResult(IError error) {
        super(error);
    }

    public PageResult(IError error, String msg) {
        super(error, msg);
    }

    /**
     * 构造结果
     *
     * @param pageNum  当前页
     * @param pageSize 每页记录数
     */
    public PageResult(int pageNum, int pageSize) {
        super(Status.SUCCESS);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 构造结果
     *
     * @param records  当前页记录集合
     * @param pageNum  当前页
     * @param pageSize 每页记录数
     * @param total    总页数
     */
    public PageResult(Collection<T> records, int pageNum, int pageSize, long total) {
        super(records);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * 构造结果
     *
     * @param records 记录数
     * @param total   总数
     */
    public PageResult(Collection<T> records, long total) {
        super(Status.SUCCESS);
        this.total = total;
        this.setRows(records);
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
