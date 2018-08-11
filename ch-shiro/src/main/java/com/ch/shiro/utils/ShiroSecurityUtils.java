package com.ch.shiro.utils;

import com.ch.shiro.authc.AuthPrincipals;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.Collection;

/**
 * @author 01370603
 * @date 2018/8/11 18:24
 */
public class ShiroSecurityUtils {

    public static String getUsername() {
        Subject subject = SecurityUtils.getSubject();
        return subject == null ? null : (String) subject.getPrincipal();
    }

    public static String getUsername(Subject subject) {
        return subject == null ? null : (String) subject.getPrincipal();
    }

    public static boolean isSuperAdmin() {
        Subject subject = SecurityUtils.getSubject();
        return isSuperAdmin(subject);
    }

    public static boolean isSuperAdmin(Subject subject) {
        if(subject == null) return false;
        String username = (String) subject.getPrincipal();
        PrincipalCollection principalCollection = subject.getPrincipals();
        if (principalCollection instanceof AuthPrincipals) {
            Collection collection = principalCollection.fromRealm(username);
            AuthPrincipals.Principal principal = (AuthPrincipals.Principal) collection.iterator().next();
            return principal.isSuperAdmin();
        }
        return false;
    }

}
