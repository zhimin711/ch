package com.ch.helper.tools;

import com.ch.helper.pojo.ResInfo;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * ssh登录linux以后的一些操作方式
 *
 * @author zhimin
 * @version 1.0
 */
public class SSHHelper extends ServerHelper {

    private final static Logger logger = LoggerFactory.getLogger(SSHHelper.class);

    public SSHHelper(String host, Integer port, String user, String password) throws JSchException {
        super(host, port, user, password);
    }

    @Override
    String getChannelType() {
        return "exec";
    }


    /**
     * @param command
     * @return
     * @throws Exception
     */
    public ResInfo sendCmd(String command) throws Exception {
        return sendCmd(command, 0);
    }

    /**
     * @param command
     * @param timeout
     * @return
     * @throws Exception
     */
    public ResInfo sendCmd(String command, int timeout) throws Exception {
        return sendCmd(command, timeout, 0, 0);
    }

    /**
     * @param command
     * @param timeout
     * @param limit
     * @return
     * @throws Exception
     */
    public ResInfo sendCmd(String command, int timeout, int limit) throws Exception {
        return sendCmd(command, timeout, limit, 0);
    }


    /**
     * 执行命令，返回执行结果
     *
     * @param command 命令
     * @param limit   限制输出行数
     * @param delay   估计shell命令执行时间
     * @return String 执行命令后的返回
     * @throws Exception
     */
    public ResInfo sendCmd(String command, int timeout, int limit, int delay) throws Exception {
        ResInfo result;
        byte[] tmp = new byte[1024]; //读数据缓存
        StringBuilder strBuffer = new StringBuilder();  //执行SSH返回的结果
        StringBuilder errResult = new StringBuilder();

        ChannelExec ssh = (ChannelExec) getChannel();
        //返回的结果可能是标准信息,也可能是错误信息,所以两种输出都要获取
        //一般情况下只会有一种输出.
        //但并不是说错误信息就是执行命令出错的信息,如获得远程java JDK版本就以
        //ErrStream来获得.
        InputStream stdStream = ssh.getInputStream();
        InputStream errStream = ssh.getErrStream();

        ssh.setCommand(command);
        ssh.connect(timeout);
        try {

            int countLine = 0;

            //开始获得SSH命令的结果
            while (true) {
                //获得错误输出
                while (errStream.available() > 0) {
                    int i = errStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    errResult.append(new String(tmp, 0, i));
                }
                //获得标准输出
                while (stdStream.available() > 0) {
                    int i = stdStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    strBuffer.append(new String(tmp, 0, i));
//                    logger.info(new String(tmp, 0, i));
                    countLine++;
                    if (countLine > 1000) {
                        break;
                    }
                }
                if (ssh.isClosed()) {
                    int code = ssh.getExitStatus();
//                    logger.info("exit-status: " + code);
                    result = new ResInfo(code, strBuffer.toString(), errResult.toString());
                    break;
                } else if (limit > 0 && countLine > 1000) {
                    result = new ResInfo(99, strBuffer.toString(), errResult.toString());
                    break;
                }
                sleep(delay);
            }
        } finally {
            closeChannel();
        }

        return result;
    }


}