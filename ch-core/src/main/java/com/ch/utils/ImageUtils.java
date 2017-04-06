package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImageUtils
 * Created by 80002023 on 2017/4/5.
 */
public class ImageUtils {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtils.class);


    public static String parseFileNameByUrl(String imgUrl) {
        if (StringUtils.isNotBlank(imgUrl)) {
            //
            logger.info("parse file name by url",imgUrl);
            if (!imgUrl.endsWith("/")) {
                final int startIndex = imgUrl.lastIndexOf("/");
                return imgUrl.substring(startIndex + 1);
            }
        }
        return "";
    }
}
