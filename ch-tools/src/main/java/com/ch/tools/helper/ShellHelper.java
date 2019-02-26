package com.ch.tools.helper;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FTP工具类
 * Created by 01370603 on 2017/11/9.
 */
public class ShellHelper extends ServerHelper {

    private final static Logger logger = LoggerFactory.getLogger(ShellHelper.class);

    public ShellHelper(String host, Integer port, String user, String password) throws JSchException {
        super(host, port, user, password);
    }

    @Override
    String getChannelType() {
        return "shell";
    }

    public void getRemoteFilesToServer() {
        try {
            ChannelShell channel = (ChannelShell) getChannel();
            channel.connect();
            InputStream is = channel.getInputStream();
            OutputStream os = channel.getOutputStream();
//            String cmd = "ftp 10.116.218.80";
            String cmd = "cd /home/sfapp";
            os.write(cmd.getBytes());
//            cmd = "189599";
//            os.write(cmd.getBytes());
//            cmd = "sf123456";
//            os.write(cmd.getBytes());
            os.flush();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            int timeout = 10000000;
            //获取命令执行的结果
            while (true) {
                if (is.available() > 0) {
                    byte[] data = new byte[is.available()];
                    int nLen = is.read(data);

                    if (nLen < 0) {
                        throw new IOException("network error.");
                    }

                    //转换输出结果并打印出来
                    String temp = new String(data, 0, nLen, "iso8859-1");
                    logger.info(temp);
                    cmd = "pwd";
                    os.write(cmd.getBytes());
                    os.flush();
                    Thread.sleep(1000);
                }
                if (--timeout < 0) break;
            }
            is.close();
            os.close();
        } catch (JSchException e) {
            logger.error("JSchException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
        } finally {
            close();
        }
    }
}
