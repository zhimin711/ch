package com.ch.result;

import com.ch.e.IError;

import java.io.Serializable;
import java.util.Arrays;

/**
 * desc:自定义错误
 *
 * @author zhimin
 * @date 2018/8/13 上午8:48
 */
public class Error implements Serializable, IError {

    private String code;
    private String name;

    public Error() {
    }

    public Error(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Type getType() {
        return Type.CUSTOMER;
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


    @Override
    public String formatMsg(Object... args) {
        if (name.contains("%")) {
            return String.format(name, args);
        }
        return name + Arrays.toString(args);
    }
}
