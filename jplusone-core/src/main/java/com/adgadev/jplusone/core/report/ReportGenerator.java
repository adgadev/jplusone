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

package com.adgadev.jplusone.core.report;

import com.adgadev.jplusone.core.frame.FrameExtract;
import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties;
import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.Output;
import com.adgadev.jplusone.core.registry.LazyInitialisation;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.adgadev.jplusone.core.utils.CollectionUtils.getLastItemOfList;
import static com.adgadev.jplusone.core.utils.StreamUtils.filterToList;

@Slf4j
public class ReportGenerator {

    private final String NEWLINE = "\n";

    private static String INDENT = "    ";

    private final Map<Integer, String> INDENTS = Map.of(
            1, INDENT,
            2, INDENT + INDENT,
            3, INDENT + INDENT + INDENT,
            4, INDENT + INDENT + INDENT + INDENT,
            5, INDENT + INDENT + INDENT + INDENT + INDENT,
            6, INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
            7, INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT
    );

    private final JPlusOneReportProperties reportProperties;

    public ReportGenerator(JPlusOneReportProperties reportProperties) {
        this.reportProperties = reportProperties;

        if (!reportProperties.isEnabled()) {
            log.debug("JPlusOne report generation is disabled");
        }
    }

    public void handleRecordedSession(SessionNodeView session) {
        if (reportProperties.isEnabled()) {
            Set<OperationType> visibleOperationsType = reportProperties.getOperationFilteringMode().getOperationTypes();
            Set<StatementType> visibleStatementsType = reportProperties.getStatementFilteringMode().getStatementTypes();

            boolean matchedStatementAndOperationFound = session.getOperations().stream()
                    .filter(operationNode -> visibleOperationsType.contains(operationNode.getOperationType()))
                    .flatMap(operationNode -> operationNode.getStatements().stream())
                    .filter(statementNode -> visibleStatementsType.contains(statementNode.getStatementType()))
                    .count() > 0;

            if (matchedStatementAndOperationFound) {
                printLog(sessionToString(session, visibleOperationsType, visibleStatementsType));
            } else {
                printLog("No operations / statements matching report criteria found");
            }
        }
    }

    private String sessionToString(SessionNodeView session, Set<OperationType> visibleOperationsType, Set<StatementType> visibleStatementsType) {
        StringBuilder builder = new StringBuilder();
        builder.append(NEWLINE + INDENTS.get(1) + "ROOT");

        List<FrameExtract> sessionCallFrames = session.getSessionFrameStack().getCallFrames();

        for (FrameExtract frame : filterApplicationCallFrames(sessionCallFrames, reportProperties.isProxyCallFramesHidden())) {
            if (frame.isNotThirdPartyClass()) {
                builder.append(NEWLINE + INDENTS.get(2));
                builder.append(frame.format());
            }
        }

        builder.append(NEWLINE + INDENTS.get(3) + "SESSION BOUNDARY");

        for (OperationNodeView operation : session.getOperations()) {
            if (visibleOperationsType.contains(operation.getOperationType())
                    && containsAnyVisibleStatements(operation, visibleStatementsType)) {

                builder.append(NEWLINE + INDENTS.get(4) + "OPERATION [" + operation.getOperationType() + "]");
                List<FrameExtract> operationCallFrames = operation.getCallFramesStack().getCallFrames();

                for (FrameExtract frame : filterApplicationCallFrames(operationCallFrames, reportProperties.isProxyCallFramesHidden())) {
                    if (frame.isNotThirdPartyClass()) {
                        builder.append(NEWLINE + INDENTS.get(5));
                        builder.append(frame.format());
                    }
                }

                for (LazyInitialisation lazyInitialisation: operation.getLazyInitialisations()) {
                    builder.append(NEWLINE + INDENTS.get(5) + lazyInitialisation);
                }

                for (StatementNodeView statement : operation.getStatements()) {
                    if (visibleStatementsType.contains(statement.getStatementType())) {
                        builder.append(NEWLINE + INDENTS.get(6) + "STATEMENT [" + statement.getStatementType().getStatementGroupType() + "]");
                        builder.append(ReportSqlFormatter.formatSql(INDENTS.get(7), statement.getSql()));
                    }
                }
            }
        }

        return builder.toString();
    }

    private boolean containsAnyVisibleStatements(OperationNodeView operation, Set<StatementType> visibleStatementsType) {
        return operation.getStatements().stream()
                .filter(statementNode -> visibleStatementsType.contains(statementNode.getStatementType()))
                .findFirst()
                .isPresent();
    }

    private List<FrameExtract> filterApplicationCallFrames(List<FrameExtract> callFrames, boolean proxyCallFramesHidden) {
        List<FrameExtract> applicationAllCallFrames = filterToList(callFrames, FrameExtract::isNotThirdPartyClass);

        if (!proxyCallFramesHidden) {
            return applicationAllCallFrames;
        } else {
            List<FrameExtract> result = filterToList(applicationAllCallFrames, FrameExtract::isApplicationClass);

            if (!applicationAllCallFrames.isEmpty()) {
                Optional<FrameExtract> lastApplicationCallFrame = getLastItemOfList(applicationAllCallFrames);
                Optional<FrameExtract> lastApplicationClassCallFrame = getLastItemOfList(result);

                if (lastApplicationCallFrame.isPresent() && !lastApplicationCallFrame.equals(lastApplicationClassCallFrame)) {
                    result.add(lastApplicationCallFrame.get());
                }
            }

            return result;
        }
    }

    private void printLog(String message) {
        if (reportProperties.getOutput() == Output.LOGGER) {
            log.debug(message);
        } else if (reportProperties.getOutput() == Output.STDOUT) {
            System.out.println(message);
        }
    }
}
