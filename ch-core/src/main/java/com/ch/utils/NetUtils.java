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
    public final static String REGEX_IP_PROTOCOL = "^((http|https)://)?([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    public final static String REGEX_PROTOCOL = "^((http:)|(https:))";
    /**
     * 域名正则表达式1
     */
    public final static String REGEX_DOMAIN = "^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    public final static String REGEX_DOMAIN3 = "^(//)?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    /**
     * 域名正则表达式2
     */
    public final static String REGEX_DOMAIN2 = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";

    /**
     * 相对网址开始最后"/"处理
     *
     * @param url 字符串
     * @return true or false
     */
    public static String handlerSuffix(final String url) {
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
        return tmp;
    }

    public static String trim(final String url) {
        if (!CommonUtils.isNotEmpty(url)) {
            return "";
        }
        String tmp = url.trim();
        if (isProtocolURL(url)) {
            String base = parseBaseUrl(url);
            String suffix = parseSuffixUrl(url);
            suffix = suffix.replaceAll("//", "/");
            if (base.endsWith("/") && suffix.startsWith("/")) {
                tmp = base + suffix.substring(1);
            }
        } else {
            tmp = tmp.replaceAll("//", "/");
        }
        return tmp;
    }

    /**
     * 解析网址,返回带协议头的域名或IP
     *
     * @param url HTTP或HTTPS网络地址
     * @return 域名或IP地址
     */
    public static String parseBaseUrl(String url) {
        logger.debug("parseBaseUrl: {}", url);
        Pattern p = Pattern.compile(REGEX_DOMAIN);
        Matcher m = p.matcher(url);
        if (m.find()) {
            //匹配结果
            return m.group();
        }
        p = Pattern.compile(REGEX_IP_PROTOCOL);
        m = p.matcher(url);
        if (m.find()) return m.group();

        return "";
    }

    /**
     * 解析网址,返回请求相对地址
     *
     * @param url HTTP或HTTPS网络地址
     * @return 域名或IP地址
     */
    public static String parseSuffixUrl(String url) {
        logger.debug("parseSuffixUrl: {}", url);
        Pattern p = Pattern.compile(REGEX_DOMAIN);
        Matcher m = p.matcher(url);
        if (m.find()) {
            //匹配结果
            return url.replaceAll(REGEX_DOMAIN, "");
        }
        p = Pattern.compile(REGEX_IP_PROTOCOL);
        m = p.matcher(url);
        if (m.find()) return url.replaceAll(REGEX_IP_PROTOCOL, "");

        p = Pattern.compile(REGEX_DOMAIN2);
        m = p.matcher(url);
        if (m.find()) {
            return url.replaceAll(REGEX_DOMAIN2, "");
        }

        return "";
    }

    /**
     * 解析网址,返回根域名或IP（不匹配正则）
     *
     * @param url HTTP或HTTPS网络地址
     * @return 域名或IP地址
     */
    public static String parseUrl(String url) {
        logger.debug(url);
        if (isProtocolURL(url)) {
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
    public static boolean isProtocolURL(String url) {
        return CommonUtils.isNotEmpty(url) && (url.startsWith(HTTP_PROTOCOL) || url.startsWith(HTTPS_PROTOCOL));
    }

    public static boolean isURL2(String url) {
        Pattern pattern = Pattern.compile(REGEX_DOMAIN);
        return pattern.matcher(url).find();
    }

    public static boolean isURL3(String url) {
        Pattern pattern = Pattern.compile(REGEX_DOMAIN2);
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

    public static boolean isCDN(String url) {
        Pattern pattern = Pattern.compile(REGEX_DOMAIN3);
        return pattern.matcher(url).find();
    }

    public static String parseProtocol(String url) {
        Pattern pattern = Pattern.compile(REGEX_PROTOCOL);
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            //匹配结果
            return m.group();
        }
        return "";
    }
}
