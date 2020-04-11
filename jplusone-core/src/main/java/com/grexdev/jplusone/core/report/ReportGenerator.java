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

package com.grexdev.jplusone.core.report;

import com.grexdev.jplusone.core.frame.FrameExtract;
import com.grexdev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties;
import com.grexdev.jplusone.core.registry.OperationNode;
import com.grexdev.jplusone.core.registry.OperationNode.OperationType;
import com.grexdev.jplusone.core.registry.SessionNode;
import com.grexdev.jplusone.core.registry.StatementNode;
import com.grexdev.jplusone.core.registry.StatementNode.StatementType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ReportGenerator {

    private final JPlusOneReportProperties reportProperties;

    public void handleRecordedSession(SessionNode session) {
        if (reportProperties.isEnabled()) {
            Set<OperationType> visibleOperationsType = reportProperties.getOperationFilteringMode().getOperationTypes();
            Set<StatementType> visibleStatementsType = reportProperties.getStatementFilteringMode().getStatementTypes();

            boolean matchedStatementAndOperationFound = session.getOperations().stream()
                    .filter(operationNode -> visibleOperationsType.contains(operationNode.getOperationType()))
                    .flatMap(operationNode -> operationNode.getStatements().stream())
                    .filter(statementNode -> visibleStatementsType.contains(statementNode.getStatementType()))
                    .count() > 0;

            if (matchedStatementAndOperationFound) {
                log.debug(sessionToString(session, visibleOperationsType, visibleStatementsType));
            } else {
                log.debug("No operations / statements matching report criteria found");
            }
        }
    }

    private String sessionToString(SessionNode session, Set<OperationType> visibleOperationsType, Set<StatementType> visibleStatementsType) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tROOT");

        for (FrameExtract frame : session.getSessionCallFrameStack().getCallFrames()) {
            if (frame.isNotThirdPartyClass()) {
                builder.append("\n\t\t");
                builder.append(frame.format());
            }
        }

        builder.append("\n\t\t\tSESSION BOUNDARY");

        for (OperationNode operation : session.getOperations()) {
            if (visibleOperationsType.contains(operation.getOperationType())) {
                builder.append("\n\t\t\t\tOPERATION [" + operation.getOperationType() + "]");

                for (FrameExtract frame : operation.getCallFramesStack().getCallFrames()) {
                    if (frame.isNotThirdPartyClass()) {
                        builder.append("\n\t\t\t\t\t");
                        builder.append(frame.format());
                    }
                }

                if (operation.getLazyInitialisation() != null) {
                    builder.append("\n\t\t\t\t\t" + operation.getLazyInitialisation());
                }

                for (StatementNode statement : operation.getStatements()) {
                    if (visibleStatementsType.contains(statement.getStatementType())) {
                        builder.append("\n\t\t\t\t\t\tSTATEMENT [" + statement.getStatementType() + "]");
                        builder.append(ReportSqlFormatter.formatSql("\t\t\t\t\t\t\t", statement.getSql()));
                    }
                }
            }
        }

        return builder.toString();
    }

}
