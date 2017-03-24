import com.ch.utils.CommonUtils;
import com.ch.utils.DateUtils;
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
        System.out.println(CommonUtils.isNotEmpty(Lists.newArrayList("1")));
    }


    @Test
    public void testDate() {
        System.out.println(DateUtils.parse("2018-01-02 11:22:33", DateUtils.TIME_CN));
    }


}
