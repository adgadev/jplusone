package com.grexdev.nplusone.core.proxy.hibernate;

import com.grexdev.nplusone.core.utils.ReflectionUtils;
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

    public HibernateCollectionInitialisationEventListener(EntityManagerFactory entityManagerFactory) {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.INIT_COLLECTION).appendListener(this);
    }

    @Override
    public void onInitializeCollection(InitializeCollectionEvent event) throws HibernateException {
        String entityClassName = event.getAffectedOwnerEntityName();
        Object association = event.getCollection();

        Optional.ofNullable(event.getAffectedOwnerOrNull())
                .flatMap(owner -> ReflectionUtils.findObjectFieldByFieldValue(owner, association))
                .map(Field::getName)
                .ifPresent(fieldName -> handleCollectionInitialisation(entityClassName, fieldName));
    }

    private void handleCollectionInitialisation(String entityClassName, String fieldName) {
        log.debug("Hibernate association {}.{} initialized", entityClassName, fieldName);
    }
}
