package com.grexdev.nplusone.core;

import com.grexdev.nplusone.core.proxy.ProxiedRootsBeanPostProcessor;
import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.tracking.TrackingContext;
import com.grexdev.nplusone.core.tracking.TrackingStateListener;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// TODO: add conditionals
public class NPlusOneAutoConfiguration {

    @Bean
    public TrackingContext proxyContext() {
        return new TrackingContext();
    }

    @Bean
    public StateListener stateListener(TrackingContext context) {
        return new TrackingStateListener(context);
    }

    @Bean
    public BeanPostProcessor proxiedRootsBeanPostProcessor(StateListener stateListener) {
        return new ProxiedRootsBeanPostProcessor(stateListener);
    }

}
