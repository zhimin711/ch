package com.ch.utils;

import com.ch.e.CoreError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

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
     * 按比例裁剪图片
     *
     * @param imagePath   图片路径
     * @param targetImage 裁剪图片路径
     * @param ratio       比例
     * @return
     */
    public static void subRatio(String imagePath, String targetImage, float ratio) {
        try {
            BufferedImage bufImage;
            if (NetUtils.isProtocolURL(imagePath)) {
                String imgUrl = URLDecoder.decode(imagePath, "UTF-8");
                URL url = new URL(imgUrl);
                bufImage = ImageIO.read(url);
            } else {
                File file = new File(imagePath);
                if (!file.exists()) {
                    throw ExceptionUtils.create(CoreError.NOT_EXISTS);
                }
                bufImage = ImageIO.read(file);
            }
            int oW = bufImage.getWidth();
            int oH = bufImage.getHeight();
            if (oH <= 0) return;
            float oR = oW * 1.0f / oH;
            int w = oW;
            int h = oH;
            if (oR > ratio) {
                w = (int) (oH * ratio);
            } else if (oR < ratio) {
                h = (int) (oW / ratio);
            }
            BufferedImage subImage = bufImage.getSubimage(0, 0, w, h);

            File targetFile = new File(targetImage);
            if (!targetFile.exists()) {
                FileExtUtils.create(targetFile);
            }
            ImageIO.write(subImage, FileExtUtils.getFileExtensionName(imagePath), targetFile);
        } catch (IOException e) {
            logger.info("sub image file of ratio error!", e);
        }
    }
}
