/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grexdev.jplusone.core.tracking;

import com.grexdev.jplusone.core.frame.FramesProvider;
import com.grexdev.jplusone.core.proxy.Identifier;
import com.grexdev.jplusone.core.proxy.StateListener;
import com.grexdev.jplusone.core.registry.FrameStack;
import com.grexdev.jplusone.core.registry.RootNode;
import com.grexdev.jplusone.core.registry.SessionNode;
import com.grexdev.jplusone.core.report.ReportGenerator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

import static com.grexdev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final RootNode root;

    private final FramesProvider framesProvider;

    private final ReportGenerator reportGenerator;

    private final ThreadLocal<SessionStack> currentSessionStack = new ThreadLocal<>();

    public TrackingStateListener(TrackingContext context, ReportGenerator reportGenerator, RootNode root) {
        this.root = root;
        this.reportGenerator = reportGenerator;
        this.framesProvider = new FramesProvider(context.getApplicationRootPackage());
    }

    @Override
    public void entityManagerCreated(Identifier entityManagerId) {
        SessionNode session = SessionNode.of(framesProvider.captureCallFrames());
        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            log.debug("SESSION STACK CREATED - NESTED, {}", sessionStack);
            sessionStack.increaseSessionAmount();

        } else {
            SessionStack newSessionStack = SessionStack.of(session);
            root.addSession(session);
            currentSessionStack.set(newSessionStack);
            log.debug("SESSION STACK CREATED - OUTER, {}", newSessionStack);
        }
    }

    @Override
    public void entityManagerClosed(Identifier entityManagerId) {
        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            reportGenerator.handleRecordedSession(sessionStack.outerSessionNode); // TODO: what about nested session?
            sessionStack.decreaseSessionAmount();

            if (sessionStack.allSessionsClosed()) {
                currentSessionStack.remove();
                log.debug("SESSION STACK DELETED - OUTER, {}", sessionStack);
            } else {
                log.debug("SESSION STACK DELETED - NESTED, {}", sessionStack);
            }

        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void transactionStarted(Identifier transactionId) {
        // TODO: implement
    }

    @Override
    public void transactionFinished(Identifier transactionId) {
        // TODO: implement
    }

    @Override
    public void statementExecuted(String sql) {
        SessionStack sessionStack = currentSessionStack.get();
        FrameStack frameStack = framesProvider.captureCallFrames();

        if (isExecutedByEntityManager(frameStack)) {
            if (sessionStack != null) {
                log.debug("SESSION USE, {}", sessionStack);
                SessionNode session = sessionStack.outerSessionNode;
                session.addStatement(sql, frameStack);
            } else {
                log.warn("Session has been closed already");
            }
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        statementExecuted(sqlSupplier.get());
    }

    @Override
    public void lazyCollectionInitialized(String entityClassName, String fieldName) {
        SessionStack sessionStack = currentSessionStack.get();

        if (sessionStack != null) {
            SessionNode session = sessionStack.outerSessionNode;
            session.addLazyCollectionInitialisation(collectionLazyInitialisation(entityClassName, fieldName));

        } else {
            log.warn("Session has been closed already");
        }
    }

    private boolean isExecutedByEntityManager(FrameStack frameStack) {
        return frameStack
                .findLastMatchingFrame(frameExtract -> EntityManager.class.isAssignableFrom(frameExtract.getClazz()))
                .isPresent();
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
