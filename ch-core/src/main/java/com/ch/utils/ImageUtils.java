package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片工具类
 * Created by 80002023 on 2017/4/5.
 */
public class ImageUtils {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtils.class);


    /**
     * 根据图片网络地址解析图片名
     *
     * @param imgUrl 图片网络地址
     * @return 图片名
     */
    public static String parseFileNameByUrl(String imgUrl) {
        if (CommonUtils.isEmpty(imgUrl)) {
            //
            logger.info("parse file name by url", imgUrl);
            if (!imgUrl.endsWith("/")) {
                final int startIndex = imgUrl.lastIndexOf("/");
                return imgUrl.substring(startIndex + 1);
            }
        }
        return "";
    }
}
