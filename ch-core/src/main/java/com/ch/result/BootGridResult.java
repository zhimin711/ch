package com.ch.result;

import com.ch.Constants;

import java.util.Collection;

/**
 * 描述：封装BootGrid插件HTTP请求分页结果
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class BootGridResult<T> extends BaseResult<T> {

    /**
     * 当前页
     */
    public int current = 1;
    /**
     * 每页记录数
     */
    public int rowCount = 10;
    /**
     * 总页数
     */
    public long total;
    /**
     * 当前页记录集合
     */
    public Collection<T> rows;

    public BootGridResult(int status) {
        super(status);
    }

    /**
     * 构造结果
     *
     * @param records  当前页记录集合
     * @param pageNum  当前页
     * @param pageSize 每页记录数
     * @param total    总页数
     */
    public BootGridResult(Collection<T> records, int pageNum, int pageSize, long total) {
        super(Constants.SUCCESS);
        this.current = pageNum;
        this.rowCount = pageSize;
        this.total = total;
        this.rows = records;
    }
}
