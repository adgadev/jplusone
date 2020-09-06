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

import com.adgadev.jplusone.core.proxy.Identifier;
import com.adgadev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class ActivationStateListener implements StateListener {

    private final StateListener delegate;

    private final TrackingContext context;

    @Override
    public void entityManagerCreated(Identifier entityManagerId) {
        if (context.isRecordingEnabled()) {
            executeSilently(() -> delegate.entityManagerCreated(entityManagerId));
        }
    }

    @Override
    public void entityManagerClosed(Identifier entityManagerId) {
        if (context.isRecordingEnabled()) {
            executeSilently(() -> delegate.entityManagerClosed(entityManagerId));
        }
    }

    @Override
    public void statementExecuted(String sql) {
        if (context.isRecordingEnabled()) {
            executeSilently(() -> delegate.statementExecuted(sql));
        }
    }

    @Override
    public void statementExecuted(Supplier<String> sqlSupplier) {
        if (context.isRecordingEnabled()) {
            executeSilently(() -> delegate.statementExecuted(sqlSupplier));
        }
    }

    @Override
    public void lazyCollectionInitialized(String entityClassName, String fieldName) {
        if (context.isRecordingEnabled()) {
            executeSilently(() -> delegate.lazyCollectionInitialized(entityClassName, fieldName));
        }
    }

    private void executeSilently(Runnable eventHandlerAction) {
        try {
            eventHandlerAction.run();
        } catch (Exception e) {
            log.warn("JPlusOne failed to handle JPA/hibernate event", e);
        }
    }

}
