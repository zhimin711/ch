package com.ch.core;

import com.ch.pojo.KeyValue;
import com.ch.result.PageResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：PACKAGE_NAME
 *
 * @author 80002023
 * 2017/3/8.
 * @version 1.0
 * @since 1.8
 */
public class ResultTests {

    final Logger logger = LoggerFactory.getLogger(ResultTests.class);

    Object o;

    @Test
    public void testCommon() {
        PageResult<KeyValue> res = PageResult.success();

//        ExceptionUtils._throw(PubError.NOT_EXISTS);
    }

}
