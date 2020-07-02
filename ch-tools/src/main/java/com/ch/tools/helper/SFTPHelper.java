package com.ch.tools.helper;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Vector;

/**
 * FTP工具类
 * Created by 01370603 on 2017/11/9.
 */
@Slf4j
public class SFTPHelper extends ServerHelper {

    public SFTPHelper(String host, Integer port, String user, String password) throws JSchException {
        super(host, port, user, password);
    }

    @Override
    String getChannelType() {
        return "sftp";
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    /**
     * 删除远程文件
     *
     * @param remoteFile
     * @return
     */
    public boolean deleteRemoteFileOrDirectory(String remoteFile) {
        try {
            ChannelSftp channel = (ChannelSftp) getChannel();
            channel.connect();
            SftpATTRS sftpATTRS = channel.lstat(remoteFile);
            if (sftpATTRS.isDir()) {
                //目录
                log.debug("remote File:dir");
                channel.rmdir(remoteFile);
                return true;
            } else if (sftpATTRS.isReg()) {
                //文件
                log.debug("remote File:file");
                channel.rm(remoteFile);
                return true;
            } else {
                log.debug("remote File:unkown");
                return false;
            }
        } catch (JSchException e) {
            close();
            log.error("deleteRemoteFileOrDirectory", e);
            return false;
        } catch (SftpException e) {
            log.error("deleteRemoteFileOrDirectory", e);
            return false;
        }

    }

    /**
     * 判断linux下 某文件是否存在
     *
     * @param remoteFile
     */
    public boolean detectedFileExist(String remoteFile) {
        try {
            ChannelSftp channel = (ChannelSftp) getChannel();
            channel.connect();
            SftpATTRS sftpATTRS = channel.lstat(remoteFile);
            if (sftpATTRS.isDir() || sftpATTRS.isReg()) {
                //目录 和文件
                log.info("remote File:dir");
                return true;
            } else {
                log.info("remote File: unkown");
                return false;
            }
        } catch (JSchException e) {
            close();
            return false;
        } catch (SftpException e) {
            log.error("detectedFileExist!", e);
        }
        return false;
    }

    public void getRemoteFiles(String dir) {
        try {
            ChannelSftp channel = (ChannelSftp) getChannel();
            channel.connect();
            Vector vector = channel.ls(dir);
            if (vector != null && !vector.isEmpty()) ;
            //vector.forEach(r -> log.info("==>", r));
        } catch (JSchException e) {
            log.error("JSchException", e);
        } catch (SftpException e) {
            log.error("SftpException", e);
        }

    }


}
