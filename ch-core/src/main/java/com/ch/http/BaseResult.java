package com.ch.http;

import com.ch.Constants;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：com.zh.http
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseResult<T> {

    private int status;
    private Collection<T> records;
    private Set<Error> errors;

    public BaseResult(int status) {
        setStatus(status);
    }

    public BaseResult(Collection<T> records) {
        setStatus(Constants.SUCCESS);
        setRecords(records);
    }

    public BaseResult(T record) {
        setStatus(Constants.SUCCESS);
        this.put(record);
    }

    public void put(T record) {
        if (records == null) {
            records = new HashSet<T>();
        }
        records.add(record);
    }

    public void newError(String code, String name, String msg) {
        if (errors == null) {
            errors = new HashSet<>();
        } else {
            errors = errors.stream().filter(r -> !CommonUtils.isEquals(code, r.getCode())).collect(Collectors.toSet());
        }

        errors.add(new Error(code, name, msg));
    }


    public void newError(ErrorCode error, String msg) {
        if (errors == null) {
            errors = new HashSet<>();
        } else {
            errors = errors.stream().filter(r -> !CommonUtils.isEquals(error.getCode(), r.getCode())).collect(Collectors.toSet());
        }

        errors.add(new Error(error.getCode(), error.getName(), msg));
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Collection<T> getRecords() {
        return records;
    }

    public void setRecords(Collection<T> records) {
        this.records = records;
    }

    public Set<Error> getErrors() {
        return errors;
    }

    public void setErrors(Set<Error> errors) {
        this.errors = errors;
    }

    class Error implements Serializable {

        String code;
        String name;
        String msg;

        public Error() {
            setCode(null);
        }

        public Error(String code, String name, String msg) {
            setCode(code);
            setName(name);
            setMsg(msg);
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                code = ErrorCode.ERROR.getCode();
            }
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
