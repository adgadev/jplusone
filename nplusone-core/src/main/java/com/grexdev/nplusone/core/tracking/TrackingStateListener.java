package com.grexdev.nplusone.core.tracking;

import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.stacktrace.StackTraceAnalyzer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackingStateListener implements StateListener {

    @Override
    public void sessionCreated() {
        log.info("########## SESSION CREATED ##########");
    }

    @Override
    public void sessionClosed() {
        log.info("########## SESSION CLOSED ##########");
    }

    @Override
    public void statementExecuted() {
        log.info("########## STATEMENT EXECUTED ##########");
        new StackTraceAnalyzer().check();
    }
}
