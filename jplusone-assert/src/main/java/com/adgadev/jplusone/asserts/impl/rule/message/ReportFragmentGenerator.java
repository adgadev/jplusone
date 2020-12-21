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

package com.adgadev.jplusone.asserts.impl.rule.message;

import com.adgadev.jplusone.core.registry.FrameStack;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.core.report.SessionFormatter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReportFragmentGenerator {

    String generate(SessionNodeView session, List<OperationNodeView> operations) {
        return generate(session, operations, null);
    }

    String generate(SessionNodeView session, List<OperationNodeView> operations, Set<StatementType> filteredStatementTypes) {
        Set<OperationType> operationTypes = Set.of(OperationType.values());
        Set<StatementType> statementTypes = Optional.ofNullable(filteredStatementTypes)
                .orElseGet(() -> Set.of(StatementType.values()));

        SessionFormatter sessionFormatter = new SessionFormatter(operationTypes, statementTypes, true);
        SessionFragment sessionFragment = new SessionFragment(session, operations);

        return sessionFormatter.format(sessionFragment);
    }

    @RequiredArgsConstructor
    private static class SessionFragment implements SessionNodeView {

        private final SessionNodeView session;

        @Getter
        private final List<OperationNodeView> operations;

        @Override
        public FrameStack getSessionFrameStack() {
            return session.getSessionFrameStack();
        }
    }
}
