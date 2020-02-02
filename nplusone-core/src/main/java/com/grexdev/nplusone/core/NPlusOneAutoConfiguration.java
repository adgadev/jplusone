package com.grexdev.nplusone.core;

import com.grexdev.nplusone.core.proxy.NPlusOneBeanBeanPostProcessor;
import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.proxy.datasource.ProxyContext;
import com.grexdev.nplusone.core.tracking.TrackingStateListener;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// TODO: add conditionals
public class NPlusOneAutoConfiguration {

    @Bean
    public StateListener stateListener() {
        return new TrackingStateListener();
    }

    @Bean
    public ProxyContext proxyContext(StateListener stateListener) {
        return new ProxyContext(stateListener);
    }

    @Bean
    public BeanPostProcessor nplusOneProxyBeanPostProcessor(ProxyContext proxyContext) {
        return new NPlusOneBeanBeanPostProcessor(proxyContext);
    }

}
