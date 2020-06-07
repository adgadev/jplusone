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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.Stack;
import java.util.function.Supplier;

import static com.grexdev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;

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
    public void transactionStarted(Identifier transactionId) {
        // TODO: implement
    }

    @Override
    public void transactionFinished(Identifier transactionId) {
        // TODO: implement
    }

    @Override
    public void statementExecuted(String sql) {
        Stack<SessionNode> sessionStack = currentSessionStack.get();
        FrameStack frameStack = framesProvider.captureCallFrames();

        if (isExecutedByEntityManager(frameStack)) {
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
}
