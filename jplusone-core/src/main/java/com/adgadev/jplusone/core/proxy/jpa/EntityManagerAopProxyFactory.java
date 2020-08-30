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

package com.adgadev.jplusone.core.proxy.jpa;

import com.adgadev.jplusone.core.proxy.Identifier;
import com.adgadev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import javax.persistence.EntityManager;

@Slf4j
public class EntityManagerAopProxyFactory {

    public static EntityManager createProxy(EntityManager target, StateListener stateListener, Identifier identifier) {
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new EntityManagerMethodInterceptor(stateListener, identifier));
        // TODO: switch to use cglib proxy via setTargetClass/setProxyTargetClass

        return (EntityManager) proxyFactory.getProxy();
    }

    @RequiredArgsConstructor
    private static class EntityManagerMethodInterceptor implements MethodInterceptor {

        private final StateListener stateListener;

        private final Identifier identifier;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();

            if (methodInvocation.getMethod().getName().equals("close")) {
                stateListener.entityManagerClosed(identifier);
            }

            return result;
        }
    }
}