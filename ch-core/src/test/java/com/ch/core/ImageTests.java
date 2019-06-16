package com.ch.core;

import com.ch.utils.ImageUtils;
import org.junit.Test;

import java.io.IOException;

public class ImageTests {

    @Test
    public void testNet() throws IOException {
        String newFilePath;
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559879537354&di=ac454d3b50e4e59dcdddb1d2cb7788f8&imgtype=0&src=http%3A%2F%2Fwww.33lc.com%2Farticle%2FUploadPic%2F2012-9%2F20129910515514039.jpg";
//        ImageUtils.read(url);
        newFilePath = "D:\\work\\tmp\\test1.jpg";
        ImageUtils.reduceImageByRatio(url, newFilePath, 2, 2);
//        ImageUtils.reduceImageEqualProportion(url, newFilePath, 2);
        newFilePath = "D:\\work\\tmp\\test2.jpg";
        ImageUtils.subRatio(url, newFilePath, 2.2f);
        newFilePath = "D:\\work\\tmp\\test3.jpg";
        ImageUtils.cropImage(url, newFilePath, 0, 0, 3000, 2000, "jpg", "jpg");
        newFilePath = "D:\\work\\tmp\\test4.jpg";
        ImageUtils.resizeImage(url, newFilePath, 3000, 2000);
    }
}
