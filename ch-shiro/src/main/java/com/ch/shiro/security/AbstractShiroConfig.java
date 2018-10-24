package com.ch.shiro.security;

import com.ch.shiro.security.permission.UrlPermissionResolver;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;

/**
 * 描述：com.ch.cloud.admin.config
 *
 * @author 80002023
 *         2017/2/28.
 * @version 1.0
 * @since 1.8
 */
public abstract class AbstractShiroConfig {


    public abstract ShiroFilterFactoryBean shiroFilterFactory();
    public abstract Realm authRealm();
    public abstract CacheManager cacheManager();
    /**
     * 会影响Spring事务和切面事务
     *
     * @return
     */
//    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm());
        manager.setSessionManager(sessionManager());
        manager.setCacheManager(cacheManager());
        return manager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setGlobalSessionTimeout(18000000);
        manager.setDeleteInvalidSessions(true);
        manager.setSessionValidationSchedulerEnabled(true);
        return manager;
    }

    @Bean
    public PermissionResolver urlPermissionResolver() {
        return new UrlPermissionResolver();
    }


    @Bean
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }
}
