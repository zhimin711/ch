package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/3/6.
 * @version 1.0
 * @since 1.8
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 通过文件名获取后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        logger.info("File name: {}", fileName);
        int start = fileName.lastIndexOf(".");
        String suffix = fileName.substring(start);
        if (CommonUtils.isNotEmpty(suffix)) {
            return suffix;
        }
        return null;
    }
}
