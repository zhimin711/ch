package com.ch.helper.pojo;

import java.util.Date;

/**
 * 文件信息
 * Created by 01370603 on 2017/11/14.
 */
public class FileInfo {

    private String fileName;
    private String path;
    private Long fileSize;
    private Date modifyAt;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }
}
