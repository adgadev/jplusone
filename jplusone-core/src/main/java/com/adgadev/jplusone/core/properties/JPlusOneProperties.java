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

package com.adgadev.jplusone.core.properties;

import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.core.tracking.VerbosityLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumSet;
import java.util.Set;

import static com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.OperationFilteringMode.IMPLICIT_OPERATIONS_ONLY;
import static com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.Output.LOGGER;
import static com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.StatementFilteringMode.READ_STATEMENTS_ONLY;

@Getter
@Setter
@ConfigurationProperties("jplusone")
public class JPlusOneProperties {

    private String applicationRootPackage;

    private boolean enabled = true;

    private boolean debugMode = false;

    private VerbosityLevel verbosityLevel = VerbosityLevel.V3;

    private JPlusOneReportProperties report = new JPlusOneReportProperties();

    @Getter
    @Setter
    public static class JPlusOneReportProperties {

        @Getter
        @RequiredArgsConstructor
        public enum OperationFilteringMode {
            IMPLICIT_OPERATIONS_ONLY(EnumSet.of(OperationType.IMPLICIT)),
            EXPLICIT_OPERATIONS_ONLY(EnumSet.of(OperationType.EXPLICIT)),
            ALL_OPERATIONS(EnumSet.of(OperationType.IMPLICIT, OperationType.EXPLICIT));

            private final Set<OperationType> operationTypes;
        }

        @Getter
        @RequiredArgsConstructor
        public enum StatementFilteringMode {
            READ_STATEMENTS_ONLY(EnumSet.of(StatementType.SELECT)),
            WRITE_STATEMENTS_ONLY(EnumSet.of(StatementType.INSERT, StatementType.UPDATE, StatementType.DELETE)),
            OTHER_STATEMENTS_ONLY(EnumSet.of(StatementType.OTHER)),
            ALL_STATEMENTS(EnumSet.of(StatementType.SELECT, StatementType.INSERT, StatementType.UPDATE, StatementType.DELETE, StatementType.OTHER));

            private final Set<StatementType> statementTypes;
        }

        public enum Output {
            LOGGER,
            STDOUT
        }

        private boolean enabled = true;

        private boolean proxyCallFramesHidden = true;

        private OperationFilteringMode operationFilteringMode = IMPLICIT_OPERATIONS_ONLY;

        private StatementFilteringMode statementFilteringMode = READ_STATEMENTS_ONLY;

        private Output output = LOGGER;

    }

}
