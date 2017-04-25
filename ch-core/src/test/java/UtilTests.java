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

    Object o;

    @Test
    public void testCommon() {
        System.out.println(CommonUtils.isEquals(20, 20));
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
        System.out.println(o);
    }


    @Test
    public void testFile() {
        o = FileUtils.convertSize(22209L);
        System.out.println(o);
    }


}
