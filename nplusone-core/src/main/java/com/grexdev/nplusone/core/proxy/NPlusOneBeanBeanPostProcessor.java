package com.grexdev.nplusone.core.proxy;

import com.grexdev.nplusone.core.proxy.datasource.ProxyContext;
import com.grexdev.nplusone.core.proxy.datasource.ProxyDataSource;
import com.grexdev.nplusone.core.proxy.jpa.EntityManagerFactoryProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@RequiredArgsConstructor
public class NPlusOneBeanBeanPostProcessor implements BeanPostProcessor {

    private final ProxyContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            return new ProxyDataSource((DataSource) bean, context);
        }

        if (bean instanceof EntityManagerFactory) {
            return new EntityManagerFactoryProxy((EntityManagerFactory) bean, context.getStateListener());
        }

        return bean;
    }
};

