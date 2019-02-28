package com.ch.mybatis.service;

import com.ch.mybatis.context.BaseMapper;
import com.ch.mybatis.exception.MybatisException;
import com.ch.utils.BeanUtils;
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
 * 描述：com.ch.mybatis.support
 *
 * @author zhimin.ma
 * 2017/2/14.
 * @version 1.0
 * @since 1.8
 * @param <PK> 主键
 * @param <T> 对象
 */
public abstract class MyService<PK extends Serializable, T> implements IService<PK, T> {

    protected MyService(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    private Mapper<T> mapper;

    public Mapper<T> getMapper() {
        return mapper;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    private void check() {
        if (mapper == null) {
            throw new MybatisException("Mapper接口为空!");
        }
    }

    protected void check(T record) {
        if (record == null) {
            throw new MybatisException("参数不能为空!");
        }
    }

    private void checkPK(T record) {
        check(record);
        Set<EntityColumn> pkSet = EntityHelper.getPKColumns(getEntityClass());
        if (pkSet == null || pkSet.isEmpty()) {
            throw new MybatisException("no pk columns, this method is not support!");
        }
        for (EntityColumn pk : pkSet) {
            Object value = BeanUtils.getValueByProperty(record, pk.getProperty());
            if (CommonUtils.isEmpty(value)) {
                throw new MybatisException(pk.getProperty() + " pk column is null!");
            }
        }
    }

    protected Example getExample() {
        return new Example(getEntityClass());
    }

    @Override
    public int save(T record) {
        check();
        check(record);
        return mapper.insertSelective(record);
    }


    @Override
    public int update(T record) {
        check();
        checkPK(record);
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateWithNull(T record) {
        check();
        checkPK(record);
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int delete(PK id) {
        check();
//        checkParam(id);
        if (id == null) return 0;
        return mapper.deleteByPrimaryKey(id);
    }


    @SuppressWarnings("unchecked")
    @Override
    public int delete(Collection<PK> ids) {
        check();
//        checkParam(ids);
        if (ids == null || ids.isEmpty()) return 0;
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Set<EntityColumn> pkSet = EntityHelper.getPKColumns(entityClass);
        if (pkSet == null || pkSet.isEmpty() || pkSet.size() > 1) {
            throw new MybatisException("no or multi pk columns, this method is not support!");
        }
        EntityColumn pkColumn = pkSet.iterator().next();
        Example e = getExample();
        e.createCriteria().andIn(pkColumn.getProperty(), ids);
        return mapper.deleteByExample(e);
    }


    @Override
    public T find(PK id) {
        check();
        if (id == null) return null;
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> find(T record) {
        check();
        check(record);
        return mapper.select(record);
    }

    @Override
    public List<T> findPageList(T record, int pageNum, int pageSize) {
        check();
        check(record);
        PageHelper.startPage(pageNum, pageSize, true, true, false);
        return mapper.select(record);
    }

    @Override
    public List<T> findAll() {
        check();
        return mapper.selectAll();
    }

    @Override
    public int batchSave(List<T> records) {
        check();
        if(records == null) return 0;
        if (mapper instanceof BaseMapper) {
            return ((BaseMapper<T>) mapper).insertList(records);
        }
        Long c = records.stream().mapToInt(r -> mapper.insertSelective(r)).count();
        return c.intValue();
    }

    @Override
    public int batchUpdate(List<T> records) {
        check();
        if(records == null) return 0;
//        records.forEach(r -> getMapper().updateByPrimaryKeySelective(r));
        Long c = records.stream().mapToInt(r -> mapper.updateByPrimaryKeySelective(r)).count();
        return c.intValue();
    }

    @Override
    public PageInfo<T> findPage(T record, int pageNum, int pageSize) {
        check();
        check(record);
        PageHelper.startPage(pageNum, pageSize);
        List<T> records = mapper.select(record);
        return new PageInfo<>(records);
    }
}
