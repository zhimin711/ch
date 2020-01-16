package com.ch.utils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * decs:
 *
 * @author 01370603
 * @date 2020/1/16
 * @Description 处理数字的工具类
 */
public class DigitUtils {


    //读音与汉字对应的表
    private static final Map<Character, Integer> ARAB_2_CHINESE = new HashMap<Character, Integer>() {
        {
            put('零', 0);
            put('一', 1);
            put('二', 2);
            put('三', 3);
            put('四', 4);
            put('五', 5);
            put('六', 6);
            put('七', 7);
            put('八', 8);
            put('九', 9);
            put('十', 10);

        }
    };

    /**
     * 单位对应数量级的表
     */
    private static final Map<Character, Integer> UnitMap = new HashMap<Character, Integer>() {
        {
            put('十', 10);
            put('百', 100);
            put('千', 1000);
            put('万', 10000);
            put('亿', 10000 * 10000);

        }
    };

    //正则提取
    private static Pattern pattern = Pattern.compile("[零一二三四五六七八九十]?[十百千万亿]?");

    /**
     * 处理思路：
     * <p>
     * 可能的类型：
     * 一千三百万  130000000
     * 十一       11
     * 一万       10000
     * 一百二十五  125
     * <p>
     * 通过正则将字符串处理为 数字+单位的情况
     * 然后处理的时候通过数字*单位+数字*单位...得到最后结果
     *
     * @param chinese 汉字输入，比如一千三百二十八
     * @return 阿拉伯输入，比如 1328
     */
    private static Integer chinese2Arab(String chinese) {

        Objects.requireNonNull(chinese);

        //判断参数合法性
        if (!pattern.matcher(chinese).replaceAll("").trim().equals("")) {
            throw new InvalidParameterException();
        }

        Integer result = 0;

        Matcher matcher = pattern.matcher(chinese);

        //正则提取所有数字
        while (matcher.find()) {
            String res = matcher.group(0);
            if (res.length() == 2) {
                result += ARAB_2_CHINESE.get(res.charAt(0)) * UnitMap.get(res.charAt(1));
            } else if (res.length() == 1) {
                if (CommonUtils.isEquals(res, "十")) {
                    result += 10;
                } else if (UnitMap.containsKey(res.charAt(0))) {
                    //处理三十、一百万的情况
                    result *= UnitMap.get(res.charAt(0));
                } else if (ARAB_2_CHINESE.containsKey(res.charAt(0))) {
                    result += ARAB_2_CHINESE.get(res.charAt(0));
                }
            }
        }
        return result;
    }

    /**
     * 将汉字中的数字转换为阿拉伯数字
     * 处理思路：
     * 1.当大于万需求
     * 2.处理万以上的中文数字
     * <p>
     * 可能的类型：
     * 一千三百万  130000000
     * 十一       11
     * 一万       10000
     * 一百二十五  125
     * <p>
     * 通过正则将字符串处理为 数字+单位的情况
     * 然后处理的时候通过数字*单位+数字*单位...得到最后结果
     *
     * @param chinese 汉字输入，比如一千三百二十八
     * @return 阿拉伯输入，比如 1328
     */
    public static Integer chinese2Num(String chinese) {

        Objects.requireNonNull(chinese);

        //判断参数合法性
//        if (!pattern2.matcher(chinese).replaceAll("").trim().equals("")) {
//            throw new InvalidParameterException();
//        }

        Integer result = 0;

        String tmp = chinese;
        if (chinese.contains("亿")) {
            String s = chinese.substring(0, chinese.indexOf("亿"));
            Integer n1 = chinese2Arab(s);
            tmp = tmp.substring(chinese.indexOf("亿") + 1);
            result += n1 * 100000000;
        }
        if (tmp.contains("万")) {
            String s = tmp.substring(0, tmp.indexOf("万"));
            Integer n1 = chinese2Arab(s);
            tmp = tmp.substring(tmp.indexOf("万") + 1);
            result += n1 * 10000;
        }
        if (CommonUtils.isNotEmpty(tmp)) {
            Integer n1 = chinese2Arab(tmp);
            result += n1;
        }

        return result;
    }



    /**
     * 数字转中文(忽略小数)
     * Java 好用的
     * int 数字转中文
     *
     * @param number 数字
     * @return
     */
    public static String num2Chinese(Number number) {
        if (number == null) return "";
        int src = number.intValue();
        final String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        StringBuilder dst = new StringBuilder();
        int count = 0;
        while (src > 0) {
            dst.insert(0, (num[src % 10] + unit[count]));
            src = src / 10;
            count++;
        }
        if (dst.toString().startsWith("一十")) {
            dst = new StringBuilder(dst.substring(1));
        }
        return dst.toString().replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");

    }

}
