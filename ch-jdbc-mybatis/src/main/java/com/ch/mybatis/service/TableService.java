package com.ch.mybatis.service;

/**
 * 表服务
 */
public interface TableService {

    void create(String sourceName, String targetName);

    void truncate(String tableName);

    int isExists(String schemaName, String tableName);
}
