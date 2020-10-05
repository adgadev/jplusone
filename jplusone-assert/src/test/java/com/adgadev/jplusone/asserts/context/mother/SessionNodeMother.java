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
import com.adgadev.jplusone.asserts.context.stub.SessionNodeStub;

import java.util.List;

import static com.adgadev.jplusone.asserts.context.mother.FrameStackMother.anyFrameStackForSession;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyExplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static java.util.Arrays.asList;

public class SessionNodeMother {

    public static SessionNodeStub anySessionNodeWithImplicitOperation() {
        return anySessionNode(anyImplicitOperationNode());
    }

    public static SessionNodeStub anySessionNodeWithExplicitOperation() {
        return anySessionNode(anyExplicitOperationNode());
    }

    public static SessionNodeStub anySessionNode(OperationNodeStub operation) {
        return anySessionNode(asList(operation));
    }

    public static SessionNodeStub anySessionNode(List<OperationNodeStub> operations) {
        return SessionNodeStub.builder()
                .sessionFrameStack(anyFrameStackForSession())
                .operations(operations)
                .build();
    }
}
