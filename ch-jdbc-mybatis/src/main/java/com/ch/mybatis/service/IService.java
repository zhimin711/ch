package com.ch.mybatis.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 描述：com.ch.jdbc.support
 *
 * @author 80002023
 *         2017/2/14.
 * @version 1.0
 * @since 1.8
 */
public interface IService<ID extends Serializable, T> {

    /**
     * @param record
     * @return
     */
    int save(T record);

    /**
     * @param record
     * @return
     */
    int update(T record);

    /**
     * @param id
     * @return
     */
    int delete(ID id);

    /**
     * @param id
     * @return
     */
    T find(ID id);

    /**
     * @param record
     * @return
     */
    List<T> find(T record);

    /**
     * @param record
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> findPageList(T record, int pageNum, int pageSize);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param records
     * @return
     */
    int batchSave(Collection<T> records);

    /**
     * @param records
     * @return
     */
    int batchUpdate(Collection<T> records);
}
