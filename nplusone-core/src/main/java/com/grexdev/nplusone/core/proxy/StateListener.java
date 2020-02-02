package com.grexdev.nplusone.core.proxy;

public interface StateListener {

    void sessionCreated();

    void sessionClosed();

    void statementExecuted(String sql);
}
