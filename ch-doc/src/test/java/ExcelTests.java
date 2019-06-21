import com.ch.doc.poi.ColumnDefine;
import com.ch.doc.poi.ExcelExport;
import com.ch.doc.poi.ExcelImport;
import com.ch.doc.poi.RecordDefine;
import com.ch.utils.UUIDGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

        ExcelExport excelExport = new ExcelExport(templateFile, 1);
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

    @Test
    public void read() throws Exception {
        File templateFile = new File("D:\\nfsc\\副本（历史数据刷数）系统任务所属网点修正.xlsx");

        RecordDefine recordDefine = new RecordDefine();
        recordDefine.addColumn(new ColumnDefine());
        recordDefine.addColumn(new ColumnDefine());
        recordDefine.addColumn(new ColumnDefine());
        recordDefine.addColumn(new ColumnDefine());
        ExcelImport excelImport = new ExcelImport(recordDefine, 10000);
        List<?> rows = excelImport.read(templateFile, HashMap.class, 1, 0);
        rows.forEach(r -> {
            Map obj = (Map) r;
            StringBuilder sb = new StringBuilder();
            sb.append("insert into `temp_plan_require_task_dept_0620` values(");
            sb.append(obj.get(0).toString());
            sb.append(",").append(obj.get(1).toString());
            sb.append(",").append("'").append(obj.get(2).toString()).append("'");
            sb.append(",").append("'").append(obj.get(3).toString()).append("'");
            sb.append(");");
            System.out.println(sb.toString());
        });
    }
}
