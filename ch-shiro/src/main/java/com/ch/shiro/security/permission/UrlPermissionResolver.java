package com.ch.shiro.security.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 描述：com.ch.cloud.admin.security
 *
 * @author 80002023
 *         2017/2/28.
 * @version 1.0
 * @since 1.8
 */
public class UrlPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String permissionString) {
        if (permissionString.startsWith("/")) {
            return new UrlPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
