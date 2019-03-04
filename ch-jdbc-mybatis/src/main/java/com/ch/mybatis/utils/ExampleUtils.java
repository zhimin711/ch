package com.ch.mybatis.utils;

import com.ch.utils.BeanUtils;
import com.ch.utils.CommonUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * desc:通用查询工具
 *
 * @author zhimin
 * @date 2018/12/10 12:26 AM
 */
public class ExampleUtils {

    private static final String[] BASE = new String[]{"id", "pid", "type", "status"};
    private static final String[] LIKE = new String[]{"code", "name"};

    private ExampleUtils() {
    }

    public static int dynIn(Example.Criteria criteria, String property, List<?> values) {
        if (CommonUtils.isEmpty(values)) {
            return 0;
        } else {
            criteria.andIn(property, values);
        }
        return 1;
    }

    public static int dynInOrLike(Example.Criteria criteria, String property, List<String> values) {
        if (CommonUtils.isEmpty(values)) {
            return 0;
        } else if (values.size() == 1) {
            criteria.andLike(property, "%" + values.get(0) + "%");
        } else {
            criteria.andIn(property, values);
        }
        return 1;
    }


    public static int dynCond(Example.Criteria criteria, Object record) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();

        Stream.of(BASE).forEach(r -> {
            Object v = BeanUtils.getValueByProperty(record, r);
            if (v != null) {
                criteria.andEqualTo(r, v);
                c.addAndGet(1);
            }
        });
        Stream.of(LIKE).forEach(r -> {
            Object v = BeanUtils.getValueByProperty(record, r);
            if (v != null) {
                criteria.andLike(r, "%" + v + "%");
                c.addAndGet(1);
            }
        });

        return c.get();
    }
}
