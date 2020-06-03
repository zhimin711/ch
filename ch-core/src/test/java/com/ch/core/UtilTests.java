package com.ch.core;

import com.ch.pojo.KeyValue;
import com.ch.pojo.VueRecord;
import com.ch.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        logger.info("1.{}", CommonUtils.compareTo(a, b));
        logger.info("1.{}", CommonUtils.isEquals(a, b));
        logger.info("1.{}", CommonUtils.isEquals(20.0000000000022, 20.0000000000022));
        logger.info("1.{}", CommonUtils.isEquals(20L, 20));
        logger.info("6. {}", CommonUtils.isEquals(new BigDecimal("2.7"), new BigDecimal(2.700f)));
        o = DateUtils.current();
        logger.info("2.{}", CommonUtils.isEquals(o, o));
        o = CommonUtils.isNotEmpty(Lists.newArrayList("1"));
        logger.info("3.{}", o);
        for (int i = 0; i < 10; i++) {
            logger.info(EncryptUtils.md5(b));
        }
        logger.info("4.{}", CommonUtils.isNotEmptyNull(null, "nul"));
    }

    @Test
    public void testDate() {
        o = DateUtils.getFirstDayOfMonth(DateUtils.current());
        System.out.println(o);
        Date date = DateUtils.parse("2018-01-02 00:10:21");
//        o = DateUtils.format(DateUtils.parseTimestamp(String.valueOf(date.getTime())));
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
    public void testFile() throws MalformedURLException, ClassNotFoundException {
        o = FileExtUtils.convertSize(22209L);
        System.out.println(o);
        Date currTime = DateUtils.current();
        String dateStr = DateUtils.format(currTime, DateUtils.Pattern.DATE_SHORT);
        String uuid = UUIDGenerator.generate();
//        File file = new File("D:\\opt\\" + dateStr + File.separator + uuid + "\\demo.txt");
//        System.out.println(file.getParent());
//        boolean ok = FileExtUtils.create(file);
//        System.out.println(ok);
//        Class<?> clazz = JarUtils.loadClassForJar("file:D:\\mnt\\share\\common\\libs\\ground-rs-api-6.6-SNAPSHOT.jar", "com.sf.shiva.trtms.ground.rs.pojo.temp.TemRequireVO");
//        Class<?> clazz2 = JarUtils.reloadClassForJar("file:D:\\mnt\\share\\common\\libs\\ground-rs-api-6.6-SNAPSHOT.jar", "com.sf.shiva.trtms.ground.rs.pojo.temp.TemRequireVO");
//        Class<?> clazz3 = JarUtils.loadClassForJar("file:D:\\mnt\\share\\common\\libs\\ground-rs-api-6.6-SNAPSHOT.jar", "com.sf.shiva.trtms.ground.rs.pojo.temp.TemRequireVO");
    }

    @Test
    public void testJson() throws Exception {
        o = new KeyValue("a", "b", DateUtils.current());
        System.out.println(JSONUtils.toJson2(o));
//        System.out.println(new JSONObject(o).toString());
//        KeyValue tmp = mapper.readValue("{\"expires\":\"2017-08-15\",\"value\":\"[{\"b\":\"b\"}]\",\"key\":\"a\"}", KeyValue.class);
//        JSONArray data = new JSONArray("[]");
//        System.out.println(data.toString(1));
        o = JSONUtils.fromJson2(JSONUtils.toJson2(o), KeyValue.class);
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
        String sql = "select * \n# af\n" +
                "-- a\n" +
                "from ts_role limit  11";

        logger.info("after trim comment: {}", SQLUtils.trimComment(sql));
        logger.info("after trim simple comment: {}", SQLUtils.trimSimpleComment(sql));
        logger.info("sql has limit: {}", SQLUtils.hasLimit(sql));

        sql = StringUtils.formatSort(1, 3);
        logger.info("formatSort: {}", sql);
    }

    @Test
    public void testBean() {
        KeyValue kv = new KeyValue("a", "a1");
        KeyValue kv2 = new KeyValue("a", "b1");
        VueRecord vr = new VueRecord("b","c");
//        kv.setValue(null);
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("key", "b");
            map.put("value", "b1");
            BeanExtUtils.setFieldValue(kv, map, true);
            logger.info("{}:{}", kv.getKey(), kv.getValue());

            logger.info("compareFields: {}", BeanExtUtils.compareFields(kv, kv2));
            logger.info("compareSameFields: {}", BeanExtUtils.compareSameFields(kv, vr,"name"));

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

    @Test
    public void testResult() {
        Number num1 = 0.000000000000000000f;
        Number num2 = 0;
        logger.info("{} == {} -> {}", num1.floatValue(), num2.floatValue(), num1.doubleValue() == num2.doubleValue());

        logger.info("data calc offset days: {}", DateUtils.calcOffsetDays("2019-12-05", "2019-12-02"));
        logger.info("data calc cross days: {}", DateUtils.calcCrossDays("2019-12-12", "2019-12-02"));
    }

    @Test
    public void testNetUtils() {

        String url = "http://sfecp.sit.sf-express.com/ecp/";

        logger.info("url protocol correct: {} \n base: {} \n suffix: {}", NetUtils.isProtocolURL(url), NetUtils.parseBaseUrl(url), NetUtils.parseSuffixUrl(url));

        url = "http://www.r5k.com/books/z2_81_81780/984118/";
        System.out.println(NetUtils.trim(FileExtUtils.convertToUnix(url)));

        System.out.println(FileExtUtils.convertToUnix(url));

    }

    @Test
    public void testString() {
//        logger.info(Arrays.toString(StringExtUtils.parseIds("1,2")));

        logger.info(StringExtUtils.toIdStr(new Long[]{111L, 222L}));
        logger.info(StringExtUtils.toIdStr(new Integer[]{1, 2}));
        logger.info(StringExtUtils.toIdStr(new Float[]{1.1f, 22.01f}));

        logger.info(DigitUtils.num2Chinese(1211020001));

        logger.info("1: {}", DigitUtils.chinese2Num("十二"));
        logger.info("2: {}", DigitUtils.chinese2Num("十二万零一百零二"));
        logger.info("3: {}", DigitUtils.chinese2Num("一百二十一万一千零二十"));
        logger.info("4: {}", DigitUtils.chinese2Num("一百二十一万一千零二十"));
        logger.info("5: {}", DigitUtils.chinese2Num("十二亿一千一百零二万零一"));


        logger.info("6: {}", new BigDecimal(2.7).toPlainString());
        logger.info("6: {}", new BigDecimal(2.7000f).toPlainString());
        logger.info("6: {}", DigitUtils.fixed(new BigDecimal(2.7000f)));

    }
}
