package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class NetUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    public final static String HTTP_PROTOCOL = "http://";
    public final static String HTTPS_PROTOCOL = "https://";
    public final static String WWW = "www.";

    private NetUtils() {
    }

    /**
     * 判断IP格式和范围
     */
    public final static String REGEX_IP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    /**
     * 网址最后"/"处理
     *
     * @param url 字符串
     * @return true or false
     */
    public static String urlHandler(final String url) {
        if (!CommonUtils.isNotEmpty(url)) {
            return "";
        }
        String tmp = url;
        if (!tmp.startsWith("/")) {
            tmp = "/" + tmp;
        }
        if (tmp.endsWith("/")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        } else if (tmp.endsWith("*") && !tmp.endsWith("/*") && !tmp.endsWith(".*")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
//        logger.info("url[{}] handler [{}]", url, tmp);
        return tmp;
    }

    /**
     * 解析网址,返回根网址
     *
     * @param url HTTP或HTTPS网络地址
     * @return 域名或IP地址
     */
    public static String parseUrl(String url) {
        logger.debug(url);
        if (isURL(url)) {
            int start = 0;
            if (url.startsWith(HTTP_PROTOCOL)) {
                start = 7;
            } else if (isSSL(url)) {
                start = 8;
            }
            String s = url.substring(start);
            int end = s.indexOf("/", 1);
            if (end > 0) {
                return s.substring(0, end);
            } else {
                return s;
            }
        }
        return "";
    }

    /**
     * 判断是否是网址
     *
     * @param url HTTP或HTTPS网络地址
     * @return true or false
     */
    public static boolean isURL(String url) {
        return CommonUtils.isEmpty(url) && (url.startsWith(HTTP_PROTOCOL) || url.startsWith(HTTPS_PROTOCOL));
    }

    public static boolean isURL2(String url) {
        String p = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        Pattern pattern = Pattern.compile(p);

        return pattern.matcher(url).find();
    }

    /**
     * 判断是否是SSL网址
     *
     * @param url HTTPS网络地址
     * @return true or false
     */
    public static boolean isSSL(String url) {
        return url.startsWith(HTTPS_PROTOCOL);
    }

    /**
     * 判断是否是IP地址IPV4
     * IP的范围是0-255.0-255.0-255.0-255
     *
     * @param address IP地址
     * @return true or false
     */
    public static boolean isIPV4(String address) {
        if (CommonUtils.isEmpty(address)) {
            address = address.trim();
        }
        if (address.length() < 7 || address.length() > 15) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_IP);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }


}
