package com.ch.mybatis.service;

import com.ch.mybatis.context.BaseMapper;
import com.ch.mybatis.exception.MybatisException;
import com.ch.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 描述：com.ch.jdbc.support
 *
 * @author 80002023
 * 2017/2/14.
 * @version 1.0
 * @since 1.8
 */

public abstract class BaseService<ID extends Serializable, T> implements IService<ID, T> {

    protected abstract Mapper<T> getMapper();

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

    @SuppressWarnings("unchecked")
    protected Example getExample() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return new Example(entityClass);
    }

    @Override
    public int save(T record) {
        checkMapper();
        checkParam(record);
        return getMapper().insertSelective(record);
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
//        checkParam(id);
        if (id == null) return 0;
        return getMapper().deleteByPrimaryKey(id);
    }


    @SuppressWarnings("unchecked")
    @Override
    public int delete(Collection<ID> ids) {
        checkMapper();
//        checkParam(ids);
        if (ids == null) return 0;
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Set<EntityColumn> pkSet = EntityHelper.getPKColumns(entityClass);
        if (pkSet == null || pkSet.isEmpty() || pkSet.size() > 1) {
            throw new MybatisException("no or multi pk columns, this method is not support!");
        }
        EntityColumn pkColumn = pkSet.iterator().next();
        Example e = getExample();
        e.createCriteria().andIn(pkColumn.getProperty(), ids);
        return getMapper().deleteByExample(e);
    }


    @Override
    public T find(ID id) {
        checkMapper();
//        checkParam(id);
        if (id == null) return null;
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
