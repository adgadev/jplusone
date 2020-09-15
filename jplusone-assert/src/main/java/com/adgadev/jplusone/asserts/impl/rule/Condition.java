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

package com.adgadev.jplusone.asserts.impl.rule;

import com.adgadev.jplusone.asserts.impl.rule.exclusion.OperationExclusionList;
import com.adgadev.jplusone.asserts.impl.rule.message.AmountAssertionMessageTemplate;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.adgadev.jplusone.asserts.impl.util.ValidationUtils.ensureThat;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Slf4j
public class Condition {

    @Setter
    private AmountVerifier amountVerifier;

    @Setter
    private Set<StatementType> statementTypes;

    @Setter
    private OperationType operationType;

    @Setter
    private OperationExclusionList operationExclusionList;

    public void check(SessionNodeView session, List<OperationNodeView> operations) {
        if (operationType != null) {
            checkOperation(session, operations);
        } else {
            checkStatements(session, operations);
        }
    }

    private void checkStatements(SessionNodeView session, List<OperationNodeView> operations) {
        ensureThat(nonNull(amountVerifier), "amountVerifier is not set");
        ensureThat(nonNull(statementTypes), "statementTypes is not set");

        int numberOfStatements = (int) operations.stream()
                .map(OperationNodeView::getStatements)
                .flatMap(List::stream)
                .filter(statement -> statementTypes.contains(statement.getStatementType()))
                .count();

        amountVerifier.checkAmount(numberOfStatements,
                () -> AmountAssertionMessageTemplate.forStatements(session, operations, statementTypes));
    }

    private void checkOperation(SessionNodeView session, List<OperationNodeView> operations) {
        ensureThat(nonNull(amountVerifier), "amountVerifier is not set");
        ensureThat(nonNull(operationType), "operationType is not set");
        ensureThat(nonNull(operationExclusionList), "operationExclusionList is not set");

        List<OperationNodeView> matchingOperations = operations.stream()
                .filter(operation -> operation.getOperationType() == operationType)
                .collect(collectingAndThen(toList(), this::filterExcludedOperations));

        int numberOfOperations = matchingOperations.size();

        amountVerifier.checkAmount(numberOfOperations,
                () -> AmountAssertionMessageTemplate.forOperations(session, matchingOperations, operationType));
    }

    private List<OperationNodeView> filterExcludedOperations(List<OperationNodeView> operations) {
        return Optional.ofNullable(operationExclusionList)
                .map(exclusionList -> exclusionList.filter(operations))
                .orElse(operations);
    }
}
