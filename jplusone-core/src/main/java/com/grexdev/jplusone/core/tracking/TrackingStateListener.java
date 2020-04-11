package com.grexdev.jplusone.core.tracking;

import com.grexdev.jplusone.core.report.ReportGenerator;
import com.grexdev.jplusone.core.frame.FramesProvider;
import com.grexdev.jplusone.core.proxy.StateListener;
import com.grexdev.jplusone.core.registry.RootNode;
import com.grexdev.jplusone.core.registry.SessionNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static com.grexdev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final RootNode root;

    private final boolean debugMode;

    private final FramesProvider framesProvider;

    private final ReportGenerator reportGenerator;

    private final ThreadLocal<SessionStack> currentSessionStack = new ThreadLocal<>();

    public TrackingStateListener(TrackingContext context, ReportGenerator reportGenerator, RootNode root) {
        this.root = root;
        this.debugMode = context.isDebugMode();
        this.reportGenerator = reportGenerator;
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
            reportGenerator.handleRecordedSession(sessionStack.outerSessionNode);
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

    @Override
    public void lazyCollectionInitialized(String entityClassName, String fieldName) {
        if (debugMode) {
            log.info("########## COLLECTION INITIALIZED: {}.{} ##########", entityClassName, fieldName);
        }

        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            SessionNode session = sessionStack.outerSessionNode;
            session.addLazyCollectionInitialisation(collectionLazyInitialisation(entityClassName, fieldName));

        } else {
            log.warn("Session has been closed already");
        }
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
