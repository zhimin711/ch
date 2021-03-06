package com.ch.shiro.security.filters;

import com.ch.http.HttpResult;
import com.ch.shiro.utils.ServletUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：com.ch.cloud.admin.security
 *
 * @author 80002023
 *         2017/2/28.
 * @version 1.0
 * @since 1.8
 */
public class AccessFilter extends AccessControlFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String url = getPathWithinApplication(servletRequest);
        logger.info("AccessFilter isAccessAllowed. url: {}", url);
        return "admin".equals(subject.getPrincipal()) || subject.isPermitted(url);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        logger.info("AccessFilter onAccessDenied ...");
        if (ServletUtils.isAjax(WebUtils.toHttp(servletRequest))) {
            HttpResult result = new HttpResult();
            result.newError("403", "UNAUTHORIZED", "No Permission!");
            ServletUtils.write(WebUtils.toHttp(servletResponse), result);
        } else {
            WebUtils.toHttp(servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return false;
    }
}
