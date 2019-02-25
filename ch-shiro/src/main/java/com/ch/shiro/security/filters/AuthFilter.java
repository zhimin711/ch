package com.ch.shiro.security.filters;

import com.ch.e.Error;
import com.ch.result.HttpResult;
import com.ch.shiro.utils.ServletUtils;
import com.ch.utils.JsonUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：登录授权过滤 auth Filter
 * com.ch.cloud.shiro.security
 *
 * @author 80002023
 * 2017/2/28.
 * @version 1.0
 * @since 1.8
 */
public class AuthFilter extends AuthorizationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        Subject subject = getSubject(servletRequest, servletResponse);
        return subject.getPrincipal() != null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        logger.debug("onAccessDenied...{}", JsonUtils.toJson(subject.getPrincipal()));
        if (subject.getPrincipal() == null) {
            // if request ajax
            if (ServletUtils.isAjax(WebUtils.toHttp(request))) {
                HttpResult<String> result = new HttpResult<>(Error.NOT_LOGIN);
                logger.debug("request ajax...{}", JsonUtils.toJson(result));
                ServletUtils.write(WebUtils.toHttp(response), result);
            } else {
                String url = getPathWithinApplication(request);

                logger.debug("saveRequestAndRedirectToLogin ...{}", url);
                saveRequestAndRedirectToLogin(request, response);
            }

        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            String unauthorizedUrl = getUnauthorizedUrl();
            //SHIRO-142 - ensure that redirect _or_ err code occurs - both cannot happen due to response commit:
            logger.debug("unauthorizedUrl ...{}", unauthorizedUrl);
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                logger.debug("SC_UNAUTHORIZED ...");
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }

}
