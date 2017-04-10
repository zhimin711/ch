import com.ch.utils.CommonUtils;
import com.ch.utils.DateUtils;
import com.ch.utils.FileUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

/**
 * 描述：PACKAGE_NAME
 *
 * @author 80002023
 *         2017/3/8.
 * @version 1.0
 * @since 1.8
 */
public class UtilTests {

    @Test
    public void testCommon() {
        o = CommonUtils.isNotEmpty(Lists.newArrayList("1"));
        System.out.println(o);
    }

    Object o;

    @Test
    public void testDate() {
        o = DateUtils.getFirstDayOfMouth(DateUtils.currentTime());
        System.out.println(o);
        o = DateUtils.parse("2018-01-02 00:00:00");
        System.out.println(o);
    }


    @Test
    public void testFile() {
        o = FileUtils.convertSize(22209L);
        System.out.println(o);
    }


}
