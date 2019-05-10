package com.ch.tools.helper;

import com.ch.e.PubError;
import com.ch.tools.pojo.FileInfo;
import com.ch.utils.*;
import com.google.common.collect.Lists;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * FTP 工具
 * Created by 01370603 on 2017/11/14.
 */
public class FtpHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FTPClient client;

    public FtpHelper(String host, String username, String password) throws IOException {
        connect(host, 0, username, password);
    }

    public FtpHelper(String host, int port, String username, String password) throws IOException {
        connect(host, port, username, password);
    }

    private void connect(String host, int port, String username, String password) throws IOException {
        if (client == null) {
            client = new FTPClient();
        }
        if (port > 0) {
            client.connect(host, port);
        } else {
            client.connect(host);
        }
        client.login(username, password);


        client.setFileType(FTPClient.BINARY_FILE_TYPE);

        //设置linux环境
        if (PlatformUtils.isLinux() || PlatformUtils.isUnix()) {
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            client.configure(conf);
        }

        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            logger.error("FTP server refused connection.");
            disconnect();
            throw ExceptionUtils.create(PubError.CONNECT,"FTP server refused connection!");
        }
    }

    public void disconnect() {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                logger.error("disconnect!", e);
            }
        }
    }

    /**
     * 显示指定目录下所有文件(包含目录)
     *
     * @param dir 指定目录
     * @return 指定目录文件集合
     */
    public List<FileInfo> listFiles(String dir) {
        return listFiles(dir, null, null);
    }

    /**
     * 显示指定目录下指定类型文件
     *
     * @param dir           显示目录
     * @param fileExtension 显示文件扩展后缀
     * @return 指定目录文件集合
     */
    public List<FileInfo> listFiles(final String dir, final String fileExtension) {
        return listFiles(dir, fileExtension, null);
    }

    /**
     * 显示指定目录下指定类型文件并过滤文件名
     *
     * @param dir           指定目录
     * @param fileExtension 文件类型(后缀)
     * @param fileName      过滤文件名(包含)
     * @return 指定目录文件集合
     */
    public List<FileInfo> listFiles(final String dir, final String fileExtension, final String fileName) {
        try {
            FTPFile[] ftpFiles;
            String tmpDir = CommonUtils.isEmpty(dir) ? "" : dir;
            if (CommonUtils.isNotEmpty(fileExtension) && CommonUtils.isNotEmpty(fileName)) {
                ftpFiles = client.listFiles(tmpDir, file ->
                        fileExtension.equals(FileExtUtils.getFileExtensionName(file.getName()))
                                && file.getName().contains(fileName));
            } else if (CommonUtils.isNotEmpty(fileExtension)) {
                ftpFiles = client.listFiles(tmpDir, file -> fileExtension.equals(FileExtUtils.getFileExtensionName(file.getName())));
            } else if (CommonUtils.isNotEmpty(fileName)) {
                ftpFiles = client.listFiles(tmpDir, file -> file.getName().contains(fileName));
            } else if (CommonUtils.isNotEmpty(tmpDir)) {
                ftpFiles = client.listFiles(tmpDir);
            } else {
                ftpFiles = client.listFiles();
            }
            return covertToFileInfo(ftpFiles);
        } catch (IOException e) {
            logger.error("listFiles", e);
        }
        return Lists.newArrayList();
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param targetFile 输出文件
     * @return 成功或失败
     */
    public boolean download(String remoteFile, String targetFile) {
        return download(remoteFile, new File(targetFile));
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param file       输出文件
     * @return 成功或失败
     */
    public boolean download(String remoteFile, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            return client.retrieveFile(remoteFile, os);
        } catch (IOException e) {
            logger.error("download file!", e);
        } finally {
            IOUtils.close(os);
        }
        return false;
    }

    private List<FileInfo> covertToFileInfo(FTPFile[] ftpFiles) {
        List<FileInfo> files = Lists.newArrayList();
        if (CommonUtils.isEmpty(ftpFiles)) {
            return Lists.newArrayList();
        }
        for (FTPFile file : ftpFiles) {
            FileInfo info = new FileInfo();
            info.setFileName(file.getName());
            info.setFileSize(file.getSize());
            info.setModifyAt(file.getTimestamp().getTime());
            if (file.isDirectory()) {
                info.setDir(true);
            }
            files.add(info);
        }
        return files;
    }
}
