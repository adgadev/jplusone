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

package com.grexdev.jplusone.core.proxy.jpa;

import com.grexdev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityTransaction;

@Slf4j
@RequiredArgsConstructor
public class EntityTransactionProxy implements EntityTransaction {

    @Delegate(excludes = EntityTransactionOverwrite.class)
    private final EntityTransaction delegate;

    private final StateListener stateListener;

    @Override
    public void begin() {
        delegate.begin();
        stateListener.transactionStarted();
    }

    @Override
    public void commit() {
        delegate.commit();
        stateListener.transactionFinished();
    }

    @Override
    public void rollback() {
        delegate.rollback();
        stateListener.transactionFinished();
    }

    private interface EntityTransactionOverwrite {

        void begin();

        void commit();

        void rollback();
    }
}
