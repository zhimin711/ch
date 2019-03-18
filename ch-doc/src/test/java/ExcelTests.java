import com.ch.doc.poi.POIExcelExport;
import com.ch.utils.UUIDGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * ExcelTests
 * Created by 01370603 on 2017/11/29.
 */
public class ExcelTests {

    @Test
    public void test() throws Exception {
//        ExcelUtils.create(ExcelUtils.ExcelType.X);
        List<String> h1 = Lists.newArrayList("h1", "h2", "h3");
        List<String> h2 = Lists.newArrayList("h3", "h4", "h5");
        File templateFile = new File("D:\\nfsc\\temp.xlsx");
        File excelFile = new File("D:\\nfsc\\" + UUIDGenerator.generate() + ".xlsx");

        POIExcelExport excelExport = new POIExcelExport(templateFile, 1);
        Map<String, Object> objectMap = Maps.newHashMap();
        h1.forEach(r -> objectMap.put(r, "v1-" + r));
        excelExport.setSheetAt(0);
        excelExport.writeRecords(Lists.newArrayList(objectMap), h1, false);

        Map<String, Object> objectMap1 = Maps.newHashMap();
        h2.forEach(r -> objectMap1.put(r, "v2-" + r));
        excelExport.setSheetAt(1);
        excelExport.writeRecords(Lists.newArrayList(objectMap1), h2, false);
        excelExport.write(excelFile);
    }
}
