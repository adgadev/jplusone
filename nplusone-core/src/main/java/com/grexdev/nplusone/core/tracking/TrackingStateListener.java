package com.grexdev.nplusone.core.tracking;

import com.grexdev.nplusone.core.frame.FramesProvider;
import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.registry.RootNode;
import com.grexdev.nplusone.core.registry.SessionNode;
import com.grexdev.nplusone.core.registry.SessionToString;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final RootNode root;

    private final boolean debugMode;

    private final FramesProvider framesProvider;

    private final ThreadLocal<SessionStack> currentSessionStack = new ThreadLocal<>();

    public TrackingStateListener(TrackingContext context, RootNode root) {
        this.root = root;
        this.debugMode = context.isDebugMode();
        this.framesProvider = new FramesProvider(context.getApplicationRootPackage(), context.isDebugMode());
    }

    @Override
    public void sessionCreated() {
        if (debugMode) {
            log.debug("########## SESSION CREATED ##########");
        }

        SessionNode session = SessionNode.of(framesProvider.captureCallFrames());
        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            sessionStack.increaseSessionAmount();
        } else {
            root.addSession(session);
            currentSessionStack.set(SessionStack.of(session));
        }
    }

    @Override
    public void sessionClosed() {
        if (debugMode) {
            log.debug("########## SESSION CLOSED ##########");
        }

        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            handleRecordedSession(sessionStack.outerSessionNode);
            sessionStack.decreaseSessionAmount();

            if (sessionStack.allSessionsClosed()) {
                currentSessionStack.remove();
            }

        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void statementExecuted(String sql) {
        if (debugMode) {
            log.info("########## STATEMENT EXECUTED: {} ##########", sql);
        }

        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            SessionNode session = sessionStack.outerSessionNode;
            session.addStatement(sql, framesProvider.captureCallFrames());
        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        statementExecuted(sqlSupplier.get());
    }

    private void handleRecordedSession(SessionNode session) {
        log.info(SessionToString.toString(session));
    }

    @AllArgsConstructor
    private static class SessionStack {

        private final SessionNode outerSessionNode;

        private int nestedSessionsAmount;

        private static SessionStack of(SessionNode sessionNode) {
            return new SessionStack(sessionNode, 1);
        }

        private void increaseSessionAmount() {
            nestedSessionsAmount++;
        }

        private void decreaseSessionAmount() {
            nestedSessionsAmount--;
        }

        private boolean allSessionsClosed() {
            return nestedSessionsAmount == 0;
        }
    }
}
