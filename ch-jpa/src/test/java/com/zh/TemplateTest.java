package com.zh;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.List;
import java.util.Map;

/**
 * decs:
 *
 * @author 01370603
 * @date 2019/6/27
 */
public class TemplateTest {

    private Long[] ids = new Long[]{74036698L, 74036707L, 74990445L, 74990453L, 74990461L, 74990472L, 74990485L, 67767310L, 67767172L, 68461051L, 67767308L, 74037297L, 73989443L, 73989644L, 74990582L, 74990612L, 74990564L, 74990570L, 74990596L, 74990767L, 74990878L, 74990720L, 74990735L, 74990803L, 74036791L, 74036797L, 74036806L, 74036816L, 74036783L, 73316000L, 73316037L, 73316065L, 73316010L, 73316044L, 73316229L, 74990404L, 74990410L, 74990418L, 74990425L, 73655975L, 73655999L, 73655967L, 73655991L, 70679636L, 73988778L, 73988794L, 67767396L, 73988739L, 73656149L, 73656189L, 75947018L, 74037190L, 74990261L, 74990279L, 74140522L, 71955842L, 74140942L, 73306889L, 74990519L, 75947007L, 75944543L, 76340776L, 71955802L, 74140900L, 74992663L, 68899658L, 74008439L, 74008426L, 72978148L, 72978161L, 72978167L, 72978167L, 72978120L, 72978131L, 72978140L, 73035875L, 73035891L, 73035900L, 73035775L, 73035784L, 73035793L, 73035848L, 73036654L, 76688778L, 74992582L, 74992598L, 74992590L, 74992614L, 74991788L, 74991867L, 74991857L, 72714510L, 76688792L, 70935073L, 70935128L, 70935104L, 70935166L, 73199645L, 73199647L, 73199456L, 73199464L};

    @Test
    public void test() {

        //创建数据源
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://10.202.11.117:3306/test1");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
//        Driver driver = new
//        DataSource ds = new SingleConnectionDataSource("jdbc:mysql://10.202.11.117:3306/test1", "root", "sf123456", true);


        JdbcTemplate template = new JdbcTemplate(dataSource);

        List<String> list = template.queryForList("SELECT content from tl_kafka_resend", String.class);

        System.out.println(list.size());
        List<Long> taskIds = Lists.newArrayList();
        list.forEach(r -> {
            JSONObject obj = JSONObject.parseObject(r);
            Long lineId = obj.getJSONObject("data").getLong("lineId");
            Long taskId = obj.getJSONObject("data").getLong("taskId");
            Long requireId = obj.getJSONObject("data").getLong("requireId");
            if (Lists.newArrayList(ids).contains(lineId)) {
                System.out.println(lineId + "\t" + requireId + "\t" + taskId);
                taskIds.add(taskId);
            }
        });
        System.out.println(taskIds.size());
    }
}
