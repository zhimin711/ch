package com.ch.utils;

import com.ch.exception.OutOfLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;

/**
 * 描述：文件工具类
 *
 * @author 80002023
 *         2017/3/6.
 * @version 1.0
 * @since 1.8
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 通过文件名获得扩展名(带点)
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtension(String fileName) {
//        logger.info("File name: {}", fileName);
        if (CommonUtils.isEmpty(fileName)) {
            return "";
        }
        int start = fileName.lastIndexOf(".");
        if (start > 0) {
            String suffix = fileName.substring(start);
            if (CommonUtils.isNotEmpty(suffix)) {
                return suffix;
            }
        }
        return "";
    }

    /**
     * 通过文件名获得扩展名(不带点)
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtensionName(String fileName) {
        String extension = getFileExtension(fileName);
        if (CommonUtils.isEmpty(extension)) {
            return "";
        }
        return extension.substring(1);
    }

    /**
     * Txt file get charset
     *
     * @param file 文件
     * @return 编码
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

    /**
     * 读取文件流输出为字符串
     *
     * @param is 文件输入流
     * @return str - 文件内容字符
     */
    public static String readToString(final InputStream is) {
        byte[] tmp = new byte[1024]; //读数据缓存
        StringBuilder builder = new StringBuilder();
        try {
            while (is.available() > 0) {
                int i = is.read(tmp, 0, 1024);
                if (i < 0) break;
                builder.append(new String(tmp, 0, i));
                if (builder.length() / ONE_MB > 100) {
                    throw new OutOfLimitException("File input is  too large!");
                }
            }
        } catch (IOException e) {
            logger.info("readToString Error!", e);
        }
        return builder.toString();
    }

    /**
     * 创建文件或目录
     *
     * @param file
     * @return
     */
    public static boolean create(File file) {
        if (file == null) {
            return false;
        } else if (file.isFile()) {
            return true;
        } else if (file.isDirectory()) {
            return true;
        }
        boolean ok = false;
        if (!file.exists()) {
            ok = file.mkdir();
        }
        if (ok) {
            return true;
        } else {
            ok = create(file.getParent());
        }
        return ok && file.mkdir();
    }

    /**
     * 创建文件目录
     *
     * @param path
     * @return
     */
    public static boolean create(String path) {
        File file = new File(path);
        boolean ok = false;
        if (!file.exists()) {
            ok = file.mkdir();
        }
        if (ok) {
            return true;
        }
        String tmp = file.getParent();
        if (CommonUtils.isEmpty(tmp)) {
            return false;
        }
        ok = create(file.getParent());
        return ok && file.mkdir();
    }
}
