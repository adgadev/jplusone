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

import com.grexdev.jplusone.core.frame.FrameExtract;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Getter
public class OperationNode {

    public enum OperationType {IMPLICIT_FETCH, EXPLICIT_FETCH}

    private final List<StatementNode> statements = new ArrayList<>();

    private final FrameStack callFramesStack;

    private OperationType operationType;

    private LazyInitialisation lazyInitialisation;

    public OperationNode(FrameStack callFramesStack) {
        this.callFramesStack = callFramesStack;
        this.lazyInitialisation = resolveLazyInitialisationDetails(callFramesStack);
        this.operationType = nonNull(lazyInitialisation) ? OperationType.IMPLICIT_FETCH : OperationType.EXPLICIT_FETCH;
    }

    void addStatement(String sql) {
        StatementNode statement = StatementNode.fromSql(sql);
        statements.add(statement);
    }

    void addLazyInitialisation(LazyInitialisation lazyInitialisation) {
        if (this.lazyInitialisation == null) {
            this.lazyInitialisation = lazyInitialisation;
            this.operationType = OperationType.IMPLICIT_FETCH;
        } else {
            log.warn("Details of operation lazy initialisation already captured, bug?");
        }
    }

    boolean hasCallFramesStack(FrameStack callFramesStack) {
        return this.callFramesStack.equals(callFramesStack);
    }

    private LazyInitialisation resolveLazyInitialisationDetails(FrameStack callFramesStack) {
        return callFramesStack.findLastMatchingFrame(FrameExtract::isNotThirdPartyClass)
                .map(frameExtract -> frameExtract.getClazz())
                .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                .map(Class::getCanonicalName)
                .map(LazyInitialisation::entityLazyInitialisation)
                .orElse(null);
    }
}
