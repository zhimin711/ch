package com.ch.mybatis.service;

import com.ch.mybatis.context.BaseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class ServiceImpl<M extends BaseMapper<T>, T> implements IService2<T> {

    @Resource
    private M baseMapper;

    @Override
    public int save(T record) {
        return baseMapper.insertSelective(record);
    }

    @Override
    public int update(T record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateWithNull(T record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    @Override
    public int delete(Serializable id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(Collection<Serializable> id) {
        return 0;
    }

    @Override
    public T find(Serializable id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> find(T record) {
        return baseMapper.select(record);
    }

    @Override
    public List<T> findPageList(T record, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return baseMapper.select(record);
    }

    @Override
    public PageInfo<T> findPage(T record, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(baseMapper.select(record));
    }

}
