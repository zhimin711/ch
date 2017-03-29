package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public static String getFileExtension(String fileName) {
        logger.info("File name: {}", fileName);
        int start = fileName.lastIndexOf(".");
        String suffix = fileName.substring(start);
        if (CommonUtils.isNotEmpty(suffix)) {
            return suffix;
        }
        return null;
    }

    /**
     * Txt file get charset
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String getCharset(InputStream inputStream) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        int p = (bin.read() << 8) + bin.read();
        String code;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }
}
