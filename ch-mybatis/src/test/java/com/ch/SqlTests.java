package com.ch;

import com.ch.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 01370603
 * @date 2018/9/6 16:24
 */
@Slf4j
public class SqlTests {

    private final static Logger logger = LoggerFactory.getLogger(SqlTests.class);

    @Test
    public void testSql() {
        String sql = "SELEcT count(0) FROM LF_EVENT WHERE (APP_CODE IN \n(?, ?, ?, ?, ?, ?, ?, ?, ?) AND LEVEL = \n? AND TIMESTAMP BETWEEN ? AND ?) limit 1,10";
        String sql1 = "INSERT INTO st_permission (ID, NAME, CODE, URL, TYPE, ICON, SORT, IS_SHOW, IS_SYS, PARENT_ID, PARENT_NAME, DESCRIPTION, STATUS, CREATE_BY, CREATE_AT, UPDATE_BY, UPDATE_AT) VALUES (21, '权限删除', 'ADMIN_PERMISSION_DELETE', '/permission/delete', '3', 'icon-user', 4, '1', '1', '4', '权限管理', null, '1', null, '2018-07-28 01:35:33', null, null);\n";
        log.info("sql type: {}", SQLUtils.parseType(sql1));
        log.info("isSelect: {}", SQLUtils.isSelect(sql1));
        log.info("hasLimit: {}", SQLUtils.hasLimit(sql));
        log.info("sql: {}", SQLUtils.trimComment(sql));
    }

    public void testBase() {

    }

}
