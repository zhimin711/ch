package com.ch.helper.tools;

import com.ch.utils.CommonUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器抽象工具类
 * Created by 01370603 on 2017/11/9.
 */
public abstract class ServerHelper {

    private final static Logger logger = LoggerFactory.getLogger(ServerHelper.class);

    private Session session;
    private Channel channel;

    public Session getSession() {
        return session;
    }

    public ServerHelper(String host, Integer port, String user, String password) throws JSchException {
        this(host, port, user, password, 5000);
    }


    public ServerHelper(String host, Integer port, String user, String password, Integer timeout) throws JSchException {
        connect(host, port, user, password, timeout);
    }

    /**
     * 连接sftp服务器
     *
     * @param host     远程主机ip地址
     * @param port     sftp连接端口，null 时为默认端口
     * @param user     用户名
     * @param password 密码
     * @return session - ssh会话
     * @throws JSchException
     */
    private Session connect(String host, Integer port, String user, String password, Integer timeout) throws JSchException {
        try {
            JSch jsch = new JSch();
            if (port != null) {
                session = jsch.getSession(user, host, port);
            } else {
                session = jsch.getSession(user, host);
            }
            if (CommonUtils.isNotEmpty(password)) {
                session.setPassword(password);
            }
            //设置第一次登陆的时候提示，可选值:(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //30秒连接超时
            session.setTimeout(timeout);

            session.connect();
        } catch (JSchException e) {
            logger.error("Server Helper 获取连接发生错误!", e);
            throw e;
        }
        return session;
    }

    public void sleep(int delay) {
        if (delay < 50) return;
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            logger.error("sleep error!", e);
        }
    }


    /**
     * 用完记得关闭，否则连接一直存在，程序不会退出
     */
    public void close() {
        closeChannel();
        closeSession();
    }

    public void closeChannel() {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
    }

    public void closeSession() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public Channel getChannel() throws JSchException {
        if (channel == null || channel.isClosed()) {
            openChannel();
        }
        return channel;
    }

    public void openChannel() throws JSchException {
        this.channel = getSession().openChannel(getChannelType());
    }

    abstract String getChannelType();
}
