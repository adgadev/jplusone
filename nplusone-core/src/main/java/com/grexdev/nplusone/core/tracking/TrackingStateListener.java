package com.grexdev.nplusone.core.tracking;

import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.stacktrace.StackTraceAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final TrackingContext context;

    @Override
    public void sessionCreated() {
        if (context.isRecording()) {
            log.info("########## SESSION CREATED ##########");
        }
    }

    @Override
    public void sessionClosed() {
        if (context.isRecording()) {
            log.info("########## SESSION CLOSED ##########");
        }
    }

    @Override
    public void statementExecuted(String sql) {
        if (context.isRecording()) {
            log.info("########## STATEMENT EXECUTED: {} ##########", sql);
            new StackTraceAnalyzer().check();
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        if (context.isRecording()) {
            log.info("########## STATEMENT EXECUTED: {} ##########", sqlSupplier.get());
            new StackTraceAnalyzer().check();
        }
    }
}
