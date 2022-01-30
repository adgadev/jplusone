/*
 * Copyright (c) 2022 Adam Gaj
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

package com.adgadev.jplusone.core.proxy.datasource;

import com.adgadev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import javax.sql.DataSource;
import java.sql.Connection;

import static java.util.Objects.nonNull;

@Slf4j
public class DataSourceAopProxyFactory {

    public static <T extends DataSource> T createProxy(T dataSource, StateListener stateListener) {
        Advisor advisor = createGetConnectionResultProxyfingAdvisor(stateListener);

        if (dataSource instanceof Advised) {
            log.debug("Enhancing existing DataSource proxy by adding advisor");
            ((Advised) dataSource).addAdvisor(advisor);
            return dataSource;

        } else {
            ProxyFactory proxyFactory = new ProxyFactory(dataSource);
            proxyFactory.setProxyTargetClass(true);
            proxyFactory.addAdvisor(advisor);
            return (T) proxyFactory.getProxy();
        }
    }

    private static Advisor createGetConnectionResultProxyfingAdvisor(StateListener stateListener) {
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName("getConnection");
        advisor.setAdvice(new ResultProxyWrappingMethodInterceptor(stateListener));
        return advisor;
    }

    @RequiredArgsConstructor
    private static class ResultProxyWrappingMethodInterceptor implements MethodInterceptor {

        private final StateListener stateListener;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();

            if (nonNull(result) && !(result instanceof ConnectionProxy)) {
                log.trace("Returning wrapped connection");
                return new ConnectionProxy((Connection) result, stateListener);
            }

            return result;
        }
    }
}
