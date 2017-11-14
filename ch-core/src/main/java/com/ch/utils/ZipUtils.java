package com.ch.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 描述：Zip 工具
 *
 * @author zhimin
 *         2016/9/12.
 * @version 1.0
 * @since JDK1.8
 */
public class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    private ZipUtils() {
    }

    /**
     * 解压缩（解压单个指定文件）
     *
     * @param zipFilePath 压缩文件绝对路径
     * @param outFilePath 解压输出目录
     * @param fileName    指定文件名
     * @return 成功或失败
     */
    public static boolean extract(String zipFilePath, String outFilePath, String fileName) {
        InputStream input = null;
        OutputStream output = null;
        ZipFile zipFile = null;
        try {
            File file = new File(zipFilePath);//压缩文件路径和文件名
            File outFile = new File(outFilePath);//解压后路径和文件名
            zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(fileName);//所解压的文件名
            input = zipFile.getInputStream(entry);
            output = new FileOutputStream(outFile);
            int temp;
            while ((temp = input.read()) != -1) {
                output.write(temp);
            }
            return true;
        } catch (Exception e) {
            logger.error("extract file error!", e);
        } finally {
            IOUtils.close(input, output, zipFile);
        }
        return false;
    }


    /**
     * zip压缩包解压
     * <p>包含中文名文件解压会报错</p>
     *
     * @param filePath   压缩包文件路径
     * @param extractDir 解压保存路径
     * @return 文件集合
     */
    public static List<File> unzip(String filePath, String extractDir) {
        List<File> fileList = new ArrayList<>();
        File source = new File(filePath);
        if (!source.exists()) {
            return Lists.newArrayList();
        }
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        try {
            zis = new ZipInputStream(new FileInputStream(source));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null
                    && !entry.isDirectory()) {
                FileUtils.create(extractDir);
                File target = new File(extractDir, UUID.randomUUID().toString() + FileUtils.getFileExtension(entry.getName()));
                // 写入文件
                bos = new BufferedOutputStream(new FileOutputStream(target));
                int read;
                byte[] buffer = new byte[1024 * 10];
                while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bos.flush();
                fileList.add(target);
            }
            zis.closeEntry();
        } catch (IOException e) {
            logger.error("unzip error!", e);
        } finally {
            IOUtils.close(zis, bos);
        }
        return fileList;
    }

    //zipFileName为需要解压的zip文件，extPlace为解压后文件的存放路径，两者均须已经存在

    /**
     * Ant解压zip压缩文件
     *
     * @param zipFileName 解压的zip文件绝对路径
     * @param targetDir   输出目录
     * @return 文件集合
     */
    public static List<File> unzipFile(String zipFileName, String targetDir) {
        List<File> fileList = new ArrayList<>();
        org.apache.tools.zip.ZipFile zipFile = null;
        try {
            zipFile = new org.apache.tools.zip.ZipFile(zipFileName);
            Enumeration e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry;
            InputStream in = null;
            OutputStream os = null;
            while (e.hasMoreElements()) {
                zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
                String entryName = zipEntry.getName();
                logger.info("unzip File name: {}", entryName);
                FileUtils.create(targetDir);
                File newFile = new File(targetDir, UUID.randomUUID().toString() + FileUtils.getFileExtension(entryName));
                try {
                    in = zipFile.getInputStream(zipEntry);
                    os = new FileOutputStream(newFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                    fileList.add(newFile);
                } catch (IOException e1) {
                    logger.error("unzip inner file error!", e1);
                } finally {
                    IOUtils.close(in, os);
                }
            }
            return fileList;
        } catch (Exception e) {
            logger.error("unzipFile  error!", e);
        } finally {
            IOUtils.close(zipFile);
        }
        return Lists.newArrayList();
    }
}
