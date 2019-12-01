package com.ch.mybatis.utils;

import com.ch.t.ConditionType;
import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.SQLUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * @param criteria
     * @param property
     * @param values
     * @return
     */
    public static int dynIn(Example.Criteria criteria, String property, List<?> values) {
        if (CommonUtils.isEmpty(values)) {
            return 0;
        } else {
            criteria.andIn(property, values);
        }
        return 1;
    }

    /**
     * 动态拼接指定字段条件
     *
     * @param criteria
     * @param property
     * @param values
     * @return
     */
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

    /**
     * 动态拼接基础字段条件
     *
     * @param criteria 条件装配
     * @param record   条件
     * @return 装配条件数
     */
    public static int dynCond(Example.Criteria criteria, Object record) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        c.getAndAdd(dynEqual(criteria, record, BASE));
        c.getAndAdd(dynLike(criteria, record, LIKE));
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param criteria   条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynEqual(Example.Criteria criteria, Object record, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v)) {
                continue;
            }
            if (v instanceof Iterable) {
                criteria.andIn(r, (Iterable) v);
            } else {
                criteria.andEqualTo(r, v);
            }
            c.addAndGet(1);
        }
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param criteria   条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynNotEqual(Example.Criteria criteria, Object record, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v)) {
                continue;
            }
            criteria.andNotEqualTo(r, v);
            c.addAndGet(1);
        }
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param criteria      条件装配
     * @param record        条件
     * @param conditionType 条件类型
     * @param property      属性
     * @return 装配条件数
     */
    public static int dynType(Example.Criteria criteria, Object record, ConditionType conditionType, String property) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        if (conditionType == ConditionType.NOT_NULL) {
            criteria.andIsNotNull(property);
            return 1;
        } else if (conditionType == ConditionType.NULL) {
            criteria.andIsNull(property);
            return 1;
        }
        Object v = BeanExtUtils.getValueByProperty(record, property);
        if (v == null) {
            return 0;
        }
        if (conditionType == ConditionType.NOT_EQUAL_TO) {
            criteria.andNotEqualTo(property, v);
        } else if (conditionType == ConditionType.GREATER_THAN) {
            criteria.andGreaterThan(property, v);
        } else if (conditionType == ConditionType.LESS_THAN) {
            criteria.andLessThan(property, v);
        } else {
            criteria.andEqualTo(property, v);
        }
        c.addAndGet(1);
        return c.get();
    }

    /**
     * 动态拼接指定字段Like条件（只装配String字符串）
     *
     * @param criteria   条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynLike(Example.Criteria criteria, Object record, String... properties) {
        return dynLike(criteria, record, LikeType.LIKE_ANY, properties);
    }

    /**
     * 动态拼接指定字段Like条件（只装配String字符串）
     *
     * @param criteria   条件装配
     * @param record     条件
     * @param likeType   模糊类型
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynLike(Example.Criteria criteria, Object record, LikeType likeType, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v) || !(v instanceof String)) continue;
            String tmp = ((String) v).trim();
            if (likeType == LikeType.LIKE_ANY) {
                tmp = SQLUtils.likeAny(tmp);
            } else if (likeType == LikeType.LIKE_SUFFIX) {
                tmp = SQLUtils.likeSuffix(tmp);
            } else if (likeType == LikeType.LIKE_PREFIX) {
                tmp = SQLUtils.likePrefix(tmp);
            }
            criteria.andLike(r, tmp);
            c.addAndGet(1);
        }
        return c.get();
    }

    public enum LikeType {
        /**
         *
         */
        LIKE_ANY,
        /**
         * 后模糊
         */
        LIKE_SUFFIX,
        /**
         * 前模糊
         */
        LIKE_PREFIX
    }


    /**
     * @param sqls
     * @param property
     * @param values
     * @return
     */
    public static int dynIn(Sqls sqls, String property, List<?> values) {

        if (CommonUtils.isEmpty(values)) {
            return 0;
        } else {
            sqls.andIn(property, values);
        }
        return 1;
    }

    /**
     * 动态拼接指定字段条件
     *
     * @param sqls
     * @param property
     * @param values
     * @return
     */
    public static int dynInOrLike(Sqls sqls, String property, List<String> values) {
        if (CommonUtils.isEmpty(values)) {
            return 0;
        } else if (values.size() == 1) {
            sqls.andLike(property, "%" + values.get(0) + "%");
        } else {
            sqls.andIn(property, values);
        }
        return 1;
    }

    /**
     * 动态拼接基础字段条件
     *
     * @param sqls   条件装配
     * @param record 条件
     * @return 装配条件数
     */
    public static int dynCommon(Sqls sqls, Object record) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        c.getAndAdd(dynEqual(sqls, record, BASE));
        c.getAndAdd(dynLike(sqls, record, LIKE));
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param sqls       条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynEqual(Sqls sqls, Object record, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v)) {
                continue;
            }
            if (v instanceof Iterable) {
                sqls.andIn(r, (Iterable) v);
            } else {
                sqls.andEqualTo(r, v);
            }
            c.addAndGet(1);
        }
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param sqls       条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynNotEqual(Sqls sqls, Object record, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v)) {
                continue;
            }
            sqls.andNotEqualTo(r, v);
            c.addAndGet(1);
        }
        return c.get();
    }

    /**
     * 动态拼接指定字段条件(equal or in)
     *
     * @param sqls          条件装配
     * @param record        条件
     * @param conditionType 条件类型
     * @param property      属性
     * @return 装配条件数
     */
    public static int dynType(Sqls sqls, Object record, ConditionType conditionType, String property) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        if (conditionType == ConditionType.NOT_NULL) {
            sqls.andIsNotNull(property);
            return 1;
        } else if (conditionType == ConditionType.NULL) {
            sqls.andIsNull(property);
            return 1;
        }
        Object v = BeanExtUtils.getValueByProperty(record, property);
        if (v == null) {
            return 0;
        }
        if (conditionType == ConditionType.NOT_EQUAL_TO) {
            sqls.andNotEqualTo(property, v);
        } else if (conditionType == ConditionType.GREATER_THAN) {
            sqls.andGreaterThan(property, v);
        } else if (conditionType == ConditionType.LESS_THAN) {
            sqls.andLessThan(property, v);
        } else {
            sqls.andEqualTo(property, v);
        }
        c.addAndGet(1);
        return c.get();
    }

    /**
     * 动态拼接指定字段Like条件（只装配String字符串）
     *
     * @param sqls       条件装配
     * @param record     条件
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynLike(Sqls sqls, Object record, String... properties) {
        return dynLike(sqls, record, LikeType.LIKE_ANY, properties);
    }

    /**
     * 动态拼接指定字段Like条件（只装配String字符串）
     *
     * @param sqls       条件装配
     * @param record     条件
     * @param likeType   模糊类型
     * @param properties 属性
     * @return 装配条件数
     */
    public static int dynLike(Sqls sqls, Object record, LikeType likeType, String... properties) {
        AtomicInteger c = new AtomicInteger(0);
        if (record == null) return c.get();
        for (String r : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, r);
            if (CommonUtils.isEmpty(v) || !(v instanceof String)) continue;
            String tmp = ((String) v).trim();
            if (likeType == LikeType.LIKE_ANY) {
                tmp = SQLUtils.likeAny(tmp);
            } else if (likeType == LikeType.LIKE_SUFFIX) {
                tmp = SQLUtils.likeSuffix(tmp);
            } else if (likeType == LikeType.LIKE_PREFIX) {
                tmp = SQLUtils.likePrefix(tmp);
            }
            sqls.andLike(r, tmp);
            c.addAndGet(1);
        }
        return c.get();
    }

    /**
     * 动态拼接指定字段Like条件（只装配String字符串）
     *
     * @param example 条件装配
     * @param orderBy 排序
     * @return 装配条件数
     */
    public static int orderBy(Example example, String orderBy) {
        if (CommonUtils.isEmpty(orderBy)) return 0;

        String[] props = orderBy.split(",");
        int c = 0;
        for (String prop : props) {
            if (CommonUtils.isEmpty(prop)) {
                continue;
            }
            boolean isDesc = false;
            String str = prop;
            if (prop.toLowerCase().endsWith(" desc")) {
                isDesc = true;
                str = prop.substring(0, prop.length() - 5);
            } else if (prop.toLowerCase().endsWith(" asc")) {
                str = prop.substring(0, prop.length() - 4);
            }
            if (isDesc) {
                example.orderBy(str).desc();
            } else {
                example.orderBy(str).asc();
            }
            c++;
        }

        return c;
    }
}
