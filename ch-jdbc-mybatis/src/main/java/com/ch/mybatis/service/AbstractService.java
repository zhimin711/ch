package com.ch.mybatis.service;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.common.Mapper;

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
public abstract class AbstractService<ID extends Serializable, T> implements IService<ID, T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int save(T record) {
        return getMapper().insertSelective(record);
    }

    @Override
    public int update(T record) {
        return getMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int delete(ID id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public T find(ID id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<T> find(T record) {
        return getMapper().select(record);
    }

    @Override
    public List<T> findPageList(T record, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, true, true, false);
        return getMapper().select(record);
    }

    @Override
    public List<T> findAll() {
        return getMapper().selectAll();
    }

    public abstract Mapper<T> getMapper();

    protected boolean valid() {
        if (getMapper() == null) {
            logger.error("Not set base service property mapper!");
            return false;
        }
        return true;
    }

    @Override
    public int batchSave(Collection<T> records) {
        Long c = records.stream().mapToInt(r -> getMapper().insertSelective(r)).count();
        return c.intValue();
    }

    @Override
    public int batchUpdate(Collection<T> records) {
//        records.forEach(r -> getMapper().updateByPrimaryKeySelective(r));
        Long c = records.stream().mapToInt(r -> getMapper().updateByPrimaryKeySelective(r)).count();
        return c.intValue();
    }
}
