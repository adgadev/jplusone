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
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class LoggingStateListener implements StateListener {

    private final VerbosityLevel verbosityLevel;

    private final StateListener stateListener;

    private final FramesProvider framesProvider;

    public LoggingStateListener(TrackingContext context, StateListener stateListener) {
        this.verbosityLevel = context.isDebugMode() ? context.getVerbosityLevel() : VerbosityLevel.V0;
        this.stateListener = stateListener;
        this.framesProvider = new FramesProvider(context.getApplicationRootPackage());
    }

    @Override
    public void entityManagerCreated(Identifier entityManagerId) {
        if (verbosityLevel.isDebugModeEnabled()) {
            if (verbosityLevel.isSessionStackVisible()) {
                framesProvider.captureCallFrames().printStackTrace("JPA EntityManager created, ID: " + entityManagerId);
            } else {
                log.debug("JPA EntityManager created, ID: {}", entityManagerId);
            }
        }

        stateListener.entityManagerCreated(entityManagerId);
    }

    @Override
    public void entityManagerClosed(Identifier entityManagerId) {
        if (verbosityLevel.isDebugModeEnabled()) {
            if (verbosityLevel.isSessionStackVisible()) {
                framesProvider.captureCallFrames().printStackTrace("JPA Session closed, ID: " + entityManagerId);
            } else {
                log.debug("JPA EntityManager closed, ID: {}", entityManagerId);
            }
        }

        stateListener.entityManagerClosed(entityManagerId);
    }

    @Override
    public void transactionStarted(Identifier transactionId) {
        if (verbosityLevel.isDebugModeEnabled()) {
            if (verbosityLevel.isTransactionStackVisible()) {
                framesProvider.captureCallFrames().printStackTrace("JPA Transaction started, ID: " + transactionId);
            } else {
                log.debug("JPA Transaction started, ID: {}", transactionId);
            }
        }

        stateListener.transactionStarted(transactionId);
    }

    @Override
    public void transactionFinished(Identifier transactionId) {
        if (verbosityLevel.isDebugModeEnabled()) {
            if (verbosityLevel.isTransactionStackVisible()) {
                framesProvider.captureCallFrames().printStackTrace("JPA Transaction finished, ID: " + transactionId);
            } else {
                log.debug("JPA Transaction finished, ID: {}", transactionId);
            }
        }

        stateListener.transactionFinished(transactionId);
    }

    @Override
    public void statementExecuted(String sql) {
        if (verbosityLevel.isDebugModeEnabled()) {
            if (verbosityLevel.isSqlStatementStackVisible()) {
                String label = verbosityLevel.isSqlStatementVisible()
                    ? "SQL Statement executed, SQL: " + sql
                    : "SQL Statement executed";
                framesProvider.captureCallFrames().printStackTrace(label);
            } else if (verbosityLevel.isSqlStatementVisible()){
                log.debug("SQL Statement executed, SQL: {}", sql);
            } else {
                log.debug("SQL Statement executed");
            }
        }

        stateListener.statementExecuted(sql);
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        statementExecuted(sqlSupplier.get());
    }

    @Override
    public void lazyCollectionInitialized(String entityClassName, String fieldName) {
        if (verbosityLevel.isDebugModeEnabled()) {
            log.debug("Hibernate collection initialised: {}.{}", entityClassName, fieldName);
        }

        stateListener.lazyCollectionInitialized(entityClassName, fieldName);
    }
}
