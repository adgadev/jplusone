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

package com.adgadev.jplusone.core.tracking;

import com.adgadev.jplusone.core.frame.FramesProvider;
import com.adgadev.jplusone.core.proxy.Identifier;
import com.adgadev.jplusone.core.proxy.StateListener;
import com.adgadev.jplusone.core.registry.FrameStack;
import com.adgadev.jplusone.core.registry.LazyInitialisation;
import com.adgadev.jplusone.core.registry.RootNode;
import com.adgadev.jplusone.core.registry.SessionNode;
import com.adgadev.jplusone.core.report.ReportGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class TrackingStateListener implements StateListener {

    private final RootNode root;

    private final FramesProvider framesProvider;

    private final ReportGenerator reportGenerator;

    private final ThreadLocal<Stack<SessionNode>> currentSessionStack = ThreadLocal.withInitial(() -> new Stack<>());

    // TODO: verify content of non closed currentSessionStack after all session closed

    public TrackingStateListener(TrackingContext context, ReportGenerator reportGenerator, RootNode root) {
        this.root = root;
        this.reportGenerator = reportGenerator;
        this.framesProvider = new FramesProvider(context.getApplicationRootPackage());
    }

    @Override
    public void entityManagerCreated(Identifier entityManagerId) {
        SessionNode session = SessionNode.create();
        Stack<SessionNode> sessionStack = currentSessionStack.get();

        if (!sessionStack.isEmpty()) {
            log.debug("Entity manager session is nested in another entity manager session");
        }

        sessionStack.push(session);
    }

    @Override
    public void entityManagerClosed(Identifier entityManagerId) {
        Stack<SessionNode> sessionStack = currentSessionStack.get();

        if (!sessionStack.empty()) {
            SessionNode session = sessionStack.pop();
            FrameStack frameStack = framesProvider.captureCallFrames();
            SessionNode optimizedSession = session.close(frameStack);
            root.addSession(optimizedSession);

            reportGenerator.handleRecordedSession(optimizedSession);

            if (sessionStack.empty()) {
                currentSessionStack.remove();
            }

        } else {
            log.warn("Session has been closed already");
        }
    }

    @Override
    public void statementExecuted(String sql) {
        Stack<SessionNode> sessionStack = currentSessionStack.get();
        FrameStack frameStack = framesProvider.captureCallFrames();

        if (isExecutedByEntityManager(frameStack) || isExecutedByJpaQuery(frameStack)) {
            if (!sessionStack.empty()) {
                SessionNode session = sessionStack.peek();
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
        Stack<SessionNode> sessionStack = currentSessionStack.get();

        if (!sessionStack.empty()) {
            SessionNode session = sessionStack.peek();
            session.addLazyCollectionInitialisation(LazyInitialisation.collectionLazyInitialisation(entityClassName, fieldName));

        } else {
            log.warn("Session has been closed already");
        }
    }

    private boolean isExecutedByEntityManager(FrameStack frameStack) {
        return frameStack
                .findLastMatchingFrame(frameExtract -> EntityManager.class.isAssignableFrom(frameExtract.getClazz()))
                .isPresent();
    }

    private boolean isExecutedByJpaQuery(FrameStack frameStack) {
        return frameStack
                .findLastMatchingFrame(frameExtract -> Query.class.isAssignableFrom(frameExtract.getClazz()))
                .isPresent();
    }
}
