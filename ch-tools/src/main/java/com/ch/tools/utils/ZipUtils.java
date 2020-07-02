package com.ch.tools.utils;

import com.ch.utils.CommonUtils;
import com.ch.utils.FileExtUtils;
import com.ch.utils.IOUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 描述：Zip 工具
 *
 * @author zhimin
 * 2016/9/12.
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
public class ZipUtils {

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
            log.error("extract file error!", e);
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
                FileExtUtils.create(extractDir);
                File target = new File(extractDir, UUID.randomUUID().toString() + FileExtUtils.getFileExtension(entry.getName()));
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
            log.error("unzip error!", e);
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
        return unzipFile(zipFileName, targetDir, null, true);
    }

    /**
     * Ant解压zip压缩文件
     *
     * @param zipFileName 解压的zip文件绝对路径
     * @param targetDir   输出目录
     * @param extensions  输出文件后缀(过滤)
     * @return 文件集合
     */
    public static List<File> unzipFile(String zipFileName, String targetDir, Set<String> extensions, boolean useOriginalFileName) {
        List<File> fileList = new ArrayList<>();
        org.apache.tools.zip.ZipFile zipFile = null;
        try {
            zipFile = new org.apache.tools.zip.ZipFile(zipFileName);
            Enumeration<org.apache.tools.zip.ZipEntry> e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry;
            InputStream in = null;
            OutputStream os = null;
            FileExtUtils.create(targetDir);
            while (e.hasMoreElements()) {
                zipEntry = e.nextElement();
                String entryName = zipEntry.getName();
                log.info("unzip File name: {}", entryName);
                if (CommonUtils.isNotEmpty(extensions) && !extensions.contains(FileExtUtils.getFileExtensionName(entryName))) {
                    continue;
                }
                File newFile;
                if (zipEntry.isDirectory()) {
                    FileExtUtils.create(targetDir + File.separator + entryName);
                    continue;
                }
                if (useOriginalFileName) {
                    newFile = new File(targetDir + File.separator + entryName);
                } else {
                    newFile = new File(targetDir, UUID.randomUUID().toString() + FileExtUtils.getFileExtension(entryName));
                }
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
                    log.error("unzip inner file error!", e1);
                } finally {
                    IOUtils.close(in, os);
                }
            }
            return fileList;
        } catch (Exception e) {
            log.error("unzipFile  error!", e);
        } finally {
            IOUtils.close(zipFile);
        }
        return Lists.newArrayList();
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void zipFile(String sourcePath, String zipPath) {
        org.apache.tools.zip.ZipOutputStream zos = null;
        try {
            zos = new org.apache.tools.zip.ZipOutputStream(new FileOutputStream(zipPath));
            zos.setEncoding("gbk");//此处修改字节码方式。
            File srcFile = new File(sourcePath);
            if (!srcFile.exists()) {
                throw new FileNotFoundException("源文件或目录不存在！");
            }
            if (srcFile.isDirectory()) {//处理文件夹
                File[] files = srcFile.listFiles();
                if (files != null && files.length != 0) {
                    for (File f : files) {
                        writeZipFile(zos, f, null);
                    }
                }
            } else
                writeZipFile(zos, new File(sourcePath), null);
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败", e);
        } finally {
            IOUtils.close(zos);
        }
    }

    private static void writeZipFile(org.apache.tools.zip.ZipOutputStream zos, final File file, final String parentPath) {
        if (!file.exists()) {
            return;
        }
        String fileName = file.getName();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        String path = CommonUtils.isEmpty(parentPath) ? fileName : parentPath + File.separator + file.getName();
        if (file.isDirectory()) {//处理文件夹
            File[] files = file.listFiles();
            if (files != null && files.length != 0) {
                for (File f : files) {
                    writeZipFile(zos, f, path);
                }
            } else {
                try {//空目录则创建当前目录
                    zos.putNextEntry(new org.apache.tools.zip.ZipEntry(path));
                } catch (IOException ignored) {
                }
            }
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                zos.putNextEntry(new org.apache.tools.zip.ZipEntry(path));
                byte[] content = new byte[1024];
                int len;
                while ((len = fis.read(content)) != -1) {
                    zos.write(content, 0, len);
                    zos.flush();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
            } finally {
                IOUtils.close(fis);
            }
        }
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void zip(String sourcePath, String zipPath) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipPath));
//            zos.setEncoding("gbk");//此处修改字节码方式。
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败", e);
        } finally {
            IOUtils.close(zos);

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                String tmpName = file.getName();
                tmpName = new String(tmpName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                parentPath += tmpName + File.separator;
                File[] files = file.listFiles();
                if (files != null && files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (IOException e) {
                    log.error("创建ZIP文件失败", e);
                } finally {
                    IOUtils.close(fis);
                }
            }
        }
    }

}
