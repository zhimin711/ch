package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static final String SHORT_EN = "yyyy/MM/dd";
    public static final String SHORT_CN = "yyyy-MM-dd";
    public static final String SHORT_ZH_CN = "yyyy年MM月dd日";

    public static final String TIME_EN = "yyyy/MM/dd HH:mm:ss";
    public static final String TIME_CN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_ZH_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String TIME_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtils() {
    }

    public static String format(Date date) {
        if (null == date) {
            date = currentTime();
        }
        return format(date, TIME_CN);
    }

    public static String format(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = TIME_CN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatShort(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_CN);
        return sdf.format(date);
    }

    public static String formatShortOfCN(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_ZH_CN);
        return sdf.format(date);
    }

    public static String formatShortOfUTC(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_UTC);
        return sdf.format(date);
    }


    public static Date parse(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            logger.error("Parse date failed! date = " + dateStr + ", pattern = " + pattern, e);
        }
        return null;
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, TIME_CN);
    }

    public static Date currentTime() {
        return Calendar.getInstance().getTime();
    }

    /**
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int compare(Date dateStart, Date dateEnd) {
        return dateStart.compareTo(dateEnd);
    }

    /**
     * 日期时间字符串比较
     *
     * @param dateFormat 时间格式
     * @param date1      时间字符串1
     * @param date2      时间字符串2
     * @return 返回 -1：小于，0：等于，1：大于
     * @throws ParseException
     */
    public static int compareTo(String date1, String date2, String dateFormat) {
        if (!CommonUtils.isNotEmpty(dateFormat)) {
            dateFormat = SHORT_CN;
        }
        Date d1 = parse(date1, dateFormat);
        Date d2 = parse(date2, dateFormat);
        if (d1 == null && d2 == null) {
            return 0;
        } else if (d1 == null) {
            return -1;
        } else if (d2 == null) {
            return 1;
        }
        return d1.compareTo(d2);
    }

    public static Long convertExpiresToSeconds(Date expires) {
        logger.info("convert expires date seconds! {}", expires);
        Date dateTime = currentTime();
        if (dateTime.before(expires)) {
            return (expires.getTime() - dateTime.getTime()) / 1000;
        }

        return 0L;
    }
}
