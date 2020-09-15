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

import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties;
import com.adgadev.jplusone.core.registry.FrameStack;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.core.report.ReportGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.OperationFilteringMode.ALL_OPERATIONS;
import static com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.StatementFilteringMode.ALL_STATEMENTS;

public class ReportFragmentGenerator {

    String generate(SessionNodeView session, List<OperationNodeView> operations) {
        return generate(session, operations, null);
    }

    String generate(SessionNodeView session, List<OperationNodeView> operations, Set<StatementType> filteredStatementTypes) {
        JPlusOneReportProperties properties = new JPlusOneReportProperties();
        properties.setOperationFilteringMode(ALL_OPERATIONS);
        properties.setStatementFilteringMode(ALL_STATEMENTS);

        ReportGenerator reportGenerator = new ReportGenerator(properties);
        SessionFragment sessionFragment = new SessionFragment(session, operations);
        Set<OperationType> operationTypes = properties.getOperationFilteringMode().getOperationTypes();
        Set<StatementType> statementTypes = Optional.ofNullable(filteredStatementTypes)
                .orElse(properties.getStatementFilteringMode().getStatementTypes());

        return reportGenerator.sessionToString(sessionFragment, operationTypes, statementTypes);
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
