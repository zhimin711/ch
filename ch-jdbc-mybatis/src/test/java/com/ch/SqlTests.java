package com.ch;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * @author 01370603
 * @date 2018/9/6 16:24
 */
public class SqlTests {

    @Test
    public void testSql() {
        String sql = "SELECT count(0) FROM LF_EVENT WHERE (APP_CODE IN (?, ?, ?, ?, ?, ?, ?, ?, ?) AND LEVEL = ? AND TIMESTAMP BETWEEN ? AND ?) LIMIT 1,10";

    }

}
