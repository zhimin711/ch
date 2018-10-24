package com.ch.err;

import java.io.Serializable;

/**
 * desc:
 *
 * @author zhimin
 * @date 2018/8/13 上午8:48
 */
public class Error implements Serializable {

    private String code;
    private String name;

    public Error(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public Error(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.name = errorCode.getName();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
