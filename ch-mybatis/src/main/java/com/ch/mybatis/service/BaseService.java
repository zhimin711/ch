package com.ch.mybatis.service;

import com.ch.mybatis.context.BaseMapper;
import com.ch.mybatis.exception.MybatisException;
import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @param <PK> 主键
 * @param <T>  对象
 * @author zhimin.ma
 * 2017/2/14.
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseService<PK extends Serializable, T> implements IService<PK, T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract Mapper<T> getMapper();

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    private Set<EntityColumn> getPK() {
        Set<EntityColumn> pkSet = EntityHelper.getPKColumns(getEntityClass());
        if (pkSet == null || pkSet.isEmpty()) {
            throw new MybatisException("no pk columns, this method is not support!");
        }
        return pkSet;
    }


    private T newInstance() {
        try {
            return getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Mybatis Service New Instance Error", e);
            throw new MybatisException("New Instance Error!");
        }
    }


    private void check() {
        if (getMapper() == null) {
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
        Set<EntityColumn> pkSet = getPK();
        for (EntityColumn pk : pkSet) {
            Object value = BeanExtUtils.getValueByProperty(record, pk.getProperty());
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
        return getMapper().insertSelective(record);
    }


    @Override
    public int update(T record) {
        check();
        checkPK(record);
        return getMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateWithNull(T record) {
        check();
        checkPK(record);
        return getMapper().updateByPrimaryKey(record);
    }

    @Override
    public int delete(PK id) {
        check();
//        checkParam(id);
        if (id == null) return 0;
        return getMapper().deleteByPrimaryKey(id);
    }


    @SuppressWarnings("unchecked")
    @Override
    public int delete(Collection<PK> ids) {
        check();
//        checkParam(ids);
        if (ids == null || ids.isEmpty()) return 0;
        Set<EntityColumn> pkSet = EntityHelper.getPKColumns(getEntityClass());
        if (pkSet == null || pkSet.isEmpty() || pkSet.size() > 1) {
            throw new MybatisException("no or multi pk columns, this method is not support!");
        }
        EntityColumn pkColumn = pkSet.iterator().next();
        Example e = getExample();
        e.createCriteria().andIn(pkColumn.getProperty(), ids);
        return getMapper().deleteByExample(e);
    }


    @Override
    public T find(PK id) {
        check();
        if (id == null) return null;
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<T> find(T record) {
        check();
        check(record);
        return getMapper().select(record);
    }

    @Override
    public List<T> findPageList(T record, int pageNum, int pageSize) {
        check();
        if (record == null) {
            record = newInstance();
        }
        PageHelper.startPage(pageNum, pageSize, true, true, false);
        return getMapper().select(record);
    }

    @Override
    public List<T> findPageList(int pageNum, int pageSize, T record) {
        return this.findPageList(record, pageNum, pageSize);
    }

    @Override
    public List<T> findAll() {
        check();
        return getMapper().selectAll();
    }

    @Override
    public int batchSave(List<T> records) {
        check();
        if (records == null) return 0;
        if (getMapper() instanceof BaseMapper) {
            return ((BaseMapper<T>) getMapper()).insertList(records);
        }
        Long c = records.stream().mapToInt(r -> getMapper().insertSelective(r)).count();
        return c.intValue();
    }

    @Override
    public int batchUpdate(List<T> records) {
        check();
        if (records == null) return 0;
//        records.forEach(r -> getMapper().updateByPrimaryKeySelective(r));
        Long c = records.stream().mapToInt(r -> getMapper().updateByPrimaryKeySelective(r)).count();
        return c.intValue();
    }

    @Override
    public PageInfo<T> findPage(T record, int pageNum, int pageSize) {
        check();
        check(record);
        PageHelper.startPage(pageNum, pageSize);
        List<T> records = getMapper().select(record);
        return new PageInfo<>(records);
    }

    @Override
    public PageInfo<T> findPage(int pageNum, int pageSize, T record) {
        return this.findPage(record, pageNum, pageSize);
    }
}
