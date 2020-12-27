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
import javax.persistence.EntityManagerFactory;

@Slf4j
public class EntityManagerFactoryAopProxyFactory {

    public static EntityManagerFactory createProxy(EntityManagerFactory target, StateListener stateListener) {
        // dynamic proxy is needed to enhance EntityManagerFactory with interface EntityManagerFactoryInfo
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new EntityManagerFactoryInfoMethodInterceptor(stateListener));
        proxyFactory.addAdvice(new EntityManagerFactoryMethodInterceptor(stateListener));
        // TODO: switch to use cglib proxy via setTargetClass/setProxyTargetClass

        return (EntityManagerFactory) proxyFactory.getProxy();
    }

    @RequiredArgsConstructor
    private static class EntityManagerFactoryMethodInterceptor implements MethodInterceptor {

        private final StateListener stateListener;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();
            String methodName = methodInvocation.getMethod().getName();

            if (methodName.equals("createEntityManager") || methodName.equals("createNativeEntityManager")) {
                log.trace("Returning wrapped entityManager");
                return wrapEntityManager((EntityManager) result);
            }

            return result;
        }

        private EntityManager wrapEntityManager(EntityManager entityManager) {
            Identifier entityManagerIdentifier = Identifier.nextEntityManagerIdentifier();
            log.trace("EntityManager {} is associated with object {}", entityManagerIdentifier.toString(), entityManager.toString());
            EntityManager entityManagerProxy = EntityManagerAopProxyFactory.createProxy(entityManager, stateListener, entityManagerIdentifier);
            stateListener.entityManagerCreated(entityManagerIdentifier);
            return entityManagerProxy;
        }
    }

    @RequiredArgsConstructor
    private static class EntityManagerFactoryInfoMethodInterceptor implements MethodInterceptor {

        private final StateListener stateListener;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();

            if (methodInvocation.getMethod().getName().equals("getNativeEntityManagerFactory")) {
                log.trace("Returning wrapped nativeEntityManagerFactory");
                return wrapEntityManagerFactory((EntityManagerFactory) result);
            }

            return result;
        }

        private EntityManagerFactory wrapEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            return EntityManagerFactoryAopProxyFactory.createProxy(entityManagerFactory, stateListener);
        }
    }
}