/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adgadev.jplusone.core;

import com.adgadev.jplusone.core.flyway.FlywayAspect;
import com.adgadev.jplusone.core.properties.JPlusOneProperties;
import com.adgadev.jplusone.core.proxy.ProxiedRootsBeanPostProcessor;
import com.adgadev.jplusone.core.proxy.hibernate.HibernateCollectionInitialisationEventListener;
import com.adgadev.jplusone.core.registry.RootNode;
import com.adgadev.jplusone.core.report.ReportGenerator;
import com.adgadev.jplusone.core.report.output.ReportChannelFactory;
import com.adgadev.jplusone.core.tracking.ActivationStateListener;
import com.adgadev.jplusone.core.tracking.LoggingStateListener;
import com.adgadev.jplusone.core.tracking.TrackingContext;
import com.adgadev.jplusone.core.tracking.TrackingStateListener;
import com.adgadev.jplusone.core.utils.ApplicationScanner;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(JPlusOneProperties.class)
@ConditionalOnProperty(prefix = "jplusone", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter(name = "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration")
public class JPlusOneAutoConfiguration {

    private static final AtomicInteger SPRING_CONTEXT_NUMBER_PROVIDER = new AtomicInteger(0);

    private final JPlusOneProperties jPlusOneProperties;

    private final ApplicationContext applicationContext;

    @Bean
    public ApplicationScanner applicationScanner() {
        return new ApplicationScanner(applicationContext);
    }

    @Bean
    public TrackingContext proxyContext(ApplicationScanner applicationScanner) {
        String applicationRootPackage = Optional.ofNullable(jPlusOneProperties.getApplicationRootPackage())
                .orElseGet(applicationScanner::resolveRootApplicationPackage);
        return new TrackingContext(applicationRootPackage, jPlusOneProperties.isDebugMode(), jPlusOneProperties.getVerbosityLevel());
    }

    @Bean
    public RootNode rootNode() {
        return new RootNode();
    }

    @Bean
    public ReportChannelFactory reportChannelFactory() {
        int springContextNumber = SPRING_CONTEXT_NUMBER_PROVIDER.incrementAndGet();
        return new ReportChannelFactory(jPlusOneProperties.getReport(), springContextNumber);
    }

    @Bean
    public ReportGenerator reportGenerator(ReportChannelFactory reportChannelFactory) {
        return new ReportGenerator(jPlusOneProperties.getReport(), reportChannelFactory);
    }

    @Bean
    public TrackingStateListener trackingStateListener(TrackingContext context, ReportGenerator reportGenerator, RootNode rootNode) {
        return new TrackingStateListener(context, reportGenerator, rootNode);
    }

    @Bean
    public LoggingStateListener loggingStateListener(TrackingContext context, TrackingStateListener stateListener) {
        return new LoggingStateListener(context, stateListener);
    }

    @Bean
    public ActivationStateListener activationStateListener(TrackingContext trackingContext, LoggingStateListener stateListener) {
        return new ActivationStateListener(stateListener, trackingContext);
    }

    @Bean
    public BeanPostProcessor proxiedRootsBeanPostProcessor(ActivationStateListener stateListener) {
        return new ProxiedRootsBeanPostProcessor(stateListener);
    }

    @Bean
    public HibernateCollectionInitialisationEventListener hibernateCollectionInitialisationEventListener(
            EntityManagerFactory entityManagerFactory, ActivationStateListener stateListener) {
        return new HibernateCollectionInitialisationEventListener(entityManagerFactory, stateListener);
    }

    @Bean
    @ConditionalOnClass(name = "org.flywaydb.core.Flyway")
    public FlywayAspect flywayAspect(TrackingContext trackingContext) {
        return new FlywayAspect(trackingContext);
    }
}
