package com.ch.result;

import com.ch.type.Status;

import java.util.Collection;
import java.util.Collections;

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

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }
}
