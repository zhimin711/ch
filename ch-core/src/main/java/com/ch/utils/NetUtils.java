package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
     * 解析网址,返回根网址
     *
     * @param url HTTP或HTTPS网络地址
     * @return 域名或IP地址
     */
    public static String parseUrl(String url) {
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
        return StringUtils.isNotBlank(url) && (url.startsWith(HTTP_PROTOCOL) || url.startsWith(HTTPS_PROTOCOL));
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
    public static boolean isIP(String address) {
        if (StringUtils.isNotBlank(address)) {
            address = address.trim();
        }
        if (address.length() < 7 || address.length() > 15) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_IP);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    /**
     * 获取本机IP
     *
     * @return 本机IP
     */
    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get local host ip error!",e);
        }
        return "255.255.255.255";
    }
    /**
     * 获取本机IP byte[]
     *
     * @return 本机IP byte[]
     */
    public static byte[] getLocalAddress() {
        try {
            return InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            logger.error("get local host ip byte error!",e);
        }
        return null;
    }
}
