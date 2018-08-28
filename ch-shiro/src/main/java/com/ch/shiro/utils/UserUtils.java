package com.ch.shiro.utils;

import com.ch.shiro.authc.Principal;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 用户工具类
 * Created by zhimin on 2017/3/18.
 */
public class UserUtils {

    private static Logger logger = LoggerFactory.getLogger(ServletUtils.class);
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
            logger.error("get current username failed!",e);
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


    /**
     * 获取当前登录用户名数据权限Code
     *
     * @return 当前登录用户名
     */
    public static List<String> getCodes() {
        try {
            Subject subject = getSubject();
            if (subject.isAuthenticated() && subject.getPrincipal() instanceof Principal) {
                return ((Principal) subject.getPrincipal()).getCodes();
            }
        } catch (Exception e) {
            logger.error("get current username sys codes failed!",e);
        }
        return Lists.newArrayList();
    }


    public static boolean isSuperAdmin() {
        return getSubject().hasRole(SUPER_ADMIN);
    }

    public static boolean hasRole(String code) {
        return getSubject().hasRole(code);
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }


    public static Realm getCurrRealm() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        return rsm.getRealms().iterator().next();
    }

    public static String getCurrRealmName() {
        return getSubject().getPrincipals().getRealmNames().iterator().next();
    }

}
