package com.ch.shiro;

import com.ch.utils.CommonUtils;

public enum LoginStatus {

    SUCCESS("1", "登录成功"),//
    FAILED("0", "登录失败");
    private final String value;
    private final String name;

    LoginStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static LoginStatus fromValue(String value) {
        if (CommonUtils.isEmpty(value)) {
            return FAILED;
        }
        switch (value) {
            case "1":
                return SUCCESS;
        }
        return FAILED;
    }
}
