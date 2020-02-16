package com.grexdev.nplusone.core.tracking;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class ActivationStateListener implements StateListener {

    private final StateListener delegate;

    private final TrackingContext context;

    @Override
    public void sessionCreated() {
        if (context.isRecording()) {
            delegate.sessionCreated();
        }
    }

    @Override
    public void sessionClosed() {
        if (context.isRecording()) {
            delegate.sessionClosed();
        }
    }

    @Override
    public void statementExecuted(String sql) {
        if (context.isRecording()) {
            delegate.statementExecuted(sql);
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        if (context.isRecording()) {
            delegate.statementExecuted(sqlSupplier);
        }
    }
}
