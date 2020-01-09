package com.ch.core;

import com.ch.utils.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DateUtilsTests {

    Logger logger = LoggerFactory.getLogger(DateUtilsTests.class);

    @Test
    public void testWeek() {
        logger.info(DateUtils.convertWorkdays("13", ","));

        long a = 190114101828425L;
        long b = 82019010800000004L;

        logger.info("{}-{}-{}", a / 1000000000, Long.toString(a).length(), "20" + a / 1000000000);
        logger.info("{}-{}-{}", b / 100000000, Long.toString(b).length(), Long.toString(b / 100000000).substring(1));

        logger.info("{}", DateUtils.format(DateUtils.parse("20" + a / 1000000000, DateUtils.Pattern.DATE_SHORT)));
        logger.info("{}", DateUtils.format(DateUtils.parse(Long.toString(b / 100000000).substring(1), DateUtils.Pattern.DATE_SHORT)));

    }

    @Test
    public void testMonth() {
        Date d1 = DateUtils.parse("2020-02-01", DateUtils.Pattern.DATE_CN);
        Date d2 = DateUtils.parse("2020-02-29 12:00:00");

        logger.info("{} ~ {}", DateUtils.format(DateUtils.getFirstDayOfMouth(d1)), DateUtils.format(DateUtils.getLastDayOfMouth(d1)));

        logger.info("isFirstDay:{} - isLastDay:{}", DateUtils.isFirstDay(d2), DateUtils.isLastDay(d2));

        logger.info("FullMonths: {}", DateUtils.getFullMonths(d2, d1));

        logger.info("Month first and last date: {}", DateUtils.getMonthFirstAndLastDay(d1, 12));


        logger.info("Month first and last date: {}", DateUtils.getMonthFirstAndLastDay(2019, 12));
    }
}
