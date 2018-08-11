package com.ch.shiro.utils;

import com.ch.shiro.authc.Principal;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 用户工具类
 * Created by zhimin on 2017/3/18.
 */
public class UserUtils {

    public static final String SUPER_ADMIN = "SUPER_ADMIN";

    private UserUtils() {
    }

    public static String getUsername(Object principal) {
        if (CommonUtils.isEmpty(principal)) return null;
        if (principal instanceof String) {
            return (String) principal;
        } else if (principal instanceof Principal) {
            return ((Principal) principal).getUsername();
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前登录用户名
     */
    public static String getCurrentUsername() {
        try {
            Subject subject = getSubject();
            if (subject.isAuthenticated()) {
                return getUsername(subject.getPrincipal());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前登录用户名,如里没有登录用户则返回默认
     *
     * @param defaultUsername 默认用户名
     * @return 当前用户名
     */
    public static String getCurrentUsername(String defaultUsername) {
        String username = getCurrentUsername();
        return StringUtils.isNotBlank(username) ? username : defaultUsername;
    }

    public static boolean isSuperAdmin() {
        return getSubject().hasRole(SUPER_ADMIN);
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}
