package com.grexdev.nplusone.core;

import com.grexdev.nplusone.core.proxy.NPlusOneBeanBeanPostProcessor;
import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.proxy.datasource.ProxyContext;
import com.grexdev.nplusone.core.tracking.TrackingStateListener;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NPlusOneAutoConfiguration {

    @Bean
    public ProxyContext proxyContext() {
        return new ProxyContext();
    }

    @Bean
    public StateListener stateListener() {
        return new TrackingStateListener();
    }

    @Bean
    public BeanPostProcessor nplusOneProxyBeanPostProcessor(ProxyContext proxyContext, StateListener stateListener) {
        return new NPlusOneBeanBeanPostProcessor(proxyContext, stateListener);
    }

}
