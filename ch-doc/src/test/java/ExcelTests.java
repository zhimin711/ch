import com.ch.doc.utils.ExcelUtils;
import org.junit.Test;

/**
 * ExcelTests
 * Created by 01370603 on 2017/11/29.
 */
public class ExcelTests {

    @Test
    public void test() throws Exception {
        ExcelUtils.create(ExcelUtils.ExcelType.X);
    }
}
