package com.ch.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件基本信息
 *
 * @author 01370603
 * @date 2018/12/24 19:41
 */
@Data
@JsonIgnoreProperties("exists")
public class FileInfo implements Serializable {

    /**
     * 原文件名
     */
    private String original;
    /**
     * 存储文件名
     */
    private String title;
    /**
     * 地址
     */
    private String url;
    /**
     * 类型
     */
    private String type;
    /**
     * 文件大小
     */
    private long size;

    /**
     * 已存在
     */
    private boolean exists = false;

}
