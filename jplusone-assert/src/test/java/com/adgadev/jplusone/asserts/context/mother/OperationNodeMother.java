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

package com.adgadev.jplusone.asserts.context.mother;

import com.adgadev.jplusone.asserts.context.stub.OperationNodeStub;
import com.adgadev.jplusone.core.registry.LazyInitialisation;
import com.adgadev.jplusone.core.registry.OperationType;

import static com.adgadev.jplusone.asserts.context.mother.FrameStackMother.anyFrameStackForOperation;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anySelectStatementNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anyUpdateStatementNode;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.entityLazyInitialisation;
import static java.util.Arrays.asList;

public class OperationNodeMother {

    public static OperationNodeStub anyImplicitOperationNode() {
        return anyEntityLazyInitialisationOperationNode();
    }

    public static OperationNodeStub anyEntityLazyInitialisationOperationNode() {
        return anyImplicitOperationNode(entityLazyInitialisation(SampleEntityA.class.getCanonicalName()));
    }

    public static OperationNodeStub anyCollectionLazyInitialisationOperationNode() {
        return anyImplicitOperationNode(collectionLazyInitialisation(SampleEntityB.class.getCanonicalName(), "tags"));
    }

    public static OperationNodeStub anyImplicitOperationNode(LazyInitialisation lazyInitialisation) {
        return OperationNodeStub.builder()
                .operationType(OperationType.IMPLICIT)
                .callFramesStack(anyFrameStackForOperation())
                .statements(asList(anySelectStatementNode()))
                .lazyInitialisations(asList(lazyInitialisation))
                .build();
    }

    public static OperationNodeStub anyExplicitOperationNode() {
        return OperationNodeStub.builder()
                .operationType(OperationType.EXPLICIT)
                .callFramesStack(anyFrameStackForOperation())
                .statements(asList(anyUpdateStatementNode()))
                .lazyInitialisations(null)
                .build();
    }

    public static OperationNodeStub anyCommitOperationNode() {
        return OperationNodeStub.builder()
                .operationType(OperationType.COMMIT)
                .callFramesStack(anyFrameStackForOperation())
                .statements(asList(anyUpdateStatementNode()))
                .lazyInitialisations(null)
                .build();
    }

    public static class SampleEntityA {
    }

    public static class SampleEntityB {
    }
}
