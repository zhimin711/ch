package com.ch.utils;

import com.ch.e.CoreError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;

/**
 * 描述：文件工具类
 *
 * @author 80002023
 * 2017/3/6.
 * @version 1.0
 * @since 1.8
 */
public class FileExtUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileExtUtils.class);

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;
    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;
    /**
     * The number of bytes in a terabyte.
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;
    /**
     * The number of bytes in a petabyte.
     */
    public static final long ONE_PB = ONE_KB * ONE_TB;
    /**
     * The number of bytes in an exabyte.
     */
    public static final long ONE_EB = ONE_KB * ONE_PB;

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
     * <pre>
     * 获取文件的编码
     * @param file
     *            File对象实例
     * </pre>
     *
     * @return 文件编码，eg：UTF-8,GBK,GB2312形式(不确定的时候，返回可能的字符编码序列)；若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    //判断编码格式方法
    public static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) { // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                        } else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;

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
                    throw ExceptionUtils.create(CoreError.OUT_OF_LIMIT, "File input is too large!");
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

    /**
     * @param paths
     * @return
     */
    public static String linkPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        if (paths == null) return sb.toString();
        for (String path : paths) {
            if (CommonUtils.isEmpty(path)) {
                continue;
            }
            String tmp = path.trim();
            if (tmp.startsWith(File.separator)) {
                tmp = tmp.substring(1);
            }
            if (tmp.endsWith(File.separator)) {
                tmp = tmp.substring(0, tmp.length() - 1);
            }
            sb.append(File.separator).append(tmp);
        }
        return sb.toString();
    }

    public static String convertToUnix(String path) {
        if(CommonUtils.isEmpty(path)) return "";
        return path.replaceAll("//|\\\\","/");
    }

}
