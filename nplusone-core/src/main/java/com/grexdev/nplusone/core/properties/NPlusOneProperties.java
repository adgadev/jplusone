package com.grexdev.nplusone.core.properties;

import com.grexdev.nplusone.core.registry.OperationNode.OperationType;
import com.grexdev.nplusone.core.registry.StatementNode.StatementType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumSet;
import java.util.Set;

import static com.grexdev.nplusone.core.properties.NPlusOneProperties.NPlusOneReportProperties.OperationFilteringMode.ALL_OPERATIONS;
import static com.grexdev.nplusone.core.properties.NPlusOneProperties.NPlusOneReportProperties.StatementFilteringMode.READ_STATEMENTS_ONLY;

@Getter
@Setter
@ConfigurationProperties("nplusone")
public class NPlusOneProperties {

    private String applicationRootPackage;

    private boolean enabled = true;

    private boolean debugMode = false;

    private NPlusOneReportProperties report = new NPlusOneReportProperties();

    @Getter
    @Setter
    public static class NPlusOneReportProperties {

        @Getter
        @RequiredArgsConstructor
        public enum OperationFilteringMode {
            IMPLICIT_FETCH_OPERATIONS_ONLY(EnumSet.of(OperationType.IMPLICIT_FETCH)),
            EXPLICIT_FETCH_OPERATIONS_ONLY(EnumSet.of(OperationType.EXPLICIT_FETCH)),
            ALL_OPERATIONS(EnumSet.of(OperationType.IMPLICIT_FETCH, OperationType.EXPLICIT_FETCH));

            private final Set<OperationType> operationTypes;
        }

        @Getter
        @RequiredArgsConstructor
        public enum StatementFilteringMode {
            READ_STATEMENTS_ONLY(EnumSet.of(StatementType.READ)),
            WRITE_STATEMENTS_ONLY(EnumSet.of(StatementType.WRITE)),
            ALL_STATEMENTS(EnumSet.of(StatementType.READ, StatementType.WRITE));

            private final Set<StatementType> statementTypes;
        }

        private boolean enabled = true;

        private OperationFilteringMode operationFilteringMode = ALL_OPERATIONS;

        private StatementFilteringMode statementFilteringMode = READ_STATEMENTS_ONLY;

    }

}
