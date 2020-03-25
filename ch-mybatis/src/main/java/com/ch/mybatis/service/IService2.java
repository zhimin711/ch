package com.ch.mybatis.service;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 描述：com.ch.jdbc.support
 *
 * @author 80002023
 * 2017/2/14.
 * @version 1.0
 * @since 1.8
 */
public interface IService2<T> {

    /**
     * 保存记录
     *
     * @param record 对象记录
     * @return 记录条数
     */
    int save(T record);

    /**
     * 根据对象主键更新记录
     *
     * @param record 对象记录
     * @return 记录条数
     */
    int update(T record);

    /**
     * 根据对象主键更新记录(包含Null)
     *
     * @param record 对象记录
     * @return 更新记录数
     */
    int updateWithNull(T record);

    /**
     * 根据主键删除记录
     *
     * @param id 主键ID
     * @return 记录条数
     */
    int delete(Serializable id);

    int delete(Collection<Serializable> id);

    /**
     * 根据主键查询记录
     *
     * @param id 主键ID
     * @return 对象
     */
    T find(Serializable id);

    /**
     * 根据对象查询
     *
     * @param record 对象
     * @return 记录集合
     */
    List<T> find(T record);

    /**
     * 分页查询记录
     *
     * @param record   对象
     * @param pageNum  页数
     * @param pageSize 每页大小
     * @return 记录集合
     */
    List<T> findPageList(T record, int pageNum, int pageSize);

    /**
     * 分页查询记录
     *
     * @param record   对象
     * @param pageNum  页数
     * @param pageSize 每页大小
     * @return 记录页
     */
    PageInfo<T> findPage(T record, int pageNum, int pageSize);

}
