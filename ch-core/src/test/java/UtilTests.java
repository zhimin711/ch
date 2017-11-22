import com.ch.pojo.KeyValue;
import com.ch.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：PACKAGE_NAME
 *
 * @author 80002023
 *         2017/3/8.
 * @version 1.0
 * @since 1.8
 */
public class UtilTests {

    Object o;

    @Test
    public void testCommon() {
        String a = "20a";
        String b = "20b";
        System.out.println(CommonUtils.compareTo(a, b));
        System.out.println(CommonUtils.isEquals(a, b));
        System.out.println(CommonUtils.isEquals(20.0000000000022, 20.0000000000022));
        System.out.println(CommonUtils.isEquals(20L, 20));
        o = DateUtils.currentTime();
        System.out.println(CommonUtils.isEquals(o, o));
        o = CommonUtils.isNotEmpty(Lists.newArrayList("1"));
        System.out.println(o);
    }

    @Test
    public void testDate() {
        o = DateUtils.getFirstDayOfMouth(DateUtils.currentTime());
        System.out.println(o);
        o = DateUtils.parse("2018-01-02 00:00:00");
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
        System.out.println(JsonUtils.toJson(o));
        System.out.println(new JSONObject(o).toString());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(o));
//        KeyValue tmp = mapper.readValue("{\"expires\":\"2017-08-15\",\"value\":\"[{\"b\":\"b\"}]\",\"key\":\"a\"}", KeyValue.class);
        JSONArray data = new JSONArray("[]");
        System.out.println(data.toString(1));


    }


}
