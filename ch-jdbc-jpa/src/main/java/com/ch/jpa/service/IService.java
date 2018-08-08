package com.ch.jpa.service;

import com.ch.result.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：com.zh.jdbc.support
 *
 * @author 80002023
 *         2017/2/14.
 * @version 1.0
 * @since 1.8
 */
public interface IService<ID extends Serializable, T> {

    /**
     * 根据主键ID更新
     *
     * @param record
     * @return
     */
    T saveOrUpdate(T record);

    List<T> saveOrUpdate(List<T> records);

    /**
     * 根据主键ID删除
     *
     * @param id
     * @return
     */
    void delete(ID id);

    void delete(List<T> records);

    /**
     * 根据主键ID查询
     *
     * @param id
     * @return
     */
    T find(ID id);

    /**
     * 根据对象查询，请注意停用状态数据
     *
     * @param record
     * @return list
     */
    List<T> find(T record);

    /**
     * 根据对象分页查询，请注意停用状态数据
     *
     * @param record
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<T> findPage(T record, int pageNum, int pageSize);

    List<T> findAll();
}
