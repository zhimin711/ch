package com.ch.type;

/**
 * FileContentType 网络文件类型
 * Created by 80002023 on 2017/4/6.
 */
public enum FileType {
    IMAGE("image"),//
    MUSIC("music"),//
    VIDEO("video"),//
    SCRAWL("scrawl"),//
    DOC("doc"),//
    EXCEL("excel"),//
    PPT("ppt"),//
    ZIP("zip"),//
    UNKNOWN("file");

    private final String code;

    FileType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static FileType fromValue(String code) {
        if (code == null) return UNKNOWN;
        FileType type;
        switch (code.toLowerCase()) {
            case "image":
                type = IMAGE;
                break;
            case "music":
                type = MUSIC;
                break;
            case "video":
                type = VIDEO;
                break;
            default:
                type = UNKNOWN;
        }
        return type;
    }
}
