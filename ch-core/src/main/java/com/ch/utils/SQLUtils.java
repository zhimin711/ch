package com.ch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLUtils {

    private SQLUtils() {
    }

    public enum Type {
        INSERT, SELECT, UPDATE, CREATE, DELETE, UNKNOWN, DROP, ALTER, TRUNCATE
    }

    private final static String[] patternList = {
            ".*((?i)insert) .*((?i)value|(?i)values) .*", ".*((?i)update) .*((?i)set) .*",
            ".*((?i)delete) .*((?i)from) .*", ".*((?i)drop) .*",
            ".*((?i)alter) .*", ".*((?i)truncate) .*"
    };

    private final static String[] patternList2 = {".*((?i)limit)\\s+[0-9]+,[0-9]+", ".*((?i)limit)\\s+[0-9]+"};

    /**
     * 检查是否为查询SQL
     *
     * @param sql SQL
     * @return
     */
    public static boolean isSelect(String sql) {
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
        str = str.toUpperCase();
        if (str.startsWith("INSERT")) {
            return Type.INSERT;
        } else if (str.startsWith("SELECT")) {
            return Type.SELECT;
        } else if (str.startsWith("UPDATE")) {
            return Type.UPDATE;
        } else if (str.startsWith("CREATE")) {
            return Type.CREATE;
        } else if (str.startsWith("DELETE")) {
            return Type.DELETE;
        } else if (str.startsWith("DROP")) {
            return Type.DROP;
        } else if (str.startsWith("ALTER")) {
            return Type.ALTER;
        } else if (str.startsWith("TRUNCATE")) {
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
        for (String l : lines) {
            if (CommonUtils.isEmpty(l)) {
                continue;
            }
            String line = l.trim();
            if (line.startsWith("/*")) {
                i = 1;
            } else if (i == -1 && !line.startsWith("-- ") && !line.startsWith("#")) {
                sb.append(" ");
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
