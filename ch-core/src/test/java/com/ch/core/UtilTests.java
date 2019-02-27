package com.ch.core;

import com.ch.pojo.KeyValue;
import com.ch.utils.*;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：PACKAGE_NAME
 *
 * @author 80002023
 * 2017/3/8.
 * @version 1.0
 * @since 1.8
 */
public class UtilTests {

    final Logger logger = LoggerFactory.getLogger(UtilTests.class);

    Object o;

    @Test
    public void testCommon() {
        logger.info("==========");
        String a = "20a";
        String b = "20b";
        System.out.println(CommonUtils.compareTo(a, b));
        System.out.println(CommonUtils.isEquals(a, b));
        System.out.println(CommonUtils.isEquals(20.0000000000022, 20.0000000000022));
        logger.info("{}", CommonUtils.isEquals(20L, 20));
        o = DateUtils.currentTime();
        logger.info("{}", CommonUtils.isEquals(o, o));
        o = CommonUtils.isNotEmpty(Lists.newArrayList("1"));
        logger.info("{}", o);
        for (int i = 0; i < 10; i++) {
            logger.info(EncryptUtils.md5(b));
        }
    }

    @Test
    public void testDate() {
        o = DateUtils.getFirstDayOfMouth(DateUtils.currentTime());
        System.out.println(o);
        Date date = DateUtils.parse("2018-01-02 00:10:21");
        o = DateUtils.format(DateUtils.parseTimestamp(String.valueOf(date.getTime())));
        System.out.println(o);
        o = DateUtils.matchDateString("#2017-11-17 10:35:10.487|INFO |");
        o = DateUtils.matchDateString("11-17 10:35:10.487|INFO |");
        System.out.println(o);

        String pattern = "(\\d{1,2}[:|时|点]\\d{1,2}(分)?((:)?\\d{1,2}(秒)?((.)?\\d{1,3})?)?)";
//        pattern = "((\\d{1,4}[-|/|年|.]?)\\d{1,2}[-|/|月|.]\\d{1,2}([日|号])?)";
        List<String> matches = Lists.newArrayList();
        Matcher matcher = Pattern.compile(DateUtils.patternToRegex(DateUtils.Pattern.DATETIME_MDHMSS)).matcher("#2017-11-17 10:35:10.487|INFO |\n");
        if (matcher.find() && matcher.groupCount() >= 1) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String temp = matcher.group(i);
                matches.add(temp);
            }
        }
        if (matches.size() > 0) {
            o = matches.get(0).trim();
        }
        System.out.println(o);

    }


    @Test
    public void testFile() {
        o = FileUtils.convertSize(22209L);
        System.out.println(o);
        Date currTime = DateUtils.currentTime();
        String dateStr = DateUtils.format(currTime, DateUtils.Pattern.DATE_SHORT);
        String uuid = UUIDGenerator.generate();
        File file = new File("D:\\opt\\" + dateStr + File.separator + uuid + "\\demo.txt");
        System.out.println(file.getParent());
        boolean ok = FileUtils.create(file);
        System.out.println(ok);
    }

    @Test
    public void testJson() throws Exception {
        o = new KeyValue("a", "b", DateUtils.currentTime());
        System.out.println(JSONUtils.toJson(o));
//        System.out.println(new JSONObject(o).toString());
//        KeyValue tmp = mapper.readValue("{\"expires\":\"2017-08-15\",\"value\":\"[{\"b\":\"b\"}]\",\"key\":\"a\"}", KeyValue.class);
//        JSONArray data = new JSONArray("[]");
//        System.out.println(data.toString(1));
        KeyValue[] arr = JSONUtils.fromJson("[]", KeyValue[].class);
    }

    @Test
    public void testLogUtils() {
        String str = "#2017-11-22 15:42:07.611 [main] INFO  o.s.web.context.ContextLoader -Root WebApplicationContext: initialization completed in 1240 ms\t\n";
        String tmp = "2017-11-17 10:35:10.487|DEBUG|qtp1334042472-3152| com.sf.novatar.deploy.interceptor.ModuleInterceptor.intercept(ModuleInterceptor.java:70)|Response /contract/businessContractManageSF/findTaskInfosByContractLine.pvt in 17 ms.";

        o = LogUtils.isLogFormat1(tmp);
        logger.info("{}", o);
        Pattern p = Pattern.compile(LogUtils.DATE_PATTERN + "(\\s)" + LogUtils.TIME_PATTERN + "(\\s)" + LogUtils.THREAD_PATTERN);
        o = p.matcher(str).find();
        logger.info("{}", o);
    }

    @Test
    public void testSQL() {
        String sql = "select * # af\n" +
                "-- a\n" +
                "from ts_role limit  11";

        logger.info("after trim comment: {}", SQLUtils.trimComment(sql));
        logger.info("sql has limit: {}", SQLUtils.hasLimit(sql));

        sql = StringUtils.formatSort(1, 3);
        logger.info("formatSort: {}", sql);
    }

    @Test
    public void testBean() {
        KeyValue kv = new KeyValue("a", "a1");
        try {
//            Method m = kv.getClass().getMethod("getValue", String.class);
//            logger.info("{}:{}", kv.getValue(), String.valueOf(m.invoke(kv, "a2")));

            logger.info("{}:{}", PlatformUtils.getHostName(), PlatformUtils.getCanonicalHostName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEncrypt() {
        String str = "L21udC90bXBcMjAxOC0wOC0wOFw4YWY2YmVjZDY1MTgxOWU0MDE2NTE4MWQwNGYxMDAwMS56aXA=";
        String pwd = EncryptUtils.generate(16);//"abcdefgabcdefg12"
        String iv = EncryptUtils.generate(16);
//        String s1 = EncryptUtils.encryptDES(str, pwd);
//        String s2 = EncryptUtils.encryptAES(str, pwd);
        String s3 = EncryptUtils.encryptAESCBC(str, pwd, iv);

        logger.info("PlanText   : {}", str);
        logger.info("pwd        : {}", pwd);
        logger.info("iv         : {}", iv);
//        logger.info("DES encrypt: {}", s1);
//        logger.info("DES decrypt: {}", EncryptUtils.decryptDES(s1, pwd));
//        logger.info("AES encrypt: {}", s2);
//        logger.info("AES decrypt: {}", EncryptUtils.decryptAES(s2, pwd));
        logger.info("AES CBC encrypt: {}", s3);
        logger.info("AES CBC decrypt: {}", EncryptUtils.decryptAESCBC(s3, pwd, iv));
//        logger.info("AES decodeBase64: {}", EncryptUtils.decodeBase64ToString(EncryptUtils.decryptAES(s2, pwd)));
    }

}
