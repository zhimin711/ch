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
public class PageResult<T> extends BaseResult<T> {

    public int pageNum = 1;
    public int pageSize = 10;
    public long total;

    public PageResult() {
        super(Constants.FAILED);
    }

    public PageResult(Collection<T> records, int pageNum, int pageSize, long total) {
        super(records);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }
}
