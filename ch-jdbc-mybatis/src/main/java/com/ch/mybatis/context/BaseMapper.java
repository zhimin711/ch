package com.ch.mybatis.context;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 *
 * @author zhimin
 * @since 2017-09-06 21:53
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {


}
