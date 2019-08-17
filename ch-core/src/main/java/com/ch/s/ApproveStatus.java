package com.ch.s;

import com.ch.utils.CommonUtils;

public enum ApproveStatus {

    BEGIN(0, "待审核"),
    SUCCESS(1, "审核通过"),
    REFRESH(2, "重新审核"),
    CANCEL(3, "取消审核"),
    REJECT(4, "审核驳回"),
    RUNNING(5, "审核中"),
    UNKNOWN(-1, "未知状态");

    private final int code;
    private final String name;

    ApproveStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ApproveStatus fromValue(Integer code) {
        if (code == null) return UNKNOWN;
        ApproveStatus status = UNKNOWN;
        switch (code) {
            case 0:
                status = BEGIN;
                break;
            case 1:
                status = SUCCESS;
                break;
            case 2:
                status = REFRESH;
                break;
            case 3:
                status = CANCEL;
                break;
            case 4:
                status = REJECT;
                break;
            case 5:
                status = RUNNING;
                break;
        }
        return status;
    }

    public static ApproveStatus fromValue(String code) {
        if (CommonUtils.isNumeric(code)) {
            return fromValue(Integer.valueOf(code));
        }
        return UNKNOWN;
    }

    public boolean isOK() {
        return this == SUCCESS;
    }
}
