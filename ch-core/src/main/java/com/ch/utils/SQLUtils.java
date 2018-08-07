package com.ch.utils;

import java.util.regex.Pattern;

public class SQLUtils {

    private SQLUtils() {
    }

    public enum Type {
        SELECT, UPDATE, CREATE, DELETE, UNKNOWN, DROP, ALTER, TRUNCATE
    }

    private final static String[] patternList = {".*(update|UPDATE) .*(set|SET) .*", ".*(delete|DELETE) .*(from|FROM) .*", ".*(drop|DROP) .*", ".*(alter|ALTER) .*(table|TABLE) .*", ".*(truncate|TRUNCATE) .*"};

    private final static String[] patternList2 = {".*(limit|LIMIT) [0-9]+,[0-9]+", ".*(limit|LIMIT) [0-9]+"};

    /**
     * 检查是否为查询SQL
     *
     * @param sql SQL
     * @return
     */
    public static boolean isSelectSql(String sql) {
        if (CommonUtils.isEmpty(sql)) {
            return false;
        }
        String str = trimComment(sql);
        for (String pattern : patternList) {
            boolean ok = Pattern.matches(pattern, str);
            if (ok) return false;
        }
        return true;
    }

    /**
     * 解析SQL类型
     *
     * @param sql
     * @return
     */
    public static Type parseType(String sql) {
        String str = trimComment(sql);

        if (str.startsWith("select") || str.startsWith("SELECT")) {
            return Type.SELECT;
        } else if (str.startsWith("update") || str.startsWith("UPDATE")) {
            return Type.UPDATE;
        } else if (str.startsWith("create") || str.startsWith("CREATE")) {
            return Type.CREATE;
        } else if (str.startsWith("delete") || str.startsWith("DELETE")) {
            return Type.DELETE;
        } else if (str.startsWith("drop") || str.startsWith("DROP")) {
            return Type.DROP;
        } else if (str.startsWith("alter") || str.startsWith("ALTER")) {
            return Type.ALTER;
        } else if (str.startsWith("truncate") || str.startsWith("TRUNCATE")) {
            return Type.TRUNCATE;
        }
        return Type.UNKNOWN;
    }

    /**
     * 是否为";"连接批量SQL
     * 注：不区分参数字符串带";"
     *
     * @param sql SQL
     * @return
     */
    public static boolean isBatch(String sql) {
        String tmp = trimComment(sql);
        if (CommonUtils.isNotEmpty(sql)) {
            if (tmp.endsWith(";")) tmp = tmp.substring(0, tmp.length() - 1);
            return tmp.contains(";");
        }
        return false;
    }

    /**
     * 删除SQL中前后空白与注释部分
     *
     * @param sql SQL
     * @return
     */
    public static String trimComment(String sql) {
        String tmp = StringUtils.trim(sql);
        String[] lines = tmp.split("\n");
        StringBuilder sb = new StringBuilder();
        int i = -1;
        for (String line : lines) {
            if (CommonUtils.isEmpty(line)) {
                continue;
            }
            if (line.startsWith("/*")) {
                i = 1;
            } else if (i == -1 && !line.startsWith("-- ") && !line.startsWith("#")) {
                if (line.contains("-- ") || line.contains("#")) {
                    int end = line.indexOf("-- ");
                    if (end < 0) {
                        end = line.indexOf("#");
                    }
                    sb.append(line.substring(0, end));
                } else {
                    sb.append(line);
                }
            } else if (line.endsWith("*/")) {
                i = -1;
            }
        }
        return sb.toString().trim();
    }

    /**
     * 判断是否有Limit
     *
     * @param sql SQL
     * @return
     */
    public static boolean hasLimit(String sql) {
        if (CommonUtils.isEmpty(sql)) {
            return false;
        }
        String str = trimComment(sql);
        for (String pattern : patternList2) {
            boolean ok = Pattern.matches(pattern, str);
            if (ok) return true;
        }
        return false;
    }
}
