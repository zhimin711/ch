package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

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

    public enum Pattern {
        YEAR("yyyy"), //
        MONTH("MM"), //
        DAY("dd"), //
        HOUR("HH"), //
        MINUTE("mm"), //
        SECOND("ss"), //
        DATE_CN_ZH("yyyy年MM月dd日"), //
        DATE_CN_ZH2("yyyy\u5E74M\u6708d\u65E5"), //
        DATE_CN("yyyy-MM-dd"), //
        DATE_EN("yyyy/MM/dd"), //
        DATE_SHORT("yyyyMMdd"), //
        TIME_FULL("HH:mm:ss"), //
        TIME_SHORT("HHmmss"), //
        TIME_HM("HH:mm"), //
        DATETIME_YMDHM_CN("yyyy-MM-dd HH:mm"), //
        DATETIME_SHORT("yyyyMMddHHmmss"), //
        DATETIME_CN("yyyy-MM-dd HH:mm:ss"), //
        DATETIME_CN_ZH("yyyy年MM月dd日 HH时mm分ss秒"), //
        DATETIME_EN("yyyy/MM/dd HH:mm:ss"),
        DATETIME_UTC("yyyy-MM-dd'T'HH:mm:ss'Z'");

        private final String value;

        private Pattern(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private DateUtils() {
    }

    /**
     * 格式化时间为字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String format(Date date) {
        if (null == date) {
            return null;
        }
        return format(date, Pattern.DATETIME_CN);
    }

    /**
     * 根据指定格式，格式化时间为字符串
     * 没有指定，默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param date    时间
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String format(Date date, Pattern pattern) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern.getValue());
        return sdf.format(date);
    }

    /**
     * 格式化时间为日期字符串（yyyy-MM-dd）
     *
     * @param date 时间
     * @return 日期字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, Pattern.DATE_CN);
    }

    /**
     * 格式化时间为中文日期字符串（yyyy年MM月dd日）
     *
     * @param date 时间
     * @return 日期字符串
     */
    public static String formatDateOfZH(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, Pattern.DATE_CN_ZH);
    }

    /**
     * 格式化时间为UTC时间字符串
     *
     * @param date 时间
     * @return UTC符串
     */
    public static String formatOfUTC(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, Pattern.DATETIME_UTC);
    }

    /**
     * 根据时间格式解析时间字符串
     *
     * @param dateStr 时间字符串
     * @param pattern 时间解析格式
     * @return 时间
     */
    public static Date parse(String dateStr, Pattern pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern.getValue());
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            logger.error("Parse date failed! date = " + dateStr + ", pattern = " + pattern, e);
        }
        return null;
    }

    /**
     * 解析时间字符串（格式：）
     *
     * @param dateStr 时间字符串
     * @return 时间
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, Pattern.DATETIME_CN);
    }

    /**
     * 获得系统当前日期时间
     *
     * @return 返回当前日期时间
     */
    public static Date currentTime() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 日期时间比较
     *
     * @param dateStart 开始时间
     * @param dateEnd   结束时间
     * @return -1 or 0 or 1
     */
    public static int compare(Date dateStart, Date dateEnd) {
        return dateStart.compareTo(dateEnd);
    }

    /**
     * 日期时间字符串比较
     *
     * @param pattern 时间格式
     * @param date1   时间字符串1
     * @param date2   时间字符串2
     * @return 返回 -1：小于，0：等于，1：大于
     */
    public static int compareTo(String date1, String date2, Pattern pattern) {
        if (Objects.isNull(pattern)) {
            pattern = Pattern.DATE_CN;
        }
        Date d1 = parse(date1, pattern);
        Date d2 = parse(date2, pattern);
        if (d1 == null && d2 == null) {
            return 0;
        } else if (d1 == null) {
            return -1;
        } else if (d2 == null) {
            return 1;
        }
        return d1.compareTo(d2);
    }

    /**
     * 转换过期时间为秒（相对当前时间）
     *
     * @param expires 过期时间
     * @return 秒
     */
    public static Long convertExpiresToSeconds(Date expires) {
        if (CommonUtils.isEmpty(expires)) {
            return 0L;
        }
        logger.info("convert expires date seconds! {}", expires);
        Date dateTime = currentTime();
        if (dateTime.before(expires)) {
            return (expires.getTime() - dateTime.getTime()) / 1000;
        }

        return 0L;
    }

    /**
     * 获取指定日期的月份的第一天
     *
     * @param date 日期
     * @return 第一天
     */
    public static Date getFirstDayOfMouth(Date date) {
        assert date != null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 得到指定日期的星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 日期 String
     */
    public static String getWeekday(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("E")).format(calendar.getTime());
    }

    /**
     * 得到指定日期前/后offset天的日期
     *
     * @param dateString 以"yyyy-MM-dd"格式指定的日期
     * @param offset     偏移量
     * @return String
     */
    public static String getDate(String dateString, int offset) {
        Calendar calendar = Calendar.getInstance();
        Date date = parse(dateString, Pattern.DATE_CN);
        assert date != null;
        calendar.setTime(date);
        calendar.add(6, -1 * offset);
        return format(calendar.getTime(), Pattern.DATE_CN);
    }

    /**
     * 得到当前日期是一年中的第几天
     *
     * @return String
     */
    public static String getDateInYear() {
        return (new SimpleDateFormat("DDD")).format(new Date());
    }

    /**
     * 得到当前日期是一年中的第几个星期
     *
     * @return String
     */
    public static String getWeekInYear() {
        return (new SimpleDateFormat("ww")).format(new Date());
    }


    /**
     * 得到指定日期是一年中的第几个星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 指定日期是一年中的第几个星期 String
     */
    public static String getWeekInYear(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("ww")).format(calendar.getTime());
    }


    /**
     * 得到指定日期是当前月的第几个星期
     *
     * @return String
     */
    public static String getWeekInMonth() {
        return (new SimpleDateFormat("WW")).format(new Date());
    }


    /**
     * 得到指定日期是所在月份的第几个星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 指定日期是所在月份的第几个星期 String
     */
    public static String getWeekInMonth(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("WW")).format(calendar.getTime());
    }

    /**
     * 得到当前日期前beforeNum天的日期
     *
     * @param beforeNum 提前量
     * @return String
     */
    public static String getDateByBefore(int beforeNum) {
        Calendar now = Calendar.getInstance();
        now.add(6, -1 * beforeNum);
        return format(now.getTime(), Pattern.DATE_CN);
    }

    /**
     * 得到指定日期前beforeNum天的日期
     *
     * @param year      指定的年份
     * @param month     指定的月份
     * @param day       指定的日期
     * @param beforeNum 提前量
     * @return String
     */
    public static String getDateByBefore(int year, int month, int day,
                                         int beforeNum) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, -1 * beforeNum);
        return format(calendar.getTime(), Pattern.DATE_CN);
    }

    /**
     * 获取2个日期相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getOffset(String date1, String date2) {

        Date d1 = parse(date1, Pattern.DATE_CN);
        Date d2 = parse(date2, Pattern.DATE_CN);

        assert d1 != null;
        assert d2 != null;
        long c = d2.getTime() - d1.getTime();
        return c / 1000 / 3600 / 24;
    }


}
