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

import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.core.report.output.ReportChannel;
import com.adgadev.jplusone.core.report.output.ReportChannel.MessageType;
import com.adgadev.jplusone.core.report.output.ReportChannelFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ReportGenerator {

    private final JPlusOneReportProperties reportProperties;

    private final ReportChannel reportChannel;

    private final SessionFormatter sessionFormatter;

    public ReportGenerator(JPlusOneReportProperties reportProperties, ReportChannelFactory reportChannelFactory) {
        Set<OperationType> visibleOperationsType = reportProperties.getOperationFilteringMode().getOperationTypes();
        Set<StatementType> visibleStatementsType = reportProperties.getStatementFilteringMode().getStatementTypes();

        this.reportProperties = reportProperties;
        this.reportChannel = reportChannelFactory.createReportChannel(log);
        this.sessionFormatter = new SessionFormatter(visibleOperationsType, visibleStatementsType, reportProperties.isProxyCallFramesHidden());

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
                reportChannel.printMessage(MessageType.REPORT, sessionFormatter.format(session));
            } else {
                reportChannel.printMessage(MessageType.TEXT, "No operations / statements matching report criteria found");
            }
        }
    }
}
