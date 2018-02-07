package com.ch.shiro.tx;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * AbstractTransaction：抽象事务代理
 * Created by 80002023 on 2017/6/21.
 */
public abstract class AbstractTransaction {


    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        Properties attributes = new Properties();

        attributes.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
//        attributes.setProperty("load*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("find*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("query*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable,readOnly");

//        attributes.setProperty("save*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("batchSave*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("batchUpdate*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Throwable");
//        attributes.setProperty("assign*", "PROPAGATION_REQUIRED,-Throwable");

        attributes.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");

        return new TransactionInterceptor(transactionManager, attributes);
    }

    /**
     * 自动创建事务代理
     *
     * @return transactionAutoProxy - 事务代理
     */
    @Bean
    public BeanNameAutoProxyCreator transactionAutoProxy() {
        BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
        transactionAutoProxy.setProxyTargetClass(true);
//        transactionAutoProxy.setOrder(0);
        //指定对满足哪些bean name的bean自动生成业务代理
        // 下面是所有需要自动创建事务代理的bean
        transactionAutoProxy.setBeanNames("*Service", "*ServiceImpl");
        //下面定义BeanNameAutoProxyCreator所需的事务拦截器
        transactionAutoProxy.setInterceptorNames("transactionInterceptor");
        return transactionAutoProxy;
    }


    /**
     * DefaultAdvisorAutoProxyCreator搜索容器中的 advisor,并为每个bean创建代理
     *
     * @return autoProxy - bean创建代理
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxy = new DefaultAdvisorAutoProxyCreator();
        autoProxy.setProxyTargetClass(true);
//        autoProxy.setOrder(1);
//        autoProxy.setInterceptorNames("transactionInterceptor");
        return autoProxy;
    }

}
