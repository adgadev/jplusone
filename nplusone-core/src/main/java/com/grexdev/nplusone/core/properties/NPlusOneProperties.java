package com.grexdev.nplusone.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

        public enum OperationFilteringMode {
            IMPLICIT_OPERATIONS_ONLY,
            EXPLICIT_OPERATIONS_ONLY,
            ALL_OPERATIONS
        }

        public enum StatementFilteringMode {
            READ_STATEMENTS_ONLY,
            WRITE_STATEMENTS_ONLY,
            ALL_STATEMENTS
        }

        private boolean enabled = true;

        private OperationFilteringMode operationFilteringMode = ALL_OPERATIONS;

        private StatementFilteringMode statementFilteringMode = READ_STATEMENTS_ONLY;

    }

}
