package com.ch.utils;

import com.ch.e.PubError;
import com.ch.type.DateRule;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 时间格式
     */
    public enum Pattern {
        /**
         * 年
         */
        YEAR("yyyy"),
        /**
         * 月
         */
        MONTH("MM"),
        /**
         * 日
         */
        DAY("dd"),
        /**
         * 小时
         */
        HOUR("HH"),
        /**
         * 分钟
         */
        MINUTE("mm"),
        /**
         * 秒
         */
        SECOND("ss"), //
        /**
         * 中文：年/月/日
         */
        DATE_CN_ZH("yyyy年MM月dd日"), //
        /**
         * 中文：年/月/日
         */
        DATE_CN_ZH2("yyyy\u5E74M\u6708d\u65E5"), //
        /**
         * 中国：年-月-日
         */
        DATE_CN("yyyy-MM-dd"), //
        /**
         * 国际：年/月/日
         */
        DATE_EN("yyyy/MM/dd"), //
        /**
         * 年月日
         */
        DATE_SHORT("yyyyMMdd"), //
        /**
         * 小时:分钟:秒
         */
        TIME_FULL("HH:mm:ss"), //
        /**
         * 小时分钟秒
         */
        TIME_SHORT("HHmmss"), //
        /**
         * 小时:分钟
         */
        TIME_HM("HH:mm"), //
        /**
         * 年-月-日 小时:分钟
         */
        DATETIME_YMDHM_CN("yyyy-MM-dd HH:mm"), //
        /**
         * 年-月
         */
        DATE_MONTH_SHORT("yyyyMM"), //
        /**
         * 年-月-日 小时
         */
        DATE_HOUR_SHORT("yyyyMMddHH"), //
        /**
         * 年月日小时分钟秒
         */
        DATETIME_SHORT("yyyyMMddHHmmss"), //
        /**
         * 年-月-日 小时:分钟:秒(yyyy-MM-dd HH:mm:ss)
         */
        DATETIME_CN("yyyy-MM-dd HH:mm:ss"), //
        /**
         * 年-月-日 小时:分钟:秒.毫秒(yyyy-MM-dd HH:mm:ss.SSS)
         */
        DATETIME_FULL("yyyy-MM-dd HH:mm:ss.SSS"),
        /**
         * 年月日小时分钟秒.毫秒(yyyyMMddHHmmssSSS)
         */
        DATETIME_FULL_SHORT("yyyyMMddHHmmssSSS"),
        /**
         * 月-日 小时:分钟:秒.毫秒(MM-dd HH:mm:ss.SSS)
         */
        DATETIME_MDHMSS("MM-dd HH:mm:ss.SSS"), //
        /**
         * 年-月-日 小时:分钟:秒
         */
        DATETIME_CN_ZH("yyyy年MM月dd日 HH时mm分ss秒"), //
        /**
         * 年/月/日 小时:分钟:秒
         */
        DATETIME_EN("yyyy/MM/dd HH:mm:ss"),
        /**
         * UTC:年-月-日'T'小时:分钟:秒'Z'
         */
        DATETIME_UTC("yyyy-MM-dd'T'HH:mm:ss'Z'");

        private final String value;

        Pattern(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private DateUtils() {
    }

    /**
     * 创建一个格式化时间实例
     *
     * @param pattern 时间格式
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat newFormatInstance(Pattern pattern) {
        return new SimpleDateFormat(pattern.getValue());
    }

    /**
     * 创建一个格式化日期（yyyy-MM-dd）实例
     *
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat newFormatDateInstance() {
        return newFormatInstance(Pattern.DATE_CN);
    }

    /**
     * 创建一个格式化时间（yyyy-MM-dd HH:mm:ss）实例
     *
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat newFormatDateTimeInstance() {
        return newFormatInstance(Pattern.DATETIME_CN);
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
        return newFormatInstance(pattern).format(date);
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

    public static Date parseTimestamp(String timestamp) {
        if (!CommonUtils.isNumeric(timestamp)) {
            return null;
        }
        return parseTimestamp(new Long(timestamp));
    }

    /**
     * 解析时间截
     *
     * @param timestamp
     * @return
     */
    public static Date parseTimestamp(Long timestamp) {
        if (CommonUtils.isEmpty(timestamp)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }


    /**
     * 根据时间格式解析时间字符串
     *
     * @param dateStr 时间字符串
     * @param pattern 时间解析格式
     * @return 时间
     */
    public static Date parse(String dateStr, Pattern pattern) {
        if (CommonUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return newFormatInstance(pattern).parse(dateStr);
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
    public static Date current() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得系统当前日期时间
     *
     * @return 返回当前日期时间
     */
    public static Date min() {
        return parse("1970-01-01", Pattern.DATE_CN);
    }

    /**
     * 获得系统当前日期时间
     *
     * @return 返回当前日期时间
     */
    public static Date max() {
        Date date = parse("9999-12-31", Pattern.DATE_CN);
        return endDayTime(date);
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
        Date dateTime = current();
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
        calendar.add(Calendar.DAY_OF_YEAR, -1 * offset);
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
        return getWeekInYear(current());
    }


    /**
     * 得到当前日期是一年中的第几个星期
     *
     * @return String
     */
    public static String getWeekInYear(Date date) {
        return (new SimpleDateFormat("ww")).format(date);
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
     * @param date1 开始日期
     * @param date2 结束日期
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

    /**
     * (1)能匹配的年月日类型有：
     * 2014年4月19日
     * 2014年4月19号
     * 2014-4-19
     * 2014/4/19
     * 2014.4.19
     * (2)能匹配的时分秒类型有：
     * 15:28:21
     * 15:28
     * 5:28 pm
     * 15点28分21秒
     * 15点28分
     * 15点
     * (3)能匹配的年月日时分秒类型有：
     * (1)和(2)的任意组合，二者中间可有任意多个空格
     * 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略
     *
     * @param dateStr 时间
     * @return
     */
    public static String matchDateString(String dateStr) {
        return matchDateString(dateStr, Pattern.DATETIME_CN);
    }


    public static String matchDateString(String dateStr, Pattern pattern) {
        try {
            List<String> matches = Lists.newArrayList();
            //(\d{1,4}[-|/|年|.]\d{1,2}[-|/|月|.]\d{1,2}([日|号])?(\s)*(\d{1,2}([点|时])?((:)?\d{1,2}(分)?((:)?\d{1,2}(秒)?)?)?)?(\s)*(PM|AM)?)
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(patternToRegex(pattern),
                    java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE);
            Matcher matcher = p.matcher(dateStr);
            if (matcher.find() && matcher.groupCount() >= 1) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String temp = matcher.group(i);
                    matches.add(temp);
                }
            }
            if (matches.size() > 0) {
                return matches.get(0).trim();
            }
        } catch (Exception e) {
            logger.error("matchDateString Error!", e);
        }

        return dateStr;
    }

    public static String patternToRegex(Pattern pattern) {
        String regex = "(\\d{1,4}[-|/|年|.]\\d{1,2}[-|/|月|.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)";
//        String ymdStdRegex = "\\d{1,4}[-|/|年|.]\\d{1,2}[-|/|月|.]\\d{1,2}([日|号])";
        String ymdAiRegex = "((\\d{1,4}[-|/|年|.]?)\\d{1,2}[-|/|月|.]\\d{1,2}([日|号])?)";
        String hmsRegex = "(\\d{1,2}[:|时|点]\\d{1,2}(分)?((:)?\\d{1,2}(秒)?((.)?\\d{1,3})?)?)";
//        String ap = "(\\s)*(PM|AM)?";
        switch (pattern) {
            case DATE_EN:
            case DATE_CN:
            case DATE_CN_ZH:
            case DATE_CN_ZH2:
                regex = ymdAiRegex;
                break;
            case DATETIME_FULL:
                break;
            case TIME_FULL:
            case TIME_HM:
                regex = hmsRegex;
                break;
            case DATETIME_CN:
            case DATETIME_YMDHM_CN:
            case DATETIME_CN_ZH:
            case DATETIME_EN:
            case DATETIME_MDHMSS:
                regex = "(" + ymdAiRegex + "(\\s)" + hmsRegex + ")";
                break;
            default:
                break;
        }
        return regex;
    }

    /**
     * 获取指定日期开始日间 0点0分0秒0毫秒
     *
     * @param date 指定日期
     * @return
     */
    public static Date startDayTime(Date date) {
        assert date != null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定日期结束日间 23点59分59秒999毫秒
     *
     * @param date 指定日期
     * @return
     */
    public static Date endDayTime(Date date) {
        assert date != null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }


    /**
     * 清除指定日期毫秒
     *
     * @param date 指定日期
     * @return
     */
    public static Date clearMillisecond(Date date) {
        assert date != null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    protected static final String[] WEEK_CN = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    protected static final String[] QUARTERLY_CN = {"春季", "夏季", "秋季", "冬季"};

    /**
     * 获日期取周中文名称
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return WEEK_CN[week_index];
    }

    /**
     * 获工作是取周中文名称
     *
     * @param week
     * @return
     */
    public static String getWeek(final Integer week) {
        int index_week = 0;
        if (week != null && week >= 0 && week <= 6) {
            index_week = week;
        }
        return WEEK_CN[index_week];
    }


    public static Integer getOfType(Date date, int type) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int t = Calendar.YEAR;
        switch (type) {
            case Calendar.MONTH:
                t = Calendar.MONTH;
                return c.get(t) + 1;
            case Calendar.DATE:
                t = Calendar.DATE;
                break;
            case Calendar.HOUR:
                t = Calendar.HOUR;
                break;
            case Calendar.MINUTE:
                t = Calendar.MINUTE;
                break;
            case Calendar.SECOND:
                t = Calendar.SECOND;
                break;
            case Calendar.DAY_OF_WEEK:
                t = Calendar.DAY_OF_WEEK;
                break;
            case Calendar.DAY_OF_WEEK_IN_MONTH:
                t = Calendar.DAY_OF_WEEK_IN_MONTH;
                break;
            default:
                break;
        }
        return c.get(t);
    }

    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间的工作日数
     *
     * @param start    开始日期
     * @param end      结束日期
     * @param workdays 适用工作日（1周日2周一3周二4周三5周四6周五7周六）
     * @return 工作日数
     */
    public static int workdays(Date start, Date end, Set<Integer> workdays) {
        if (workdays == null) return 0;
        StringBuilder str = new StringBuilder();
        for (Integer workday : workdays) {
            str.append(workday.toString());
        }
        return workdays(start, end, str.toString());
    }

    /**
     * 计算两个日期之间的工作日数
     *
     * @param start   开始日期
     * @param end     结束日期
     * @param workday 适用工作日（1周日2周一3周二4周三5周四6周五7周六）
     * @return 工作日数
     */
    public static int workdays(Date start, Date end, String workday) {
        return workDate(start, end, workday).size();
    }

    /**
     * 1234567转中文
     *
     * @param workday   适用工作日
     * @param separator 分割符
     * @return
     */
    public static String convertWorkdays(String workday, String separator) {
        if (CommonUtils.isEmpty(workday)) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < workday.length(); i++) {
            if (sb.length() > 0)
                sb.append(separator);
            String day = String.valueOf(workday.charAt(i));
            if (CommonUtils.isNumeric(day)) {
                sb.append(DateUtils.getWeek(Integer.valueOf(day)));
            }
        }
        return sb.toString();
    }

    /**
     * 计算两个日期之间的工作日期
     *
     * @param start   开始日期
     * @param end     结束日期
     * @param workday 适用工作日（1周日2周一3周二4周三5周四6周五7周六）
     * @return 工作日数
     */
    public static List<Date> workDate(Date start, Date end, String workday) {
        if (start == null || end == null) {
            throw ExceptionUtils.create(PubError.ARGS);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        List<Date> dateList = new ArrayList<>();
        while (cal.getTime().before(addDays(end, 1))) {
            int week = getOfType(cal.getTime(), Calendar.DAY_OF_WEEK) - 1;
            if (week < 1) {
                week = 7;
            }
            if (workday.contains(Integer.toString(week))) {
                dateList.add(cal.getTime());
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }


    /**
     * 根据时间规则计算下一下日期
     *
     * @param type 规划
     * @param date 计划日期
     * @param n    偏移量
     * @return 日期
     */
    public static Date calc(DateRule type, final Date date, int n) {
        Date nextDate;
        if (type == DateRule.DAY) {
            nextDate = DateUtils.addDays(date, n);
        } else if (type == DateRule.WEEK) {
            nextDate = DateUtils.addWeeks(date, n);
        } else if (type == DateRule.MONTH) {
            nextDate = DateUtils.addMonths(date, n);
        } else if (type == DateRule.QUARTERLY) {
            nextDate = DateUtils.addMonths(date, n * 3);
        } else if (type == DateRule.YEAR) {
            nextDate = DateUtils.addYears(date, n);
        } else {
            throw new RuntimeException("未知规则无法计算!");
        }
        return nextDate;
    }

    /**
     * 根据时间规则计算下一下日期
     *
     * @param type 规划
     * @param date 计划日期
     * @return 日期
     */
    public static String format(DateRule type, final Date date) {
        String str;
        if (type == DateRule.DAY) {
            str = DateUtils.format(date, Pattern.DATE_SHORT);
        } else if (type == DateRule.WEEK) {
            str = DateUtils.format(date, Pattern.YEAR) + getWeekInYear(date);
        } else if (type == DateRule.MONTH) {
            str = DateUtils.format(date, Pattern.DATE_MONTH_SHORT);
        } else if (type == DateRule.QUARTERLY) {
            str = DateUtils.format(date, Pattern.YEAR) + getQuarterly(date);
        } else if (type == DateRule.YEAR) {
            str = DateUtils.format(date, Pattern.YEAR);
        } else {
            throw new RuntimeException("未知规则无法计算!");
        }
        return str;
    }


    /**
     * 当前季度号
     *
     * @return
     */
    public static String getQuarterly(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int i = 0;
        if (currentMonth >= 1 && currentMonth <= 3) {
            i = 1;
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            i = 2;
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            i = 3;
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            i = 4;
        }
        return String.format("%2d", i);
    }
}
