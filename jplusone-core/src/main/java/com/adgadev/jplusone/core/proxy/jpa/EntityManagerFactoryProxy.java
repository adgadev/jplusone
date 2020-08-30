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

import com.adgadev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;
import java.util.Map;

@Slf4j
@Deprecated
@RequiredArgsConstructor
public class EntityManagerFactoryProxy implements EntityManagerFactory {

    @Delegate(excludes = EntityManagerFactoryOverwrite.class)
    private final EntityManagerFactory delegate;

    private final StateListener stateListener;

    @Override
    public EntityManager createEntityManager() {
        EntityManager entityManager = delegate.createEntityManager();
        return wrapEntityManager(entityManager);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType);
        return wrapEntityManager(entityManager);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager entityManager = delegate.createEntityManager(map);
        return wrapEntityManager(entityManager);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType, map);
        return wrapEntityManager(entityManager);
    }

    private EntityManager wrapEntityManager(EntityManager entityManager) {
        EntityManagerProxy entityManagerProxy = new EntityManagerProxy(entityManager, stateListener);
        stateListener.entityManagerCreated(entityManagerProxy.getIdentifier());
        return entityManagerProxy;
    }

    private interface EntityManagerFactoryOverwrite {

        EntityManager createEntityManager();

        EntityManager createEntityManager(Map var1);

        EntityManager createEntityManager(SynchronizationType var1);

        EntityManager createEntityManager(SynchronizationType var1, Map var2);

    }
}
