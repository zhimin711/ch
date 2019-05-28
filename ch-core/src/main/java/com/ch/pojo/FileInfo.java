package com.ch.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件基本信息
 *
 * @author 01370603
 * @date 2018/12/24 19:41
 */
@Data
public class FileInfo implements Serializable {

    private long size;
    private String title;
    private String url;
    private String type;
    private String original;

}
