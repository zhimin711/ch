package com.ch.result;

import com.ch.Constants;

import java.util.Collection;

/**
 * 描述：封装HTTP请求分页结果
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class PageResult<T> extends BaseResult<T> {

    /**
     * 当前页
     */
    public int pageNum = 1;
    /**
     * 每页记录数
     */
    public int pageSize = 10;
    /**
     * 总页数
     */
    public long total;

    public PageResult() {
        super(Constants.FAILED);
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
}
