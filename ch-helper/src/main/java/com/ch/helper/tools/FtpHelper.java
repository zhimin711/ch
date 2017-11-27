package com.ch.helper.tools;

import com.ch.exception.OutOfLimitException;
import com.ch.helper.pojo.FileInfo;
import com.ch.utils.CommonUtils;
import com.ch.utils.FileUtils;
import com.ch.utils.IOUtils;
import com.ch.utils.PlatformUtils;
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
            throw new OutOfLimitException("FTP server refused connection!");
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

    public List<FileInfo> listFiles(String dir) {
        try {
            FTPFile[] ftpFiles = client.listFiles(dir);
            return covertToFileInfo(ftpFiles);
        } catch (IOException e) {
            logger.error("listFiles", e);
        }
        return Lists.newArrayList();
    }

    public List<FileInfo> listFiles(final String dir, final String fileExtension) {
        try {

            FTPFile[] ftpFiles = client.listFiles(dir, file -> fileExtension.equals(FileUtils.getFileExtensionName(file.getName())));
            return covertToFileInfo(ftpFiles);
        } catch (IOException e) {
            logger.error("listFiles", e);
        }
        return Lists.newArrayList();
    }


    public List<FileInfo> listFiles(final String dir, final String fileExtension, final String fileName) {
        try {
            FTPFile[] ftpFiles = client.listFiles(dir, file -> fileExtension.equals(FileUtils.getFileExtensionName(file.getName())) && file.getName().contains(fileName));
            return covertToFileInfo(ftpFiles);
        } catch (IOException e) {
            logger.error("listFiles", e);
        }
        return Lists.newArrayList();
    }

    public boolean download(String remoteFile, String targetFile) {
        return download(remoteFile, new File(targetFile));
    }

    /**
     * @param remoteFile
     * @param file       output file
     * @return
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
            files.add(info);
        }
        return files;
    }
}
