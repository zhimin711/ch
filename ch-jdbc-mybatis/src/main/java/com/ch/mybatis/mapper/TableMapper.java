package com.ch.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

public interface TableMapper {

    void create(@Param("sourceName") String sourceName, @Param("targetName") String targetName);

    void truncate(@Param("tableName") String tableName);

    int isExists(@Param("schemaName") String schemaName, @Param("tableName") String tableName);
}
