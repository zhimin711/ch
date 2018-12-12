package com.ch.mybatis.utils;

import com.ch.utils.CommonUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * desc:
 *
 * @author zhimin
 * @date 2018/12/10 12:26 AM
 */
public class ExampleUtils {

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

}
