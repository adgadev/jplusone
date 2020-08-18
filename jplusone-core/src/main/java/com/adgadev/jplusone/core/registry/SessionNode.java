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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionNode implements SessionNodeView {

    private final List<OperationNode> operations;

    private final FrameStack sessionFrameStack;

    public static SessionNode create() {
        return new SessionNode(new ArrayList<>(), null);
    }

    public void addStatement(String sql, FrameStack operationFramesStack) {
        getLastOperationNode()
                .filter(operationNode -> operationNode.hasCallFramesStack(operationFramesStack))
                .ifPresentOrElse(
                        addStatementToLastOperation(sql),
                        addStatementToNewOperation(sql, operationFramesStack));
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

    private Runnable addStatementToNewOperation(String sql, FrameStack operationFrameStack) {
        return () -> {
            OperationNode operationNode = new OperationNode(operationFrameStack);
            operations.add(operationNode);
            operationNode.addStatement(sql);
        };
    }

    public SessionNode close(FrameStack completeSessionFrameStack) {
        if (operations.isEmpty()) {
            log.warn("Closing empty SessionNode");
            return new SessionNode(emptyList(), sessionFrameStack);

        } else {
            OperationNode firstOperationNode = operations.iterator().next();
            FrameStack completeOperationFramesStack = firstOperationNode.getCallFramesStack();
            FrameStack sessionFrameStack = completeSessionFrameStack.intersect(completeOperationFramesStack);

            List<OperationNode> closedOperations = operations.stream()
                    .map(operationNode -> operationNode.close(sessionFrameStack))
                    .collect(toList());

            return new SessionNode(closedOperations, sessionFrameStack);
        }
    }
}