package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;

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
     * @param file
     * @return
     * @throws IOException
     */
    public static String getCharset(File file) {
        int p = 0;
        InputStream is = null;
        BufferedInputStream bin = null;
        try {
            is = new FileInputStream(file);
            bin = new BufferedInputStream(is);
            p = (bin.read() << 8) + bin.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bin, is);
        }
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

    /**
     * 转换文件大小
     *
     * @param size 文件大小(B)
     * @return 最大单位大小
     */
    public static String convertSize(final Long size) {
        String fileSize;

        DecimalFormat df = new DecimalFormat(".##");
        if (size == null) {
            fileSize = "-";
        } else if (size > ONE_TB) {
            fileSize = df.format(size * 1.0 / ONE_TB) + " TB";
        } else if (size > ONE_GB) {
            fileSize = df.format(size * 1.0 / ONE_GB) + " GB";
        } else if (size > ONE_MB) {
            fileSize = df.format(size * 1.0 / ONE_MB) + " MB";
        } else if (size > ONE_KB) {
            fileSize = df.format(size * 1.0 / ONE_KB) + " KB";
        } else {
            fileSize = size + " B";
        }
        return fileSize;
    }
}
