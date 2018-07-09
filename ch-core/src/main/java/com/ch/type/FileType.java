package com.ch.type;

/**
 * FileType 网络文件类型
 * Created by 80002023 on 2017/4/6.
 */
public enum FileType {
    JPG("image/jpeg"),//
    PNG("image/gif"),//
    GIF("image/gif"),//
    TXT("text/plain"),//
    UNKNOWN("");

    private final String code;

    FileType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
