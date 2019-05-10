package com.ch.jpa.service;

import com.ch.result.PageResult;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：com.ch.jdbc.support
 *
 * @author 80002023
 * 2017/2/24.
 * @version 1.0
 * @since 1.8
 */
public abstract class AbstractService<ID extends Serializable, T> implements IService<ID, T> {

    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public T saveOrUpdate(T record) {
        return getRepository().save(record);
    }

    @Override
    public List<T> saveOrUpdate(List<T> records) {
        return getRepository().save(records);
    }

    @Override
    public void delete(ID id) {
        getRepository().delete(id);
    }

    @Override
    public void delete(List<T> records) {
        getRepository().delete(records);
    }

    @Override
    public T find(ID id) {
        return getRepository().findOne(id);
    }

    @Override
    public List<T> find(T record) {
        if (record == null) return null;
        Example<T> example = Example.of(record);
        return getRepository().findAll(example);
    }

    @Override
    public PageResult<T> findPage(T record, int pageNum, int pageSize) {
        if (record == null) return new PageResult<T>();
        Example<T> example = Example.of(record);
        Page<T> page = getRepository().findAll(example, new PageRequest(pageNum, pageSize));

        return PageResult.success(page.getSize(), page.getContent());
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }
}
