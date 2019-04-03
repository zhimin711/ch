package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    /**
     * @param imagePath
     * @return
     */
    public static String sub(String imagePath, float ratio) {
        File file = new File(imagePath);
        try {
            BufferedImage bufImage = ImageIO.read(file);
            int oW = bufImage.getWidth();
            int oH = bufImage.getHeight();
            if (oH <= 0) return null;
            float oR = oW * 1.0f / oH;
            int w = oW;
            int h = oH;
            if (oR > ratio) {
                w = (int) (oH * ratio);
            } else if (oR < ratio) {
                h = (int) (oW / ratio);
            }
            BufferedImage subImage = bufImage.getSubimage(0, 0, w, h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
