package com.grexdev.nplusone.core.proxy;

import java.util.function.Supplier;

public interface StateListener {

    void sessionCreated();

    void sessionClosed();

    void statementExecuted(String sql);

    void statementExecuted(Supplier<String> sqlSupplier);

    void lazyCollectionInitialized(String entityClassName, String fieldName);
}
