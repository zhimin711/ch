package com.ch.mybatis.service;

import com.ch.mybatis.mapper.TableMapper;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringExtUtils;

public class TableServiceImpl implements TableService {

    private TableMapper tableMapper;

    public void setTableMapper(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    public static final String[] sList = new String[]{" ", "-", "%"};

    @Override
    public void create(String sourceName, String targetName) {
        if (CommonUtils.isNotEmpty(sourceName, targetName)) {
            if (StringExtUtils.isExists(sourceName, sList)) {
                throw new IllegalArgumentException("sourceName is invalid parameter!");
            }
            if (StringExtUtils.isExists(targetName, sList)) {
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
            if (StringExtUtils.isExists(tableName, sList)) {
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
            if (StringExtUtils.isExists(schemaName, sList)) {
                throw new IllegalArgumentException("schemaName is invalid parameter!");
            }
            if (StringExtUtils.isExists(tableName, sList)) {
                throw new IllegalArgumentException("tableName is invalid parameter!");
            }
        } else {
            throw new IllegalArgumentException("schemaName or tableName is empty!");
        }
        return tableMapper.isExists(schemaName, tableName);
    }
}
