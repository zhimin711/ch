package com.ch.shiro.security.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

/**
 * 描述：com.ch.cloud.admin.security
 *
 * @author 80002023
 *         2017/2/28.
 * @version 1.0
 * @since 1.8
 */
public class UrlPermission implements Permission {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    UrlPermission(String url) {
        setUrl(url);
    }

    public boolean implies(Permission p) {
        if (!(p instanceof UrlPermission)) {
            return false;
        }
        UrlPermission up = (UrlPermission) p;
        PatternMatcher patternMatcher = new AntPathMatcher();
        return patternMatcher.matches(this.getUrl(), up.getUrl());
    }

}
