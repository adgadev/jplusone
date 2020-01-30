package com.grexdev.nplusone.core.proxy.jpa;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class EntityManagerFactoryProxy implements EntityManagerFactory {

    @Delegate(excludes = EntityManagerFactoryOverwrite.class)
    private final EntityManagerFactory delegate;

    private final StateListener stateListener;

    @Override
    public EntityManager createEntityManager() {
        EntityManager entityManager = delegate.createEntityManager();
        stateListener.sessionCreated();
        return new EntityManagerProxy(entityManager, stateListener);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager entityManager = delegate.createEntityManager(map);
        stateListener.sessionCreated();
        return new EntityManagerProxy(entityManager, stateListener);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType);
        stateListener.sessionCreated();
        return new EntityManagerProxy(entityManager, stateListener);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType, map);
        stateListener.sessionCreated();;
        return new EntityManagerProxy(entityManager, stateListener);
    }

    private interface EntityManagerFactoryOverwrite {

        EntityManager createEntityManager();

        EntityManager createEntityManager(Map var1);

        EntityManager createEntityManager(SynchronizationType var1);

        EntityManager createEntityManager(SynchronizationType var1, Map var2);

    }
}
