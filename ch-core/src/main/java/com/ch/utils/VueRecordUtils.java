package com.ch.utils;

import com.ch.pojo.VueRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * decs:Vue 工具类
 *
 * @author 01370603
 * @date 2019/11/5
 */
public class VueRecordUtils {


    /**
     * By id
     *
     * @param tree
     * @return
     */
    public static List<VueRecord> covertTree(List<?> tree) {
        if (CommonUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }
        return covertRecords(tree, "id", "name", "children");
    }

    /**
     * By code
     *
     * @param tree
     * @return
     */
    public static List<VueRecord> covertTree2(List<?> tree) {
        if (CommonUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }
        return covertRecords(tree, "code", "name", "children");
    }

    public static List<VueRecord> covertTree(List<?> tree, final String valueProperty, final String labelProperty, final String childrenProperty) {
        if (CommonUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }
        return covertRecords(tree, valueProperty, labelProperty, childrenProperty);
    }

    private static List<VueRecord> covertRecords(Collection<?> records, final String valueProperty, final String labelProperty, final String childrenProperty) {
        if (CommonUtils.isEmpty(records)) {
            return null;
        }
        return records.stream().map(o -> {
            Object value = BeanExtUtils.getValueByProperty(o, valueProperty);
            Object label = BeanExtUtils.getValueByProperty(o, labelProperty);
            Object children = BeanExtUtils.getValueByProperty(o, childrenProperty);
            VueRecord record = new VueRecord();
            if (value != null) {
                record.setValue(value.toString());
            }
            if (label != null) {
                record.setLabel(label.toString());
            }
            if (children instanceof Collection && ((Collection) children).size() > 0) {
                List<VueRecord> children1 = covertRecords((Collection) children, valueProperty, labelProperty, childrenProperty);
                record.setChildren(children1);
            }
            return record;
        }).collect(Collectors.toList());
    }
}
