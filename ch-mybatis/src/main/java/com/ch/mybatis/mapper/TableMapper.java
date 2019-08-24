package com.ch.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TableMapper {
    /**
     * 复制表结构
     *
     * @param sourceTableName 源表名
     * @param targetTableName 新表名
     */
    void create(@Param("sourceName") String sourceTableName, @Param("targetName") String targetTableName);

    /**
     * 截断表
     *
     * @param tableName 表名
     */
    void truncate(@Param("tableName") String tableName);

    /**
     * 指定数据库检查表是否存在
     *
     * @param schemaName 库名
     * @param tableName 表名
     * @return 0/1
     */
    int isExists(@Param("schemaName") String schemaName, @Param("tableName") String tableName);
}
