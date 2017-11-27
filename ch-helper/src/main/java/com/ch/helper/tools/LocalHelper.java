package com.ch.helper.tools;

import com.ch.helper.pojo.ResInfo;
import com.ch.utils.IOUtils;
import com.ch.utils.PlatformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;

/**
 * 本地指令工具
 * Created by 01370603 on 2017/11/15.
 */
public class LocalHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ResInfo execute(String command) throws Exception {
        return execute(command, 0, 0);
    }

    public ResInfo execute(String command, int limit) throws Exception {
        return execute(command, limit, 0);
    }

    public ResInfo execute(String command, int limit, int delay) throws Exception {

        String[] commands = new String[]{"/bin/sh", "-c", command};
        if (PlatformUtils.isWindows()) {
            commands = new String[]{"cmd", "/c", command};
        }
        logger.info(Arrays.toString(commands));
        Process process = Runtime.getRuntime().exec(commands);

        if (PlatformUtils.isWindows() || limit <= 0) {
//            process.waitFor();
        }
        InputStream stdStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

        ResInfo result;
        StringBuilder strBuffer = new StringBuilder();  //执行SSH返回的结果
        StringBuilder errResult = new StringBuilder();
        int count = 0;

        byte[] tmp = new byte[1024]; //读数据缓存
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
                count++;
                if (limit > 0 && count > limit) {
                    break;
                }
            }
            sleep(delay);
            if (!process.isAlive()) {
                int code = process.exitValue();
//                    logger.info("exit-status: " + code);
                result = new ResInfo(code, strBuffer.toString(), errResult.toString());
                break;
            } else if (limit > 0 && count > limit) {
                process.destroy();
                result = new ResInfo(99, strBuffer.toString(), errResult.toString());
                break;
            }
        }
        return result;
    }

    public void sleep(int delay) {
        if (delay < 50) return;
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            logger.error("sleep error!", e);
        }
    }

    public ResInfo exec(String command) throws Exception {
        return exec(command, 0);
    }

    public ResInfo exec(String command, int limit) throws Exception {
        String[] commands = new String[]{"/bin/sh", "-c", command};
        if (PlatformUtils.isWindows()) {
            commands = new String[]{"cmd", "/c", command};
        }
        Process process = Runtime.getRuntime().exec(commands);
        LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
        //读取错误执行的返回流
        LineNumberReader er = new LineNumberReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        StringBuilder errResult = new StringBuilder();
        String line;
        int countLine = 0;
        int code = 0;
        try {
            while ((line = er.readLine()) != null) {
                errResult.append(line).append("\n");
            }
            while ((line = br.readLine()) != null) {

                if (sb.length() < 0) {
                    sb.append(line);
                } else {
                    sb.append("\n").append(line);
                }
                countLine++;
                if (limit > 0 && countLine > limit) {
                    code = 99;
                    break;
                }
            }

        } finally {
            IOUtils.close(br, er);
        }
        if (process.isAlive()) process.destroy();
        return new ResInfo(code, sb.toString(), errResult.toString());
    }
}
