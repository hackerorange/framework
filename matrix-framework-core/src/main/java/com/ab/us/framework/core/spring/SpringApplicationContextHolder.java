package com.ab.us.framework.core.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 用于持有spring的applicationContext,一个系统只能有一个ApplicationContextHolder
 *
 * @author framework
 * @since 3.0
 */
@EnableAutoConfiguration
@Configuration
public class SpringApplicationContextHolder implements ApplicationContextAware, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(SpringApplicationContextHolder.class);
    private static ApplicationContext applicationContext;
    private static boolean isDestroyed = false;

    public static boolean isApplicationContextHolden() {
        return applicationContext != null;
    }

    public static ApplicationContext getApplicationContext() {
        if (isDestroyed) {
            throw new IllegalStateException("ApplicationContextHolder已经被释放");
        }
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContextHolder为空，可能还没有初始化");
        }
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        isDestroyed = false;
        applicationContext = context;
        logger.info("正在初始化ApplicationContext,displayName:" + applicationContext.getDisplayName());
    }

    public static Object getBean(String beanName) {
        try {
            return getApplicationContext().getBean(beanName);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T getBean(Class<T> beanClass) {
        try {
            return getApplicationContext().getBean(beanClass);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T getBean(String beanName, Class<T> beanClass) {
        try {
            return getApplicationContext().getBean(beanName, beanClass);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public void destroy() throws Exception {
        if (applicationContext != null) {
            logger.info("正在释放ApplicationContext ,displayName:" + applicationContext.getDisplayName());
        }
        isDestroyed = true;
        applicationContext = null;
    }
}
