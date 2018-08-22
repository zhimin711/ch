package com.ch.core;

import com.ch.utils.ZipUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 01370603
 * @date 2018/8/14 10:53
 */
public class ZipUtilsTests {
    final Logger logger = LoggerFactory.getLogger(ZipUtilsTests.class);

    Object o;

    @Test
    public void testZipFile() {
        String srcPath = "D:\\mnt\\tmp\\2018-08-14\\8af6becd653643500165364350a30000";
        String zipPath = "D:\\mnt\\tmp\\2018-08-14\\8af6becd653643500165364350a30000.zip";

        ZipUtils.zipFile(srcPath, zipPath);
    }

}
