package com.ch.shiro.authc;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 自定义Shiro身份信息
 *
 * @author zhimin
 * @date 2018/8/11 下午10:28
 */
public class Principal implements Serializable {

    private String userId;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private List<String> codes;

    public Principal(String username) {
        this.username = username;
    }

    public Principal(String username, String userId, String realName, String mobile, String email) {
        this.username = username;
        this.userId = userId;
        this.realName = realName;
        this.phone = mobile;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
