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

package com.grexdev.jplusone.core;

import com.grexdev.jplusone.core.flyway.FlywayAspect;
import com.grexdev.jplusone.core.properties.JPlusOneProperties;
import com.grexdev.jplusone.core.proxy.ProxiedRootsBeanPostProcessor;
import com.grexdev.jplusone.core.proxy.hibernate.HibernateCollectionInitialisationEventListener;
import com.grexdev.jplusone.core.report.ReportGenerator;
import com.grexdev.jplusone.core.tracking.ActivationStateListener;
import com.grexdev.jplusone.core.tracking.TrackingContext;
import com.grexdev.jplusone.core.tracking.TrackingStateListener;
import com.grexdev.jplusone.core.utils.ApplicationScanner;
import com.grexdev.jplusone.core.proxy.datasource.HikariDataSourceAspect;
import com.grexdev.jplusone.core.registry.RootNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JPlusOneProperties.class)
@ConditionalOnProperty(prefix = "jplusone", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter(name = "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration")
public class JPlusOneAutoConfiguration {

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
        return new TrackingContext(applicationRootPackage, jPlusOneProperties.isDebugMode());
    }

    @Bean
    public RootNode rootNode() {
        return new RootNode();
    }

    @Bean
    public ReportGenerator reportGenerator() {
        return new ReportGenerator(jPlusOneProperties.getReport());
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
        boolean useHikariDataSourceAspect = applicationContext.containsBean("org.springframework.cloud.autoconfigure.RefreshAutoConfiguration");
        return new ProxiedRootsBeanPostProcessor(stateListener, useHikariDataSourceAspect);
    }

    @Bean
    public HibernateCollectionInitialisationEventListener hibernateCollectionInitialisationEventListener(
            EntityManagerFactory entityManagerFactory, ActivationStateListener stateListener) {
        return new HibernateCollectionInitialisationEventListener(entityManagerFactory, stateListener);
    }

    @Bean
    @ConditionalOnClass(name = "com.zaxxer.hikari.HikariDataSource")
    @ConditionalOnBean(type = { "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration"})
    public HikariDataSourceAspect hikariDataSourceAspect(ActivationStateListener stateListener) {
        return new HikariDataSourceAspect(stateListener);
    }

    @Bean
    @ConditionalOnClass(name = "org.flywaydb.core.Flyway")
    public FlywayAspect flywayAspect(TrackingContext trackingContext) {
        return new FlywayAspect(trackingContext);
    }
}
