package com.ch.mybatis.service;

import com.ch.mybatis.mapper.TableMapper;
import com.ch.utils.CommonUtils;

public class TableServiceImpl implements TableService {

    private TableMapper tableMapper;

    public void setTableMapper(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    @Override
    public void create(String sourceName, String targetName) {
        if (CommonUtils.isNotEmpty(sourceName, targetName)) {
            if (sourceName.contains(" ") || sourceName.contains("-") || sourceName.contains("%")) {
                throw new IllegalArgumentException("sourceName is invalid parameter!");
            }
            if (targetName.contains(" ") || targetName.contains("-") || targetName.contains("%")) {
                throw new IllegalArgumentException("sourceName is invalid parameter!");
            }
        } else {
            throw new IllegalArgumentException("sourceName or targetName is empty!");
        }
        tableMapper.create(sourceName, targetName);
    }

    @Override
    public void truncate(String tableName) {
        if (CommonUtils.isNotEmpty(tableName)) {
            if (tableName.contains(" ") || tableName.contains("-") || tableName.contains("%")) {
                throw new IllegalArgumentException("tableName is invalid parameter!");
            }
        } else {
            throw new IllegalArgumentException("tableName empty!");
        }
        tableMapper.truncate(tableName);
    }

    @Override
    public int isExists(String schemaName, String tableName) {
        if (CommonUtils.isNotEmpty(schemaName, tableName)) {
            if (schemaName.contains(" ") || schemaName.contains("-") || schemaName.contains("%")) {
                throw new IllegalArgumentException("schemaName is invalid parameter!");
            }
            if (tableName.contains(" ") || tableName.contains("-") || tableName.contains("%")) {
                throw new IllegalArgumentException("tableName is invalid parameter!");
            }
        } else {
            throw new IllegalArgumentException("schemaName or tableName is empty!");
        }
        return tableMapper.isExists(schemaName, tableName);
    }
}
