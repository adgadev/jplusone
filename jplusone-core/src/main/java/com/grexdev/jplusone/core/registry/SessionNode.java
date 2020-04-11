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

package com.grexdev.jplusone.core.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Getter
@RequiredArgsConstructor(staticName = "of")
public class SessionNode {

    private final List<OperationNode> operations = new ArrayList<>();

    private final FrameStack initialCallFrameStack;

    private FrameStack sessionCallFrameStack;

    public void addStatement(String sql, FrameStack completeOperationFramesStack) {
        if (sessionCallFrameStack == null) {
            sessionCallFrameStack = initialCallFrameStack.intersection(completeOperationFramesStack);
        }

        FrameStack operationSubFramesStack = completeOperationFramesStack.substract(sessionCallFrameStack);

        getLastOperationNode()
                .filter(operationNode -> operationNode.hasCallFramesStack(operationSubFramesStack))
                .ifPresentOrElse(
                        addStatementToLastOperation(sql),
                        addStatementToNewOperation(sql, operationSubFramesStack));
    }

    public void addLazyCollectionInitialisation(LazyInitialisation lazyInitialisation) {
        getLastOperationNode()
                .ifPresent(operationNode -> operationNode.addLazyInitialisation(lazyInitialisation));
    }

    private Optional<OperationNode> getLastOperationNode() {
        return operations.isEmpty()
                ? Optional.empty()
                : Optional.of(operations.get(operations.size() - 1));
    }

    private Consumer<OperationNode> addStatementToLastOperation(String sql) {
        return operationNode -> operationNode.addStatement(sql);
    }

    private Runnable addStatementToNewOperation(String sql, FrameStack operationSubFrameStack) {
        return () -> {
            OperationNode operationNode = new OperationNode(operationSubFrameStack);
            operations.add(operationNode);
            operationNode.addStatement(sql);
        };
    }

}