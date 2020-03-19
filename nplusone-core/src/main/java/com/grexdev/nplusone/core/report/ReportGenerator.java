package com.grexdev.nplusone.core.report;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.grexdev.nplusone.core.frame.FrameExtract;
import com.grexdev.nplusone.core.properties.NPlusOneProperties.NPlusOneReportProperties;
import com.grexdev.nplusone.core.registry.OperationNode;
import com.grexdev.nplusone.core.registry.OperationNode.OperationType;
import com.grexdev.nplusone.core.registry.SessionNode;
import com.grexdev.nplusone.core.registry.StatementNode;
import com.grexdev.nplusone.core.registry.StatementNode.StatementType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ReportGenerator {

    private final NPlusOneReportProperties reportProperties;

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

                for (StatementNode statement : operation.getStatements()) {
                    if (visibleStatementsType.contains(statement.getStatementType())) {
                        builder.append("\n\t\t\t\t\t\tSTATEMENT [" + statement.getStatementType() + "]");
                        builder.append(formatSql("\t\t\t\t\t\t\t", statement.getSql()));
                    }
                }
            }
        }

        return builder.toString();
    }

    private String formatSql(String intend, String sql) {
        String sqlFormatterIntend = " ";
        String formattedSql = SqlFormatter.format(sql, sqlFormatterIntend);
        String[] lines = formattedSql.split("\n");
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("from")) {
                builder.append(' ');
            } else if (!line.startsWith(" ")) {
                builder.append('\n');
                builder.append(intend);
                builder.append(sqlFormatterIntend);
            }

            builder.append(line, 0, line.length());
        }

        return builder.toString();
    }
}
