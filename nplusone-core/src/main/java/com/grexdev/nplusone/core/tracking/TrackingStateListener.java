package com.grexdev.nplusone.core.tracking;

import com.grexdev.nplusone.core.frame.FramesProvider;
import com.grexdev.nplusone.core.proxy.StateListener;
import com.grexdev.nplusone.core.registry.RootNode;
import com.grexdev.nplusone.core.registry.SessionNode;
import com.grexdev.nplusone.core.registry.SessionToString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final RootNode root;

    private final FramesProvider framesProvider;

    private final ThreadLocal<SessionNode> currentSession = new ThreadLocal<>();

    public TrackingStateListener(TrackingContext context, RootNode root) {
        this.root = root;
        this.framesProvider = new FramesProvider(context.getApplicationRootPackage());
    }

    @Override
    public void sessionCreated() {
        log.info("########## SESSION CREATED ##########");

        SessionNode session = SessionNode.of(framesProvider.captureCallFrames());
        root.addSession(session);
        currentSession.set(session);
    }

    @Override
    public void sessionClosed() {
        log.info("########## SESSION CLOSED ##########");

        if (currentSession.get() != null) {
            log.info(SessionToString.toString(currentSession.get()));
            currentSession.remove();

        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void statementExecuted(String sql) {
        log.info("########## STATEMENT EXECUTED: {} ##########", sql);

        SessionNode session = currentSession.get();

        if (session != null) {
            session.addStatement(sql, framesProvider.captureCallFrames());
        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        statementExecuted(sqlSupplier.get());
    }
}
