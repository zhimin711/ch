package com.ch.helper.pojo;

import java.io.Serializable;

/**服务器
 * Created by 01370603 on 2017/12/2.
 */
public class ServerInfo implements Serializable {

    /**
     * IP或域名
     */
    private String ip;
    /**
     * 端口
     */
    private int port = 22;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 目录
     */
    private String dir;
    /**
     * 日期
     */
    private String date;
    /**
     * 过滤信息
     */
    private String filter;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
