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
public class BootGridResult<T> extends BaseResult<T> {

    public int current = 1;
    public int rowCount = 10;
    public long total;
    public Collection<T> rows;

    public BootGridResult(int status) {
        super(status);
    }

    public BootGridResult(int current, int rowCount, long total, Collection<T> rows) {
        super(Constants.SUCCESS);
        this.current = current;
        this.rowCount = rowCount;
        this.total = total;
        this.rows = rows;
    }
}
