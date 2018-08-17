package com.ch.mybatis.service;

import com.ch.mybatis.context.BaseMapper;
import com.ch.mybatis.exception.MybatisException;
import com.ch.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
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

public abstract class BaseService<ID extends Serializable, T> implements IService<ID, T> {

    public abstract Mapper<T> getMapper();

    @Override
    public int save(T record) {
        checkMapper();
        checkParam(record);
        return getMapper().insertSelective(record);
    }

    protected Example getExample() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        EntityHelper.getPKColumns(entityClass);
        return new Example(entityClass);
    }

    @Override
    public int update(T record) {
        checkMapper();
        checkParam(record);
        return getMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateWithNull(T record) {
        checkMapper();
        checkParam(record);
        return getMapper().updateByPrimaryKey(record);
    }

    @Override
    public int delete(ID id) {
        checkMapper();
        checkParam(id);
        return getMapper().deleteByPrimaryKey(id);
    }


    @Override
    public int delete(Collection<ID> ids) {
        checkMapper();
        checkParam(ids);
        Example e = getExample();
        e.createCriteria().andIn("", ids);
        return getMapper().deleteByExample(e);
    }


    @Override
    public T find(ID id) {
        checkMapper();
        checkParam(id);
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<T> find(T record) {
        checkMapper();
        checkParam(record);
        return getMapper().select(record);
    }

    @Override
    public List<T> findPageList(T record, int pageNum, int pageSize) {
        checkMapper();
        checkParam(record);
        PageHelper.startPage(pageNum, pageSize, true, true, false);
        return getMapper().select(record);
    }

    @Override
    public List<T> findAll() {
        checkMapper();
        return getMapper().selectAll();
    }

    private void checkMapper() {
        if (CommonUtils.isEmpty(getMapper())) {
            throw new MybatisException("Mapper接口为空!");
        }
    }

    protected void checkParam(Object param) {
        if (CommonUtils.isEmpty(param)) {
            throw new MybatisException("参数为空!");
        }
    }

    @Override
    public int batchSave(List<T> records) {
        checkMapper();
        checkParam(records);
        if (getMapper() instanceof BaseMapper) {
            return ((BaseMapper<T>) getMapper()).insertList(records);
        }
        Long c = records.stream().mapToInt(r -> getMapper().insertSelective(r)).count();
        return c.intValue();
    }

    @Override
    public int batchUpdate(List<T> records) {
        checkMapper();
        checkParam(records);
//        records.forEach(r -> getMapper().updateByPrimaryKeySelective(r));
        Long c = records.stream().mapToInt(r -> getMapper().updateByPrimaryKeySelective(r)).count();
        return c.intValue();
    }

    @Override
    public PageInfo<T> findPage(T record, int pageNum, int pageSize) {
        checkMapper();
        checkParam(record);
        PageHelper.startPage(pageNum, pageSize, true, true, false);
        List<T> records = getMapper().select(record);
        return new PageInfo<>(records);
    }
}
