package com.netease.channel.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Properties;

public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static Properties properties;

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static String getPropertyValue(String pName) {
        if (properties != null) {
            return properties.getProperty(pName);
        } else {
            return null;
        }

    }

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
        try {
            this.properties = context.getBean(PropertiesFactoryBean.class).getObject();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}
