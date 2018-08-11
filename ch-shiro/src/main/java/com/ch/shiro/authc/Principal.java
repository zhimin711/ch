package com.ch.shiro.authc;

import java.io.Serializable;

/**
 * desc:
 *
 * @author zhimin
 * @date 2018/8/11 下午10:28
 */
public class Principal implements Serializable {

    private String userId;
    private String username;
    private String phone;
    private String email;

    public Principal(String username) {
        this.username = username;
    }

    public Principal(String username, String userId, String mobile, String email) {
        this.username = username;
        this.userId = userId;
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
}
