package com.ch.utils;

import java.util.regex.Pattern;

public class SQLUtils {

    private SQLUtils() {
    }

    private final static String[] patternList = {".*(update|UPDATE) .*(set|SET) .*", ".*(delete|DELETE) .*(from|FROM) .*", ".*(drop|DROP) .*", ".*(alter|ALTER) .*(table|TABLE) .*", ".*(truncate|TRUNCATE) .*"};

    public static boolean isSelectSql(String sql) {
        if (CommonUtils.isEmpty(sql)) {
            return false;
        }
        for (String pattern : patternList) {
            boolean ok = Pattern.matches(pattern, sql);
            if (ok) return false;
        }
        return true;
    }

    public enum Type {
        SELECT, UPDATE, CREATE, DELETE, UNKNOW, DROP, ALTER, TRUNCATE
    }

    public static Type parseType(String sql) {
        String tmp = org.apache.commons.lang3.StringUtils.trim(sql);
        String[] lines = tmp.split("\n");
        String sqlStart = "";
        if (lines.length <= 1) {
            sqlStart = tmp;
        } else {
            int i = 0;
            for (String line : lines) {
                if (CommonUtils.isEmpty(line)) {
                    continue;
                }
                if (line.startsWith("/*")) {
                    i = 1;
                } else if (i == 0 && !line.startsWith("-- ") && !line.startsWith("#")) {
                    sqlStart = line;
                    break;
                } else if (line.startsWith("*/")) {
                    i = 0;
                }
            }
        }

        if (sqlStart.startsWith("select") || sqlStart.startsWith("SELECT")) {
            return Type.SELECT;
        } else if (sqlStart.startsWith("update") || sqlStart.startsWith("UPDATE")) {
            return Type.UPDATE;
        } else if (sqlStart.startsWith("create") || sqlStart.startsWith("CREATE")) {
            return Type.CREATE;
        } else if (sqlStart.startsWith("delete") || sqlStart.startsWith("DELETE")) {
            return Type.DELETE;
        } else if (sqlStart.startsWith("drop") || sqlStart.startsWith("DROP")) {
            return Type.DROP;
        } else if (sqlStart.startsWith("alter") || sqlStart.startsWith("ALTER")) {
            return Type.ALTER;
        } else if (sqlStart.startsWith("truncate") || sqlStart.startsWith("TRUNCATE")) {
            return Type.TRUNCATE;
        }
        return Type.UNKNOW;
    }
}
