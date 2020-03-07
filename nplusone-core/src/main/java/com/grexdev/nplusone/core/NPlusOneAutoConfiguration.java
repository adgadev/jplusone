package com.grexdev.nplusone.core;

import com.grexdev.nplusone.core.flyway.FlywayAspect;
import com.grexdev.nplusone.core.properties.NPlusOneProperties;
import com.grexdev.nplusone.core.proxy.ProxiedRootsBeanPostProcessor;
import com.grexdev.nplusone.core.registry.RootNode;
import com.grexdev.nplusone.core.report.ReportGenerator;
import com.grexdev.nplusone.core.tracking.ActivationStateListener;
import com.grexdev.nplusone.core.tracking.TrackingContext;
import com.grexdev.nplusone.core.tracking.TrackingStateListener;
import com.grexdev.nplusone.core.utils.ApplicationScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(NPlusOneProperties.class)
@ConditionalOnProperty(prefix = "nplusone", name = "enabled", matchIfMissing = true)
public class NPlusOneAutoConfiguration {

    private final NPlusOneProperties nPlusOneProperties;

    private final ApplicationContext applicationContext;

    @Bean
    public ApplicationScanner applicationScanner() {
        return new ApplicationScanner(applicationContext);
    }

    @Bean
    public TrackingContext proxyContext(ApplicationScanner applicationScanner) {
        String applicationRootPackage = Optional.ofNullable(nPlusOneProperties.getApplicationRootPackage())
                .orElseGet(applicationScanner::resolveRootApplicationPackage);
        return new TrackingContext(applicationRootPackage, nPlusOneProperties.isDebugMode());
    }

    @Bean
    public RootNode rootNode() {
        return new RootNode();
    }

    @Bean
    public ReportGenerator reportGenerator() {
        return new ReportGenerator(nPlusOneProperties.getReport());
    }

    @Bean
    public TrackingStateListener trackingStateListener(TrackingContext context, ReportGenerator reportGenerator, RootNode rootNode) {
        return new TrackingStateListener(context, reportGenerator, rootNode);
    }

    @Bean
    public ActivationStateListener activationStateListener(TrackingContext trackingContext, TrackingStateListener stateListener) {
        return new ActivationStateListener(stateListener, trackingContext);
    }

    @Bean
    public BeanPostProcessor proxiedRootsBeanPostProcessor(ActivationStateListener stateListener) {
        return new ProxiedRootsBeanPostProcessor(stateListener);
    }

    // TODO: check what happens if flyway dependency not added
    @Bean
    public FlywayAspect flywayAspect(TrackingContext trackingContext) {
        return new FlywayAspect(trackingContext);
    }

}
