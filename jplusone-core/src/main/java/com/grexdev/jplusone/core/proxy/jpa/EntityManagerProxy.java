package com.grexdev.jplusone.core.proxy.jpa;

import com.grexdev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
class EntityManagerProxy implements EntityManager {

    @Delegate(excludes = EntityManagerOverwrite.class)
    private final EntityManager delegate;

    private final StateListener stateListener;

    @Override
    public void close() {
        stateListener.sessionClosed();
        delegate.close();
    }

    private interface EntityManagerOverwrite {
        void close();
    }
}
