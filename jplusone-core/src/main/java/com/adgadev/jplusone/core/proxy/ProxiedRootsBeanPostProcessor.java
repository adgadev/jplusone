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

package com.adgadev.jplusone.core.proxy;

import com.adgadev.jplusone.core.proxy.datasource.DataSourceAopProxyFactory;
import com.adgadev.jplusone.core.proxy.jpa.EntityManagerFactoryAopProxyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
public class ProxiedRootsBeanPostProcessor implements BeanPostProcessor {

    private final StateListener stateListener;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            log.debug("DataSource wrapped in the proxy, beanName: {}", beanName);
            return DataSourceAopProxyFactory.createProxy((DataSource) bean, stateListener);
        }

        if (bean instanceof EntityManagerFactory) {
            log.debug("EntityManagerFactory wrapped in the proxy, beanName: {}", beanName);
            return EntityManagerFactoryAopProxyFactory.createProxy((EntityManagerFactory) bean, stateListener);
        }

        return bean;
    }
}

