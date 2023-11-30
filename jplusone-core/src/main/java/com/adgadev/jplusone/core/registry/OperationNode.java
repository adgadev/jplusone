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

package com.adgadev.jplusone.core.registry;

import com.adgadev.jplusone.core.frame.FrameExtract;
import com.adgadev.jplusone.core.registry.LazyInitialisation.LazyInitialisationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationNode implements OperationNodeView {

    private final List<StatementNode> statements;

    private final FrameStack callFramesStack;

    private OperationType operationType;

    private List<LazyInitialisation> lazyInitialisations;

    public OperationNode(FrameStack callFramesStack) {
        LazyInitialisation lazyInitialisation = resolveLazyInitialisationDetails(callFramesStack);
        boolean transactionCommitOperation = isTransactionCommitOperation(callFramesStack);

        this.statements = new ArrayList<>();
        this.lazyInitialisations = new ArrayList<>();
        this.callFramesStack = callFramesStack;
        this.operationType = resolveOperationType(lazyInitialisation, transactionCommitOperation);

        if (nonNull(lazyInitialisation)) {
            this.lazyInitialisations.add(lazyInitialisation);
        }
    }

    void addStatement(String sql) {
        StatementNode statement = StatementNode.fromSql(sql);
        statements.add(statement);
    }

    void addLazyInitialisation(LazyInitialisation lazyInitialisation) {
        if (this.lazyInitialisations.isEmpty()) {
            this.lazyInitialisations.add(lazyInitialisation);

            if (operationType == OperationType.EXPLICIT) {
                this.operationType = OperationType.IMPLICIT;
            }

        } if (!lazyInitialisations.contains(lazyInitialisation)) {
            LazyInitialisation lazyInitialisationToRemove = LazyInitialisation.entityLazyInitialisation(lazyInitialisation.getEntityClassName());

            if (lazyInitialisationToRemove.equals(getEntityLazyInitialisation())) {
                this.lazyInitialisations.remove(lazyInitialisationToRemove);
                this.lazyInitialisations.add(lazyInitialisation);
            }
        }
    }

    OperationNode close(FrameStack sessionFrameStack) {
        FrameStack operationSubFramesStack = callFramesStack.subtract(sessionFrameStack);

        return new OperationNode(statements, operationSubFramesStack, operationType, lazyInitialisations);
    }

    boolean hasCallFramesStack(FrameStack callFramesStack) {
        return this.callFramesStack.equals(callFramesStack);
    }

    private boolean isTransactionCommitOperation(FrameStack callFramesStack) {
        // TODO: proxy EntityTransaction instead of walking whole frame stack toimprove performance
        return callFramesStack.findLastMatchingFrame(frameExtract -> frameExtract.matchesMethodInvocation(EntityTransaction.class, "commit"))
                .isPresent();
    }

    private LazyInitialisation resolveLazyInitialisationDetails(FrameStack callFramesStack) {
        return callFramesStack.findLastMatchingFrame(FrameExtract::isNotThirdPartyClass)
                .map(frameExtract -> frameExtract.getClazz())
                .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                .map(Class::getCanonicalName)
                .map(LazyInitialisation::entityLazyInitialisation)
                .orElse(null);
    }

    private LazyInitialisation getEntityLazyInitialisation() {
        return lazyInitialisations.stream()
                .filter(initialisation -> initialisation.getType() == LazyInitialisationType.ENTITY)
                .findFirst()
                .orElse(null);
    }

    private OperationType resolveOperationType(LazyInitialisation lazyInitialisation, boolean transactionCommitOperation) {
        if (transactionCommitOperation) {
            return OperationType.COMMIT;
        } else if (lazyInitialisation != null) {
            return OperationType.IMPLICIT;
        } else {
            return OperationType.EXPLICIT;
        }
    }
}
