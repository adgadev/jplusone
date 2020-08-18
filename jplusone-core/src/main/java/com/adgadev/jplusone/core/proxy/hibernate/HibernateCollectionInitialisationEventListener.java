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

package com.adgadev.jplusone.core.proxy.hibernate;

import com.adgadev.jplusone.core.utils.ReflectionUtils;
import com.adgadev.jplusone.core.tracking.ActivationStateListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;
import org.hibernate.internal.SessionFactoryImpl;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
public class HibernateCollectionInitialisationEventListener implements InitializeCollectionEventListener {

    private final ActivationStateListener stateListener;

    public HibernateCollectionInitialisationEventListener(EntityManagerFactory entityManagerFactory, ActivationStateListener stateListener) {
        this.stateListener = stateListener;

        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.INIT_COLLECTION).appendListener(this);
    }

    @Override
    public void onInitializeCollection(InitializeCollectionEvent event) throws HibernateException {
        try {
            String entityClassName = event.getAffectedOwnerEntityName();
            Object association = event.getCollection();

            Optional.ofNullable(event.getAffectedOwnerOrNull())
                    .flatMap(owner -> ReflectionUtils.findObjectFieldByFieldValue(owner, association))
                    .map(Field::getName)
                    .ifPresent(fieldName -> handleCollectionInitialisation(entityClassName, fieldName));

        } catch (Exception e) {
            log.warn("JPlusOne failed to handle hibernate collection intialisation event", e);
        }
    }

    private void handleCollectionInitialisation(String entityClassName, String fieldName) {
        stateListener.lazyCollectionInitialized(entityClassName, fieldName);
    }
}
