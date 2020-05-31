package com.ch.core;

import com.ch.utils.ImageUtils;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;

public class ImageTests {

    @Test
    public void test() throws IOException {
        String newFilePath;
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559879537354&di=ac454d3b50e4e59dcdddb1d2cb7788f8&imgtype=0&src=http%3A%2F%2Fwww.33lc.com%2Farticle%2FUploadPic%2F2012-9%2F20129910515514039.jpg";
        url = "D:\\work\\tmp\\test6.jpg";
//        ImageUtils.read(url);
        newFilePath = "D:\\work\\tmp\\out.jpg";
        ImageUtils.resizeImage(url, newFilePath, 800, 500);
//        ImageUtils.reduceImageByRatio(url, newFilePath, 0.5, 0.5);
//        ImageUtils.imageOp(url, newFilePath, 100, 100);
//        ImageUtils.reduceImageEqualProportion(url, newFilePath, 3);
        newFilePath = "D:\\work\\tmp\\test2.jpg";
//        ImageUtils.subRatio(url, newFilePath, 2.2f);
        newFilePath = "D:\\work\\tmp\\test3.jpg";
        newFilePath = "D:\\work\\tmp\\test5-1.jpg";
//        ImageUtils.crop(url, newFilePath, -10, 100, 300, 300);
        newFilePath = "D:\\work\\tmp\\test4.jpg";
//        ImageUtils.resizeImage(url, newFilePath, 3000, 2000);
    }

    @Test
    public void testType() {
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    }
}
