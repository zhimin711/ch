package com.ch.mybatis.service;

import java.io.Serializable;
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

    int save(T record);

    int update(T record);

    int delete(ID id);

    T find(ID id);

    List<T> find(T record);

    List<T> findPageList(T record, int pageNum, int pageSize);

    List<T> findAll();
}
